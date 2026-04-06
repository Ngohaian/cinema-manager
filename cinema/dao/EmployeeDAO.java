package cinema.dao;
import java.util.ArrayList;
import java.util.List;

import cinema.models.Customer;
import cinema.models.Employee;

public class EmployeeDAO {
    private List<Employee> employeeList = new ArrayList<>();

    public boolean addEmployee(Employee employee){
        if(findById(employee.getId()) != null){
            return false;
        }
        employeeList.add(employee);
        return true;
    }
    public Employee findById(String id){
        for(Employee e : employeeList){
            if(e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }
    public boolean deleteEmployee(String id){
        Employee e = findById(id);
        if(e != null){
            e.deactivateEmployee();
            return true;
        }
        return false;
    }
    public void displayAllEmployees(){
        for(Employee e : employeeList){
            e.displayInfo();
            System.out.println("----------------");
        }
    }
    public boolean updateEmployee(String id,String name,String phone,String email){
        Employee e = findById(id);
        if(e != null){
            e.setName(name);
            e.setPhone(phone);
            e.setEmail(email);
            return true;
        }
        return false;
    }
        //login
    public Employee getPosition(String username, String password){
        for(Employee emp : employeeList){
            if(emp.getUsername().equals(username) && emp.getPassword().equals(password)){
                return emp;
            }
        }
        return null;
    }
}
