package cinema.dao;

import cinema.DBConnection;
import cinema.models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM Employee";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();

                e.setId(rs.getString("EmployeeId"));
                e.setName(rs.getString("EmployeeName"));
                e.setPhone(rs.getString("EmployeePhone"));
                e.setEmail(rs.getString("EmployeeEmail"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));

                e.changePosition(Employee.Position.fromDb(rs.getString("Position")));

                e.setSalary(rs.getDouble("salary"));
                e.setHireDate(rs.getDate("hireDate").toLocalDate());

                if ("ACTIVE".equals(rs.getString("status"))) {
                    e.activateEmployee();
                } else {
                    e.deactivateEmployee();
                }

                e.setNote(rs.getString("note"));

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean addEmployee(Employee e) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, e.getId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getPhone());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getPosition().toDb());
            ps.setDouble(6, e.getSalary());
            ps.setDate(7, Date.valueOf(e.getHireDate()));
            ps.setString(8, e.getUsername());
            ps.setString(9, e.getPassword());
            ps.setString(10, e.getStatus().name());
            ps.setString(11, e.getNote());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(Employee e) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE Employee SET EmployeeName=?, EmployeePhone=?, EmployeeEmail=?, Position=?, salary=?, note=? WHERE EmployeeId=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, e.getName());
            ps.setString(2, e.getPhone());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPosition().toDb()); 
            ps.setDouble(5, e.getSalary());
            ps.setString(6, e.getNote());
            ps.setString(7, e.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(String id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE Employee SET status='INACTIVE' WHERE EmployeeId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ================= FIND BY ID =================
    public Employee findById(String id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM Employee WHERE EmployeeId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = new Employee();

                e.setId(rs.getString("EmployeeId"));
                e.setName(rs.getString("EmployeeName"));
                e.setPhone(rs.getString("EmployeePhone"));
                e.setEmail(rs.getString("EmployeeEmail"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));

                e.changePosition(Employee.Position.fromDb(rs.getString("Position")));
                e.setSalary(rs.getDouble("salary"));
                e.setHireDate(rs.getDate("hireDate").toLocalDate());

                if ("ACTIVE".equals(rs.getString("status"))) {
                    e.activateEmployee();
                } else {
                    e.deactivateEmployee();
                }

                e.setNote(rs.getString("note"));

                return e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //LOGIN 
    public Employee login(String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM Employee WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = new Employee();

                e.setId(rs.getString("EmployeeId"));
                e.setName(rs.getString("EmployeeName"));
                e.setUsername(rs.getString("username"));
                e.setPassword(rs.getString("password"));

                return e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}