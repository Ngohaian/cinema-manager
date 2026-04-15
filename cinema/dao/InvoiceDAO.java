package cinema.dao;

import cinema.models.Invoice;
import cinema.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class InvoiceDAO {

    // =========================
    // 1. Lấy tất cả hóa đơn
    // =========================
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

                Timestamp ts = rs.getTimestamp("invoiceDate");
                if (ts != null) {
                    inv.setInvoiceDate(new Date(ts.getTime()));
                }

                inv.setTotalAmount(rs.getDouble("totalAmount"));

                list.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // 2. Tìm theo ID
    // =========================
    public Invoice findById(String id) {
        String sql = "SELECT * FROM Invoice WHERE invoiceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getString("invoiceId"));
                inv.setCustomerId(rs.getString("customerId"));

                Timestamp ts = rs.getTimestamp("invoiceDate");
                if (ts != null) {
                    inv.setInvoiceDate(new Date(ts.getTime()));
                }

                inv.setTotalAmount(rs.getDouble("totalAmount"));

                return inv;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // 3. Thêm hóa đơn
    // =========================
    public boolean insert(Invoice inv) {
        String sql = "INSERT INTO Invoice(invoiceId, customerId, invoiceDate, totalAmount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getInvoiceId());
            ps.setString(2, inv.getCustomerId());

            if (inv.getInvoiceDate() != null) {
                ps.setTimestamp(3, new Timestamp(inv.getInvoiceDate().getTime()));
            } else {
                ps.setTimestamp(3, null);
            }

            ps.setDouble(4, inv.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 4. Cập nhật hóa đơn
    // =========================
    public boolean update(Invoice inv) {
        String sql = "UPDATE Invoice SET customerId=?, invoiceDate=?, totalAmount=? WHERE invoiceId=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getCustomerId());

            if (inv.getInvoiceDate() != null) {
                ps.setTimestamp(2, new Timestamp(inv.getInvoiceDate().getTime()));
            } else {
                ps.setTimestamp(2, null);
            }

            ps.setDouble(3, inv.getTotalAmount());
            ps.setString(4, inv.getInvoiceId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // 5. Xóa hóa đơn
    // =========================
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
}