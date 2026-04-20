package cinema.dao;

import cinema.DBConnection;
import cinema.models.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public boolean addCustomer(Customer c) {
    try {
        Connection conn = DBConnection.getConnection();

        String sql = "INSERT INTO customer VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, c.getId());
        ps.setString(2, c.getName());
        ps.setString(3, c.getPhone());
        ps.setString(4, c.getEmail());
        ps.setString(5, "Beta Standard"); // tạm
        ps.setFloat(6, 0);
        ps.setDouble(7, 0);
        ps.setDate(8, java.sql.Date.valueOf("2026-01-01"));
        ps.setString(9, "ACTIVE");
        ps.setString(10, "");

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

   public boolean deleteCustomer(String id) {
    try {
        Connection conn = DBConnection.getConnection();

        String sql = "UPDATE customer SET status='INACTIVE' WHERE CustomerId=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, id);

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
    }

    public boolean updateCustomer(Customer c) {
    try {
        Connection conn = DBConnection.getConnection();

        String sql = "UPDATE customer SET CustomerName=?, CustomerPhone=?, CustomerEmail=? WHERE CustomerId=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, c.getName());
        ps.setString(2, c.getPhone());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getId());
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
        return false;
    }
    public List<Customer> getAllCustomers() {
    List<Customer> list = new ArrayList<>();
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer c = new Customer("", "", ""); 
            c.setId(rs.getString("CustomerId"));
            c.setName(rs.getString("CustomerName"));
            c.setPhone(rs.getString("CustomerPhone"));
            c.setEmail(rs.getString("CustomerEmail"));
            list.add(c);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
}


