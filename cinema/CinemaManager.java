package cinema;


import java.util.ArrayList;
import java.util.List;

import cinema.dao.*;
import cinema.form.*;
import cinema.models.*;

public class CinemaManager {
    private CustomerDAO customerDAO;
    private MovieDAO movieDAO = new MovieDAO();
    private EmployeeDAO employeeDAO;
    private InvoiceDAO invoiceDAO;
    private MovieDAO moviesDAO;
    private ShowTimeDAO showTimeDAO;
    private RoomDAO roomDAO;
    

    public CinemaManager(){
        showTimeDAO = new ShowTimeDAO();
        roomDAO = new RoomDAO();
        employeeDAO = new EmployeeDAO();
        customerDAO = new CustomerDAO();
        invoiceDAO = new InvoiceDAO();
    }
    public void inDSPhim(){
        List<Movie> list = movieDAO.getDSPhim();
        for(Movie m : list){
            System.out.println(m.getId() + " - " + m.getTitle() + " - " + m.getGenre() + " - " + m.getDuration() + " - " + m.isActive());
        }
    }
}

