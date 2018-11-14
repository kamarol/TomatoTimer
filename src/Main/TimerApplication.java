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

public class TimerApplication extends Application {
    //    long start_time; // why is this long i forgot
    private TimerLogic timerlogic;
    Button startBtn, stopBtn;
    private static Label timerLbl;

//    public void startTime() {
//        start_time = System.currentTimeMillis();// start_time = current time
//        gui = new TimerLogic();
//        Thread t = new Thread(gui);
//        t.start();
//        // update gui if needed
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        timerlogic = new TimerLogic();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/tomatoUI.fxml"));
        Parent root = loader.load();
        startBtn = (Button) loader.getNamespace().get("startBtn");
        stopBtn = (Button) loader.getNamespace().get("stopBtn");
        timerLbl = (Label) loader.getNamespace().get("timerLabel");
//        timerLbl.setText("k");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startTimerPolling();
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        GuiUpdateTimer(timerlogic); // what happens if you try to put a while loop here to update GUI and sleep it? Does it affect other things?
//                    }
//                });
//                        GuiUpdateTimer(timerlogic); // what happens if you try to put a while loop here to update GUI and sleep it? Does it affect other things?

            }

            private void GuiUpdateTimer(TimerLogic timerLogic) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                while (timerlogic.isRunning()) {
//                    System.out.println("GuiUpdateTimer run()");
//                    updateLabelGUI("k");
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("egughghgh");
////                            timerLbl.setText(Double.toString(timerlogic.getTimeInSeconds()));
//                            updateLabelGUI("123");
//                        }
//                    });
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timerLbl.setText(Integer.toString(timerLogic.getSeconds()));
                    }
                });
            }

            private void startTimerPolling() {
                TimerLogic timer = new TimerLogic();
                Thread t1 = new Thread(timer);
                t1.start();
                Thread t2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
//                            int seconds = timer.getSeconds(); // putting variable declaration inside a loop?
                            GuiUpdateTimer(timer);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                t2.start();
//                Thread t = new Thread(timerlogic);
//                t.start(); // can there be more elegant/semantic way than just say start? // also make this into a function // this is called anonymous function? but it has a name
            }
        });

        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timerlogic.stopTimer();
            }
        });
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void updateLabelGUI(String s) {
        System.out.println("updating guii");
        timerLbl.setText(s);
    }
//    private class TimerLogic implements Runnable {
//        private boolean execute;
//
//        public void run() {
//            System.out.println(this.hashCode());
//            this.execute = true;
//            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
//            while (this.execute) {
//                System.out.println(sdf.format(new Date(start_time))); // is this memory efficient?
//                displayExecuteBool();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void stopUpdatingGUI() {
//            System.out.println(this.hashCode());
//            this.execute = false;
//            displayExecuteBool();
//        }
//
//        public void displayExecuteBool() {
//            System.out.println(this.execute);
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }

}
