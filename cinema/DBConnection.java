package cinema;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection conn = null;

        try {

            String url = "jdbc:mysql://127.0.0.1:3306/`cinema-manage`";
            String user = "root";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}