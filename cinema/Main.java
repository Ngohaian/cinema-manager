package cinema;

import cinema.DBConnection;
import cinema.form.LoginFrame; 
public class Main {
    public static void main(String[] args) {
        CinemaManager cinemaManager = new CinemaManager();
        cinemaManager.inDSPhim();
    }
}
