package cinema.dao;
import cinema.models.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private List<Employee> employeeList = new ArrayList<>();
    public EmployeeDAO() {
        employeeList = getAllEmployeesFromDB();
    }

   public boolean addEmployee(Employee employee) {
    if (findById(employee.getId()) != null) return false;

    String sql = "INSERT INTO employee (EmployeeId, EmployeeName, EmployeePhone, EmployeeEmail, " +
                 "Position, salary, hireDate, username, password, status, note) " +
                 "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    try (java.sql.Connection conn = cinema.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, employee.getId());
        ps.setString(2, employee.getName());
        ps.setString(3, employee.getPhone());
        ps.setString(4, employee.getEmail());
        ps.setString(5, employee.getPosition().getDisplayName());
        ps.setDouble(6, employee.getSalary());
        ps.setDate(7, java.sql.Date.valueOf(employee.getHireDate()));
        ps.setString(8, employee.getUsername());
        ps.setString(9, employee.getPassword());
        ps.setString(10, employee.getStatus().name());
        ps.setString(11, "");
        int rows = ps.executeUpdate();
        if (rows > 0) {
            employeeList.add(employee);
            return true;
        }
    } catch (java.sql.SQLException e) {
        System.err.println("LỖI ADD EMPLOYEE: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
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

    public String getNextEmployeeId() {
    String sql = "SELECT MAX(CAST(SUBSTRING(EmployeeId, 4) AS UNSIGNED)) FROM employee";
    try (java.sql.Connection conn = cinema.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            int maxId = rs.getInt(1);
            return String.format("EMP%03d", maxId + 1);
        }
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    }
    return "EMP001";
}
   public boolean updateEmployee(Employee emp) {
    Employee old = findById(emp.getId());
    if (old != null) {
        old.setName(emp.getName());
        old.setPhone(emp.getPhone());
        old.setEmail(emp.getEmail());
        old.setPosition(emp.getPosition());
        old.setStatus(emp.getStatus());
        old.setUsername(emp.getUsername());
        old.setPassword(emp.getPassword());
        old.setSalary(emp.getSalary());
    }

    String sql = "UPDATE employee SET EmployeeName=?, EmployeePhone=?, EmployeeEmail=?, " +
                 "Position=?, salary=?, status=?, username=?, password=? WHERE EmployeeId=?";
    try (java.sql.Connection conn = cinema.DBConnection.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, emp.getName());
        ps.setString(2, emp.getPhone());
        ps.setString(3, emp.getEmail());
        ps.setString(4, emp.getPosition().getDisplayName());
        ps.setDouble(5, emp.getSalary());
        ps.setString(6, emp.getStatus().name());
        ps.setString(7, emp.getUsername());
        ps.setString(8, emp.getPassword());
        ps.setString(9, emp.getId());
        return ps.executeUpdate() > 0;
    } catch (java.sql.SQLException e) {
        System.err.println("LỖI UPDATE EMPLOYEE: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
// TKE:
   public List<Object[]> getTop5NhanVienBanVeThang() {
    List<Object[]> list = new ArrayList<>();

    String sql = "SELECT e.EmployeeId, e.EmployeeName, COUNT(i.invoiceId) as soLuongVe " +
                 "FROM invoice i " +
                 "JOIN employee e ON e.EmployeeId = i.employeeId " +
                 "WHERE MONTH(i.invoiceDate) = MONTH(CURDATE()) " +
                 "  AND YEAR(i.invoiceDate) = YEAR(CURDATE()) " +
                 "  AND e.Position = 'Nhan vien ban ve' " +
                 "GROUP BY i.employeeId " +
                 "ORDER BY soLuongVe DESC " +
                 "LIMIT 5";

    try (Connection conn = cinema.DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new Object[]{
                rs.getString("EmployeeId"),
                rs.getString("EmployeeName"),
                rs.getInt("soLuongVe")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
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
