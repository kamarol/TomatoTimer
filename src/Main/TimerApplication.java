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
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class TimerApplication extends Application {
    //    long start_time; // why is this long i forgot
    private TimerLogic timerlogic;
    Button startBtn, stopBtn, pauseBtn, resumeBtn;
    ToggleButton countdownToggleBtn;
    private static Label timerLbl;
    Boolean countdown = false;

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


    private void GuiUpdateTimer(TimerLogic timerLogic) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                        timerLbl.setText(Integer.toString(timerLogic.getSeconds()));
                if (countdown) {
                    final Long _25MINUTES = TimeUnit.MINUTES.toNanos(25);
                    timerLbl.setText(formatInterval(_25MINUTES - timerLogic.getNanoSeconds()));
                } else {
                    timerLbl.setText(formatInterval(timerLogic.getNanoSeconds()));
                }

            }
        });
    }

    private void updateLabelGUI(String s) {
        System.out.println("updating guii");
        timerLbl.setText(s);
    }


    public static void main(String[] args) {
        launch(args);
    }

    static String formatInterval(long nanoSeconds) {
        long hours = TimeUnit.NANOSECONDS.toHours(nanoSeconds);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoSeconds - TimeUnit.HOURS.toNanos(hours));
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoSeconds - TimeUnit.HOURS.toNanos(hours) - TimeUnit.MINUTES.toNanos(minutes));
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
