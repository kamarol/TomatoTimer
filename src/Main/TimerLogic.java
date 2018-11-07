package Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerLogic implements Runnable {
    Long start_time;
    Boolean execute;

    @Override
    public void run() {
        start_time = System.currentTimeMillis();// start_time = current time
        System.out.println(this.hashCode());
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

    public void stopTimer() {
        this.execute = false;
    }
}
