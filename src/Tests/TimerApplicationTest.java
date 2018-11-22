package Tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//import java.sql.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


@DisplayName("TimerApplication test example")
public class TimerApplicationTest {

    @Test
    void firstTest() {
        System.out.println("this is a test!");
    }

    //@Test
//    void secondTest() {
//        TimerApplication timer = new TimerApplication();
//        timer.start();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        timer.stop();
////        timer.getElapsedTime();
//    }

    @Test
    void sqliteTest1() {
        Connection c = null;
        String url = "jdbc:sqlite:test2.db";
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        String createTableSql = "CREATE TABLE products\n" +
                "( product_id int NOT NULL,\n" +
                "  product_name char(50) NOT NULL,\n" +
                "  category_id int,\n" +
                "  CONSTRAINT products_pk PRIMARY KEY (product_id)\n" +
                ");\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(1,'Pear',50);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(2,'Banana',50);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(3,'Orange',50);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(4,'Apple',50);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(5,'Bread',75);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(6,'Sliced Ham',25);\n" +
                "\n" +
                "INSERT INTO products\n" +
                "(product_id, product_name, category_id)\n" +
                "VALUES\n" +
                "(7,'Kleenex',null);";
        String sql1 = "SELECT COUNT(*) AS total\n" +
                "FROM products\n" +
                "WHERE category_id = 50;";
        helperSQL.execute(c, createTableSql);
//    helperSQL.execute(url, sql1);

//        try (Connection conn = c;
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql1)) {
//
//            // loop through the result set
//            while (rs.next()) {
//                System.out.println(rs.getInt(0));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    }

    static class helperSQL {
        public static void execute(Connection c, String statement) {
            try (Connection conn = c;
                 Statement stmt = conn.createStatement()) {
                // create a new table
                stmt.execute(statement);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    void dateTimeGeneratorTest() throws ParseException {
        Date d1, d2;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // i tot big M is for minutes?
        d1 =  df.parse("2018-04-01 00:00:00");
        d2 = df.parse("2018-10-31 00:00:00");

        // another one in 25 minutes
        long ONE_MINUTE_IN_MILLIS=60000;
        for (int i = 0; i < 15; i++) {
            Date randomdate = new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
            Date randomdate2 = new Date(randomdate.getTime() + ONE_MINUTE_IN_MILLIS * 25);
//            System.out.println(randomdate.toString()); // change to string formatter? stringf
            System.out.printf("('%s', '', 0),", df.format(randomdate)) ; // how to make last comma removed? maybe can use ararys?
//            System.out.println(randomdate2.toString());
        }
    }
}
