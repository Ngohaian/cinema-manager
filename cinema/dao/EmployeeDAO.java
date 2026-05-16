package cinema.dao;
import cinema.models.Employee;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private List<Employee> employeeList = new ArrayList<>();
    public EmployeeDAO() {
        employeeList = getAllEmployeesFromDB();
    }

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
public List<Employee> getAllEmployeesFromDB() {
    List<Employee> list = new ArrayList<>();
    String sql = "SELECT * FROM employee";
    try (java.sql.Connection conn = cinema.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String posStr = rs.getString("Position");
            Employee.Position pos;
            switch (posStr) {
                case "Quan ly":
                    pos = Employee.Position.MANAGER; break;
                case "Nhan vien ban ve":
                    pos = Employee.Position.TICKET_SELLER; break;
                default:
                    pos = Employee.Position.TICKET_CHECKER; break;
            }

            Employee emp = new Employee(
                rs.getString("EmployeeName"),
                rs.getString("EmployeePhone"),
                rs.getString("EmployeeEmail"),
                rs.getString("username"),
                rs.getString("password"),
                pos,
                rs.getBigDecimal("salary").doubleValue() // ← fix: decimal→double
            );

            emp.setId(rs.getString("EmployeeId"));

            java.sql.Date hireDate = rs.getDate("hireDate");
            if (hireDate != null) {
                emp.setHireDate(hireDate.toLocalDate());
            }

            String statusStr = rs.getString("status");
            if ("INACTIVE".equals(statusStr)) {
                emp.deactivateEmployee();
            }

            list.add(emp);
        }

    } catch (java.sql.SQLException e) {
        System.err.println("LỖI LOAD EMPLOYEE: " + e.getMessage()); 
        e.printStackTrace();
    }
    return list;
}
    public double getMaxSalary() {
    return employeeList.stream() .mapToDouble(Employee::getSalary).max() .orElse(0);
}

    public double getMinSalary() {
        return employeeList.stream().mapToDouble(Employee::getSalary).min().orElse(0);
    }

    public List<Employee> filterEmployees(String keyword,String status,String position,double maxSalary) {
        return employeeList.stream()
            .filter(e -> (keyword.isEmpty() || e.getName().toLowerCase().contains(keyword.toLowerCase()) || e.getId().toLowerCase().contains(keyword.toLowerCase())))
            .filter(e -> status.equals("ALL")|| e.getStatus().name().equalsIgnoreCase(status))
            .filter(e ->position.equals("ALL")|| e.getPosition().name().equalsIgnoreCase(position)).filter(e -> e.getSalary() <= maxSalary).toList();
    }

    public String getNextEmployeeId() { return "EMP" + String.format("%03d", employeeList.size() + 1);}

    public boolean updateEmployee(Employee emp) {
        Employee old = findById(emp.getId());
        if (old != null) {
            old.setName(emp.getName());
            old.setPhone(emp.getPhone());
            old.setEmail(emp.getEmail());
            old.setPosition(emp.getPosition());
            old.setStatus(emp.getStatus());
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
