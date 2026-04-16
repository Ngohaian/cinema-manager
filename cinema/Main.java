package cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

import cinema.DBConnection;
import cinema.form.LoginFrame; 
public class Main {
    public static void main(String[] args) {
        CinemaManager cinemaManager = new CinemaManager();
        cinemaManager.login();
    }
}
