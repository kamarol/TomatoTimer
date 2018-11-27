package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class TimerApplication extends Application {
    //    long start_time; // why is this long i forgot
    private TimerLogic timerlogic;
    Button startBtn, stopBtn, pauseBtn, resumeBtn, statsBtn, break1Button, break2Button;
    ToggleButton countdownToggleBtn;
    private static Label timerLbl;
    Boolean countdown = false;
    ProgressIndicator tomatoIndicator;
    final static int TOMATO_SECONDS = 5;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test3.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");


        timerlogic = new TimerLogic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/tomatoUI.fxml"));
        Parent root = loader.load();
        startBtn = (Button) loader.getNamespace().get("startBtn");
        stopBtn = (Button) loader.getNamespace().get("stopBtn");
        timerLbl = (Label) loader.getNamespace().get("timerLabel");
        pauseBtn = (Button) loader.getNamespace().get("pauseBtn");
        resumeBtn = (Button) loader.getNamespace().get("resumeBtn");
        countdownToggleBtn = (ToggleButton) loader.getNamespace().get("countdownToggle");
        tomatoIndicator = (ProgressIndicator) loader.getNamespace().get("tomatoIndicator");
        statsBtn = (Button) loader.getNamespace().get("statsBtn");
        break1Button = (Button) loader.getNamespace().get("break1Button");
        break2Button = (Button) loader.getNamespace().get("button2Break"); // TODO: Change to be consistent
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startTimerPolling();

            }


        });

        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerlogic.stopTimer();
            }
        });

        pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerlogic.pauseTimer();
            }
        });

        resumeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                timerlogic.resume();
                startTimerPolling();
            }
        });

        countdownToggleBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Boolean toggled = newValue;
//                System.out.println(oldValue.toString() + " " + newValue.toString());
                if (!toggled) {
                    System.out.println("Not toggled!");
                    countdown = false;
                } else {
                    System.out.println("toggled!");
                    countdown = true;
                }
            }
        });
        statsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage graphStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/statsUI.fxml"));
                try {
                    Parent root = loader.load();
                    graphStage.setScene(new Scene(root));
                    setupStatsScene(loader);
                    graphStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        break1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // should be similar to start
                startTimerPollingBreak(5000);

            }
        });
        break2Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startTimerPollingBreak(10000);
            }
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startTimerPolling() {
        timerlogic.startTimer();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timerlogic.isRunning()) {
                    GuiUpdateTimer(timerlogic);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t2.start();
    }

    private void startTimerPollingBreak(long miliseconds) {
        timerlogic.startTimer();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timerlogic.isRunning()) {
                    GuiUpdateTimerBreak(timerlogic, miliseconds);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t2.start();
    }


    private void GuiUpdateTimer(TimerLogic timerLogic) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Long timerUIvalue = 0L;
                final Long tomatoNanoSeconds = TimeUnit.SECONDS.toNanos(TOMATO_SECONDS);
