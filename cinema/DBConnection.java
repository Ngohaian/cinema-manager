package cinema;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://gateway01.ap-southeast-1.prod.aws.tidbcloud.com:4000/cinemaManager";
    private static final String USER = "4PC2jUXDSJ4Qq31.root";
    private static final String PASSWORD = "8jJQYnbmDYZbGTMP";

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