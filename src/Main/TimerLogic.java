package Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerLogic implements Runnable {
    Boolean running = false; // i was here last at 2018-11-12 at 10:50am Monday
    int seconds = 0;

    @Override
    public void run() {
        running = true;
//        for (int i = 0; i < 10; i ++ ) {
//            System.out.println(seconds);
//            seconds = i;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
    }

    public int getSeconds() {
        System.out.println("fetching seconds..");
        return seconds;
    }

    public void stopTimer() {
        running = false;
    }
}