//                        timerLbl.setText(Integer.toString(timerLogic.getSeconds()));
                if (countdown) {
                    timerUIvalue = tomatoNanoSeconds - timerLogic.getNanoSeconds();
                    timerLbl.setText(formatInterval(timerUIvalue));
                    tomatoIndicator.setProgress(((double)timerUIvalue) / (double)tomatoNanoSeconds);
                    System.out.printf("%s\n", ((double)timerUIvalue) / (double)tomatoNanoSeconds); // 1.0 - ((double)timerUIvalue / (double)_25MINUTES)
//                    System.out.println((double)(timerUIvalue / _25MINUTES));
//                    System.out.printf("%s %s %s", timerUIvalue, _25MINUTES, (double)timerUIvalue/(double)_25MINUTES); //souf daym
                } else {
                    timerUIvalue = timerLogic.getNanoSeconds();
                    timerLbl.setText(formatInterval(timerUIvalue));
                    tomatoIndicator.setProgress((double)timerUIvalue / (double)tomatoNanoSeconds);
                    System.out.printf("%s\n", (double)timerUIvalue / (double)tomatoNanoSeconds);
                }
                if (timerUIvalue > tomatoNanoSeconds) {
                    System.out.println("Finished 10 Seconds!"); //TODO: Change to x seconds
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1, d2;
                    d1 = new Date(System.currentTimeMillis() - (timerLogic.getIntervalInNanoSeconds()/1000000));
                    d2 = new Date(System.currentTimeMillis());
                    System.out.println(df.format(d1));
                    System.out.println(df.format(d2));
                    String SQL = String.format("INSERT INTO `Tomato` (startDate, endDate, duration) VALUES ('%s', '%s', %d);", df.format(d1), df.format(d2), timerlogic.getSeconds()); // TODO: Change to prepared statements
                    String SQL2 = String.format("INSERT INTO `TomatoTags` (startDate, tag) VALUES ('%s', '%s');", df.format(d1), "completed");
                    System.out.println(SQL);
                    try {
                        Connection c;
                        c = DriverManager.getConnection("jdbc:sqlite:test3.db");
                        Statement stmt = c.createStatement();
                        stmt.execute(SQL);
                        stmt.execute(SQL2);
                        timerlogic.stopTimer();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void GuiUpdateTimerBreak(TimerLogic timerLogic, long miliseconds) {  // TODO : check if mili should be stored as long?

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Long timerUIvalue = 0L;
                final Long tomatoNanoSeconds = TimeUnit.MILLISECONDS.toNanos(miliseconds);
//                        timerLbl.setText(Integer.toString(timerLogic.getSeconds()));
                if (countdown) {
                    timerUIvalue = tomatoNanoSeconds - timerLogic.getNanoSeconds();
                    timerLbl.setText(formatInterval(timerUIvalue));
                    tomatoIndicator.setProgress(((double)timerUIvalue) / (double)tomatoNanoSeconds);
                    System.out.printf("%s\n", ((double)timerUIvalue) / (double)tomatoNanoSeconds); // 1.0 - ((double)timerUIvalue / (double)_25MINUTES)
//                    System.out.println((double)(timerUIvalue / _25MINUTES));
//                    System.out.printf("%s %s %s", timerUIvalue, _25MINUTES, (double)timerUIvalue/(double)_25MINUTES); //souf daym
                } else {
                    timerUIvalue = timerLogic.getNanoSeconds();
                    timerLbl.setText(formatInterval(timerUIvalue));
                    tomatoIndicator.setProgress((double)timerUIvalue / (double)tomatoNanoSeconds);
                    System.out.printf("%s\n", (double)timerUIvalue / (double)tomatoNanoSeconds);
                }
                if (timerUIvalue > tomatoNanoSeconds) {
                    System.out.println("Finished 10 Seconds!"); //TODO: Change to x seconds
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d1, d2;
                    d1 = new Date(System.currentTimeMillis() - (timerLogic.getIntervalInNanoSeconds()/1000000));
                    d2 = new Date(System.currentTimeMillis());
                    System.out.println(df.format(d1));
                    System.out.println(df.format(d2));
                    String SQL = String.format("INSERT INTO `Tomato` (startDate, endDate, duration) VALUES ('%s', '%s', %d);", df.format(d1), df.format(d2), timerlogic.getSeconds()); // TODO: Change to prepared statements
                    String SQL2 = String.format("INSERT INTO `TomatoTags` (startDate, tag) VALUES ('%s', '%s');", df.format(d1), ("break"));
                    System.out.println(SQL);
                    try {
                        Connection c;
                        c = DriverManager.getConnection("jdbc:sqlite:test3.db");
                        Statement stmt = c.createStatement();
                        stmt.execute(SQL);
                        stmt.execute(SQL2);
                        timerlogic.stopTimer();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateLabelGUI(String s) {
        System.out.println("updating guii");
        timerLbl.setText(s);
    }


    static String formatInterval(long nanoSeconds) {
        long hours = TimeUnit.NANOSECONDS.toHours(nanoSeconds);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoSeconds - TimeUnit.HOURS.toNanos(hours));
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoSeconds - TimeUnit.HOURS.toNanos(hours) - TimeUnit.MINUTES.toNanos(minutes));
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Graph UI functions // TODO: Separate this section into another class

    private void setupStatsScene(FXMLLoader loader) {
        String tomatoDurationTodaySQL = String.format("select sum(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'completed') as TomatoTags ON Tomato.startDate = TomatoTags.startDate where Tomato.startDate >= date('now', 'localtime', 'start of day');"); // TODO: Change to prepared statements
        String breakDurationTodaySQL = String.format("select sum(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'break') as TomatoTags ON Tomato.startDate = TomatoTags.startDate where Tomato.startDate >= date('now', 'localtime', 'start of day');"); // TODO: Change to prepared statements
        String noOfBreaksTodaySQL = "select count(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'break') as TomatoTags ON Tomato.startDate = TomatoTags.startDate where Tomato.startDate >= date('now', 'localtime', 'start of day');";
        String noOfTomatoTodaySQL ="select count(Tomato.startDate) from Tomato JOIN (select * from TomatoTags where tag = 'completed') as TomatoTags ON Tomato.startDate = TomatoTags.startDate where Tomato.startDate >= date('now', 'localtime', 'start of day');";
        // total stats
        String totalTomatoDurationSQL = "select sum(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'completed') as TomatoTags ON Tomato.startDate = TomatoTags.startDate;";
        String totalBreakDurationSQL = "select sum(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'break') as TomatoTags ON Tomato.startDate = TomatoTags.startDate;";
        String totalNoOFBreaksSQL = "select count(Tomato.duration) from Tomato JOIN (select * from TomatoTags where tag = 'break') as TomatoTags ON Tomato.startDate = TomatoTags.startDate;";
        String totalNoOfTomatoSQL = "select count(Tomato.startDate) from Tomato JOIN (select * from TomatoTags where tag = 'completed') as TomatoTags ON Tomato.startDate = TomatoTags.startDate;";
        Connection c;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test3.db");
            Statement stmt = c.createStatement(); // TODO: can i re-use this?
            ResultSet rs = stmt.executeQuery(tomatoDurationTodaySQL);
            Statement stmt2 = c.createStatement();
            ResultSet rs2 = stmt2.executeQuery(breakDurationTodaySQL);
            Label tomatoTodayLabel, breaksTodayLabel, breaksDurationTodayLabel, tomatoDurationTodayLabel, totalTomatoLabel, totalBreaksLabel, totalDurationLabel, totalBreakDurationLabel;
            tomatoTodayLabel = (Label) loader.getNamespace().get("tomatoTodayLabel");
            breaksTodayLabel = (Label) loader.getNamespace().get("breaksTodayLabel");
            breaksDurationTodayLabel = (Label) loader.getNamespace().get("durationTodayLabel"); // TODO: Change in FXML
            tomatoDurationTodayLabel = (Label) loader.getNamespace().get("tomatoDurationTodayLabel");

            totalTomatoLabel = (Label) loader.getNamespace().get("totalTomatoLabel");
            totalBreaksLabel = (Label) loader.getNamespace().get("totalBreaksLabel");
            totalDurationLabel = (Label) loader.getNamespace().get("totalDurationLabel");
            totalBreakDurationLabel = (Label) loader.getNamespace().get("totalBreakDurationLabel");
            int sumOfDuration = rs.getInt(1);
//            String tomatoTodayLabelUIText = String.format("%d:%02d:%02d", sumOfDuration / 3600, (sumOfDuration % 3600) / 60, (sumOfDuration % 60));
//            int sumOfDurationBreakToday = rs2.getInt(1);
//            String breaksTodayLabelUIText = String.format("%d:%02d:%02d", sumOfDurationBreakToday / 3600, (sumOfDurationBreakToday % 3600) / 60, (sumOfDurationBreakToday % 60));
            int timeCalculation = 0;
            rs = c.createStatement().executeQuery(noOfTomatoTodaySQL);
            String tomatoTodayLabelUIText = Integer.toString(rs.getInt(1));
            rs = c.createStatement().executeQuery(noOfBreaksTodaySQL);
            String breaksTodayLabelUIText = Integer.toString(rs.getInt(1));
            rs = c.createStatement().executeQuery(breakDurationTodaySQL);
            String durationTodayLabelUIText = timeFormatter(rs.getInt(1)); //does static functions need to have certain naming scheme? Like capitalization?
            rs = c.createStatement().executeQuery(tomatoDurationTodaySQL);
            String tomatoDurationTodayLabelUIText = timeFormatter(rs.getInt(1));


            rs = c.createStatement().executeQuery(totalNoOfTomatoSQL);
            String totalTomatoLabelUIText = Integer.toString(rs.getInt(1));
            rs = c.createStatement().executeQuery(totalNoOFBreaksSQL);
            String totalBreaksLabelUIText = Integer.toString(rs.getInt(1));
            rs = c.createStatement().executeQuery(totalTomatoDurationSQL);
            String totalDurationLabelUIText = timeFormatter(rs.getInt(1));
            rs = c.createStatement().executeQuery(totalBreakDurationSQL);
            String totalBreakDurationLabelUIText = timeFormatter(rs.getInt(1));

            tomatoTodayLabel.setText(tomatoTodayLabelUIText);
            breaksTodayLabel.setText(breaksTodayLabelUIText);
            breaksDurationTodayLabel.setText(durationTodayLabelUIText);
            tomatoDurationTodayLabel.setText(tomatoDurationTodayLabelUIText);

            totalTomatoLabel.setText(totalTomatoLabelUIText);
            totalBreaksLabel.setText(totalBreaksLabelUIText);
            totalDurationLabel.setText(totalDurationLabelUIText);
            totalBreakDurationLabel.setText(totalBreakDurationLabelUIText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String timeFormatter(int seconds) {
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }


    public static void main(String[] args) {
        launch(args);
    }


}
