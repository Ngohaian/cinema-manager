package cinema;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://tramway.proxy.rlwy.net:44114/railway";
    private static final String USER = "root";
    private static final String PASSWORD = "WYOkBKCsdgMioRRrGxwYzIdpFTlRwTBI";

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