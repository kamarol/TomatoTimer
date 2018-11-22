package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerLogic {
    boolean running = false; // i was here last at 2018-11-12 at 10:50am Monday
    long start_time = 0L;
    long backlog = 0L;
//    long current = 0L;



    public void resetTimer() {
        start_time = 0L;
        backlog = 0L;
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
        backlog =  getIntervalInNanoSeconds(); // TODO: bug: click pausing multiple times will actually break the logic
        // code to insert to database
        Connection c = null;
        try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test3.db");
            addTimePauseToDb(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

        running = false;
    }

    private void addTimePauseToDb(Connection c) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1, d2;
        d1 = new Date(System.currentTimeMillis() - (getIntervalInNanoSeconds()/1000000));
        d2 = new Date(System.currentTimeMillis());
        System.out.println(df.format(d1));
        System.out.println(df.format(d2));
        String SQL = String.format("INSERT INTO `Tomato` (startDate, endDate) VALUES ('%s', '%s');", df.format(d1), df.format(d2)); // TODO: Change to prepared statements
        System.out.println(SQL);
        try {
            Statement stmt = c.createStatement();
            stmt.execute(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void resume() {
//        start_time += backlog;
//        backlog = 0;
        startTimer();
    }
}
