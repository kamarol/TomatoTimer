package Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerLogic {
    boolean running = false; // i was here last at 2018-11-12 at 10:50am Monday
    long start_time = 0L;
//    long current = 0L;



    public void resetTimer() {
        start_time = 0L;
    }

    public void startTimer() {
        System.out.println("..Starting timer");
        resetTimer();
        running = true;
        start_time = System.nanoTime();
    }

    public int getSeconds() {
        System.out.println("fetching seconds..");
        System.out.println(System.nanoTime() - start_time);
        int seconds = Math.toIntExact((System.nanoTime() - start_time) / 1000000000);
        System.out.println(seconds);
        return  seconds;
    }

    public void stopTimer() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
