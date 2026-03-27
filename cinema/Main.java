package cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import cinema.DBConnection; 
public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
    }
}
