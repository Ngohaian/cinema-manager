package cinema.managers;

import java.util.ArrayList;
import java.util.List;
import cinema.models.Customer;

public class CustomerManager {
    private List<Customer> customerList = new ArrayList<>();

    public boolean addCustomer(Customer customer){
        if(findById(customer.getId()) != null){
            return false;
        }
        customerList.add(customer);
        return true;
    }
    public Customer findById(String id){
        for(Customer c : customerList){
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
    public boolean deleteCustomer(String id){
        Customer c = findById(id);
        if(c != null){
            c.deactivate_Customer();
            return true;
        }
        return false;
    }
    public boolean updateCustomer(String id,String name,String phone,String email){
        Customer c = findById(id);
        if(c != null){
            c.setName(name);
            c.setPhone(phone);
            c.setEmail(email);
            return true;
        }
        return false;
    }
    public void displayAllCustomers(){
        for(Customer c : customerList){
            c.displayInfo();
            System.out.println("----------------");
        }
    }
}
