package Main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer extends Application {
    long start_time; // why is this long i forgot
    updateGUI gui;
    Button startBtn, stopBtn;

    public void startTime() {
        start_time = System.currentTimeMillis();// start_time = current time
        gui = new updateGUI();
        Thread t = new Thread(gui);
        t.start();
        // update gui if needed
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/tomatoUI.fxml"));
        Parent root = loader.load();
        startBtn = (Button) loader.getNamespace().get("startBtn");
        stopBtn = (Button) loader.getNamespace().get("stopBtn");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startTime();
                updateGUI gui = new updateGUI();
                Thread t = new Thread(gui);
                t.start();
            }
        });

        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gui.stopUpdatingGUI();
            }
        });
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(25, 25, 25 , 25));
//
//        Timer time = new Timer();
//        primaryStage.setTitle("Tomato Timer");
//        startBtn = new Button();
//        startBtn.setText("Start");
//        startBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                time.startTime();
//                updateGUI gui = new updateGUI();
//                Thread t = new Thread(gui);
//                t.start();
//            }
//        });
//
//        stopBtn= new Button();
//        stopBtn.setText("Stop");
//        grid.getChildren().addAll(startBtn);
//        Scene scene = new Scene(grid, 300, 275); // do i need to do this after all of thiS? would i need to repaint()? if i do before?
//        primaryStage.setScene(scene);
//        primaryStage.show();

    }

//    public void stop() {
//        gui.stopUpdatingGUI();
//        // display_time = current_time - start_time
//    }

    private class updateGUI implements Runnable {
        private volatile boolean execute;

        public void run() {
            this.execute = true;
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            while (this.execute) {
                System.out.println(sdf.format(new Date(start_time))); // is this memory efficient?
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopUpdatingGUI() {
            this.execute = false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


//    public static void main(String[] args) {
//        ConsoleInputReadTask read = new ConsoleInputReadTask();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Timer timer = new Timer();
//        timer.start();
//        Future<String> future = executorService.submit(new ConsoleInputReadTask());
//        try {
//            while (!future.isDone()) {
//                Thread.sleep(1000);
//            }
//            System.out.println(future.get());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        timer.stop();
//    }
//
//
//    public static class ConsoleInputReadTask implements Callable<String> {
//        public String call() throws IOException {
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(System.in));
//            System.out.println("ConsoleInputReadTask run() called.");
//            String input;
//            do {
//                System.out.println("Please type something: ");
//                try {
//                    // wait until we have data to complete a readLine()
//                    while (!br.ready()  /*  ADD SHUTDOWN CHECK HERE */) {
//                        Thread.sleep(200);
//                    }
//                    input = br.readLine();
//                } catch (InterruptedException e) {
//                    System.out.println("ConsoleInputReadTask() cancelled");
//                    return null;
//                }
//            } while ("".equals(input));
//            System.out.println("Thank You for providing input!");
//            return input;
//        }
//    }
}
