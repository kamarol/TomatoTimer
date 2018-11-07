package Main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TimerApplication extends Application {
//    long start_time; // why is this long i forgot
    private TimerLogic timerlogic;
    Button startBtn, stopBtn;

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
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                startTime();
                Thread t = new Thread(timerlogic);
                t.start(); // can there be more elegant/semantic way than just say start?
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
