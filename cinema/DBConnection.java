package cinema;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://10.239.40.212:3306/cinemamanager";
    private static final String USER = "test";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("Ket noi that bai!");
            e.printStackTrace();
            return null;
        }
    }
}