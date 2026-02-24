import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private static final List<Employee> employeeList = new ArrayList<>();
    private static int autoId =1;

    public enum EmployeeStatus {
        ACTIVE, INACTIVE
    }

    public enum Position {
        TICKET_SELLER("Nhan vien ban ve", 1.2),
        MANAGER("Quan ly", 1.5),
        TICKET_CHECKER("Nhan vien check ve", 1.0);

        private final String displayName;
        private final double salaryMultiplier;

        Position(String displayName, double salaryMultiplier) {
            this.displayName = displayName;
            this.salaryMultiplier = salaryMultiplier;
        }

        public String getDisplayName(){return displayName;}
        public double getSalaryMultiplier(){return salaryMultiplier;}
    }

    private String id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Position position;
    private double salary;
    private LocalDate hireDate;
    private EmployeeStatus status;
    private String note;

    public Employee(String name, String phone, String email,String username, String password,Position position, double salary) {
        this.id = String.format("EMP%03d", autoId++);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.position = position;
        this.salary = salary;
        this.hireDate = LocalDate.now();
        this.status = EmployeeStatus.ACTIVE;
        this.note = "";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Position getPosition() { return position; }
    public double getSalary() { return salary; }
    public EmployeeStatus getStatus() { return status; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setNote(String note) { this.note = note; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    public double calculateSalary() {
        return salary * position.getSalaryMultiplier();
    }

    public boolean checkWorkingStatus() {
        return status == EmployeeStatus.ACTIVE;
    }

    public void changePosition(Position newPosition) {
        this.position = newPosition;
    }

    public void displayInfo() {
        System.out.println("THONG TIN NHAN VIEN");
        System.out.println("Ma nhan vien: " + id);
        System.out.println("Ten nhan vien: " + name);
        System.out.println("So dien thoai: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Chuc vu: " + position.getDisplayName());
        System.out.println("Luong co ban: " + salary);
        System.out.println("Luong thuc te: " + calculateSalary());
        System.out.println("Ngay vao lam: " + hireDate);
        System.out.println("Trang thai: " + status);
        System.out.println("Ghi chu: " + note);
    }

    public void deactivateEmployee() {
        status = EmployeeStatus.INACTIVE;
    }

    public void activateEmployee() {
        status = EmployeeStatus.ACTIVE;
    }
    public boolean checkCustomerStatus(Customer customer) {
        return customer.getStatus() == Customer.CustomerStatus.ACTIVE;
    }

    //CRUD (lưu tạm) 
    public static boolean addEmployee(Employee employee) {
        if (findById(employee.getId()) != null) return false;
        employeeList.add(employee);
        return true;
    }

    public static Employee findById(String id) {
        for (Employee e : employeeList) {
            if (e.id.equals(id)) return e;
        }
        return null;
    }

    public static boolean deleteEmployee(String id) {
        Employee e = findById(id);
        if (e != null) {
            e.deactivateEmployee();
            return true;
        }
        return false;
    }

    public static void displayAllEmployees() {
        for (Employee e : employeeList) {
            e.displayInfo();
            System.out.println("----------------------");
        }
    }
}
