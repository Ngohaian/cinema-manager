
import java.util.ArrayList;
import java.util.List;

public class CinemaManager {
    private List<Movie> movies;
    private List<ShowTime> showTimes;
    private List<Employee> employees;   
    private List<Customer> customers;
    private List<Room> rooms;
    private List<Invoice> invoices;

    public CinemaManager(){
        movies = new ArrayList<>();
        showTimes = new ArrayList<>();
        employees = new ArrayList<>();
        customers = new ArrayList<>();
        rooms = new ArrayList<>();
        invoices = new ArrayList<>();
    }
    // login
    public Employee getPosition(String username, String password){
        for(Employee emp : employees){
            if(emp.getUsername().equals(username) && emp.getPassword().equals(password)){
                return emp;
            }
        }
        return null;
    }
    

}

