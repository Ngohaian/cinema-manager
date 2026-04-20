package cinema.dao;

import cinema.models.Invoice;
import cinema.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class InvoiceDAO {

    // Lấy tất cả hóa đơn
    public List<Invoice> getAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getString("invoiceId"));
                inv.setCustomerId(rs.getString("customerId"));
                //inv.setInvoiceDate(rs.getTimestamp("invoiceDate"));
                //inv.setTotalAmount(rs.getDouble("totalAmount"));

                list.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm hóa đơn
    public boolean insert(Invoice inv) {
        String sql = "INSERT INTO Invoice(invoiceId, customerId, invoiceDate, totalAmount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getInvoiceId());
            ps.setString(2, inv.getCustomerId());
            //ps.setTimestamp(3, new Timestamp(inv.getInvoiceDate().getTime()));
            ps.setDouble(4, inv.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa
    public boolean delete(String id) {
        String sql = "DELETE FROM Invoice WHERE invoiceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật
    public boolean update(Invoice inv) {
        String sql = "UPDATE Invoice SET customerId=?, totalAmount=? WHERE invoiceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getCustomerId());
            ps.setDouble(2, inv.getTotalAmount());
            ps.setString(3, inv.getInvoiceId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}