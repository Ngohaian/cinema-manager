package cinema.models;
import java.time.LocalDate;

public class Employee {
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
    public static Position fromDb(String dbValue) {
        switch (dbValue) {
            case "Nhan vien ban ve":
                return TICKET_SELLER;
            case "Quan ly":
                return MANAGER;
            case "Nhan vien check ve":
                return TICKET_CHECKER;
            default:
                return TICKET_SELLER;
        }
    }
    public String toDb() {
        return displayName;
    }
}

    public Employee() {};
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
    public String getNote() { return note; }
    public LocalDate getHireDate() { return hireDate; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setNote(String note) { this.note = note; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setId(String id) { this.id = id; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate;}
    public double calculateSalary() {return salary * position.getSalaryMultiplier();}

    public boolean checkWorkingStatus() {return status == EmployeeStatus.ACTIVE;}

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
    
}
