package Main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class TimerApplication extends Application {
    //    long start_time; // why is this long i forgot
    private TimerLogic timerlogic;
    Button startBtn, stopBtn, pauseBtn, resumeBtn;
    private static Label timerLbl;


    @Override
    public void start(Stage primaryStage) throws Exception {
        timerlogic = new TimerLogic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/tomatoUI.fxml"));
        Parent root = loader.load();
        startBtn = (Button) loader.getNamespace().get("startBtn");
        stopBtn = (Button) loader.getNamespace().get("stopBtn");
        timerLbl = (Label) loader.getNamespace().get("timerLabel");
        pauseBtn = (Button) loader.getNamespace().get("pauseBtn");
        resumeBtn = (Button) loader.getNamespace().get("resumeBtn");
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
                timerLbl.setText(formatInterval(timerLogic.getNanoSeconds()));
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
