package cinema.managers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer{
    private static final List<Customer> customerList = new ArrayList<>();

    private static int autoId = 1;

    private static final double discount_Standard = 0.03;
    private static final double discount_Gold =0.05;
    private static final double discount_Diamond = 0.07;

    private static final double GOLD_THRESHOLD = 2000000;
    private static final double DIAMOND_THRESHOLD = 4000000;

    public enum CustomerStatus{
        ACTIVE, INACTIVE;
    }

    public enum CustomerType{
        STANDARD(discount_Standard, "Beta Standard"),
        GOLD(discount_Gold, "Beta Gold"),
        DIAMOND(discount_Diamond, "Beta Diamond");

        private final double cashbackRate;
        private final String display_Name;

        CustomerType(double cashbackRate, String displayName){
            this.cashbackRate = cashbackRate;
            this.display_Name = displayName;
        }
        
        public double getCashbackRate(){return cashbackRate;}
        public String getDisplayName(){return display_Name;}
    }
    private String id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String email;
    private CustomerType type;
    private double loyaltyPoints;
    private double totalSpent;
    private LocalDate createdDate;
    private CustomerStatus status;
    private String note;

    public Customer(String name, String phone, String email, String username, String password ){
        this.id = String.format("CUS%03d", autoId++);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.type = CustomerType.STANDARD;
        this.loyaltyPoints = 0;
        this.totalSpent = 0;
        this.createdDate = LocalDate.now();
        this.status = CustomerStatus.ACTIVE;
        this.note = "";
    }

    public String getId(){return id;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    public String getEmail(){return email;}
    public CustomerType getType(){return type;}
    public double getLoyaltyPoints() {return loyaltyPoints;}
    public CustomerStatus getStatus() { return status; }
    public double getTotalSpent(){return totalSpent;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}

    public void setName(String Name){name = Name;}
    public void setPhone(String Phone){phone = Phone;}
    public void setEmail(String Email){email = Email;}
    public void setNote(String note) { this.note = note;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    
    private void updateCustomerType(){
        if(totalSpent >= DIAMOND_THRESHOLD) type = CustomerType.DIAMOND;
        else if(totalSpent >= GOLD_THRESHOLD) type = CustomerType.GOLD;
        else type = CustomerType.STANDARD;
    }

    public void makePurchase (double amount){
        if(status == CustomerStatus.INACTIVE){
            System.out.println("Khach hang da bi khoa hoac chua ton tai!");
            return;
        }
        if(amount >0){
            totalSpent += amount;
            double cashback = amount *type.getCashbackRate();
            loyaltyPoints += cashback;
            updateCustomerType();

        }
    }

    public boolean usePoints(double amount){
        if(amount >0 && amount <= loyaltyPoints){
            loyaltyPoints -= amount;
            return true;
        }
        return false;
    }

    public void deactivate_Customer(){
        status = CustomerStatus.INACTIVE;
    }
    public void active_Customer(){
        status = CustomerStatus.ACTIVE;
    }
    public void displayInfo() {
            System.out.println("===== THONG TIN KHACH HANG =====");
            System.out.println("Ma KH: " + id);
            System.out.println("Ten: " + name);
            System.out.println("Hang the: " + type.getDisplayName());
            System.out.println("Tong chi tieu: " + totalSpent + " VND");
            System.out.println("Cashback hien co: " + loyaltyPoints + " VND");
            System.out.println("Trang thai: " + status);
    }
    //CRUD. tam luu 
    public static boolean addCustomer(Customer customer){
    if(findById(customer.getId()) != null){
        return false; 
    }
    customerList.add(customer);
    return true;
}
    public static Customer findById(String id){
        for(Customer c: customerList){
            if(c.id.equals(id)) return c;
        }
        return null;
    }
    public static boolean deleteCustomer(String id){
        Customer c = findById(id);
        if(c != null){
            c.deactivate_Customer();
            return true;
        }
        return false;
    }

    public static void displayAllCustomers() {
        for (Customer c : customerList) {
            c.displayInfo();
            System.out.println("----------------------");
        }
    }
    public static boolean updateCustomer(String id, String newName, String newPhone, String newEmail){
        Customer c = findById(id);
        if(c != null){
            c.setName(newName);
            c.setPhone(newPhone);
            c.setEmail(newEmail);
            return true;
        }
        return false;
    }

}
