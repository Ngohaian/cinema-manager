package cinema;


import java.util.ArrayList;
import java.util.List;

import cinema.dao.CustomerDAO;
import cinema.dao.EmployeeDAO;
import cinema.dao.InvoiceDAO;
import cinema.dao.MovieDAO;
import cinema.dao.RoomDAO;
import cinema.dao.ShowTimeDAO;
import cinema.form.*;
import cinema.models.Employee;

public class CinemaManager {
    private CustomerDAO customerManager;
    private MovieDAO movieManager;
    private EmployeeDAO employeeManager;
    private InvoiceDAO invoiceManager;
    private MovieDAO moviesManager;
    private ShowTimeDAO showTimeManager;
    private RoomDAO roomManager;
    

    public CinemaManager(){
        moviesManager = new MovieDAO();
        showTimeManager = new ShowTimeDAO();
        roomManager = new RoomDAO();
        employeeManager = new EmployeeDAO();
        customerManager = new CustomerDAO();
        invoiceManager = new InvoiceDAO();
    }

}

