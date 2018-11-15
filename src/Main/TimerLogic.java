package Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerLogic {
    boolean running = false; // i was here last at 2018-11-12 at 10:50am Monday
    long start_time = 0L;
    long backlog = 0l;
//    long current = 0L;



    public void resetTimer() {
        start_time = 0L;
    }

    public void startTimer() {
        System.out.println("..Starting timer");
//        resetTimer();
        running = true;
        System.out.println("backlog: " + Long.toString(backlog));
        start_time = System.nanoTime() - backlog;
//        backlog = 0L;
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
        resetTimer();
    }

    public boolean isRunning() {
        return running;
    }

    public long getNanoSeconds() {
        return getIntervalInNanoSeconds();
    }

    public long getIntervalInNanoSeconds() {
        return System.nanoTime() - start_time;
    }

    public void pauseTimer() {
        backlog =  getIntervalInNanoSeconds();
        running = false;
    }

    public void resume() {
//        start_time += backlog;
//        backlog = 0;
        startTimer();
    }
}
