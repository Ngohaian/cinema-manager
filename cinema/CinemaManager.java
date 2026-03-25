package cinema;


import java.util.ArrayList;
import java.util.List;
import cinema.form.*;
import cinema.managers.CustomerManager;
import cinema.managers.EmployeeManager;
import cinema.managers.InvoiceManager;
import cinema.managers.MovieManager;
import cinema.managers.RoomManager;
import cinema.managers.ShowTimeManager;
import cinema.models.Employee;

public class CinemaManager {
    private CustomerManager customerManager;
    private MovieManager movieManager;
    private EmployeeManager employeeManager;
    private InvoiceManager invoiceManager;
    private MovieManager moviesManager;
    private ShowTimeManager showTimeManager;
    private RoomManager roomManager;
    

    public CinemaManager(){
        moviesManager = new MovieManager();
        showTimeManager = new ShowTimeManager();
        roomManager = new RoomManager();
        employeeManager = new EmployeeManager();
        customerManager = new CustomerManager();
        invoiceManager = new InvoiceManager();
    }

    public static void main(String[] args) {
        new login().setVisible(true);
    }

}

