package cinema;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
private static final String URL = 
    "jdbc:mysql://192.168.1.5:3306/cinemamanager";
    private static final String USER = "test";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Ket noi thanh cong!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Ket noi that bai!");
            e.printStackTrace();
            return null;
        }
    }
}