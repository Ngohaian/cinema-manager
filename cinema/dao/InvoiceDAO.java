package cinema.dao;

import cinema.models.Invoice;
import cinema.models.Seat;
import cinema.models.Ticket;
import cinema.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                inv.setInvoiceDate(rs.getTimestamp("invoiceDate").toLocalDateTime());
                inv.setTotalAmount(rs.getDouble("totalAmount"));

                list.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void insertTicket(Ticket ticket) {
        String sql = "INSERT INTO Ticket (ticketId, price, seatId, showtimeId, status, invoiceId) VALUES(?, ?, ?,?,?,?)";
        SeatDao seatDao = new SeatDao();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ticket.getTicketId());
            ps.setDouble(2, ticket.getPrice());
            Seat s = ticket.getSeat();
            int seatId = seatDao.getSeatId(ticket.getShowtime().getRoom().getRoomId(), s.getRowIndex(), s.getColIndex());
            ps.setInt(3, seatId);
            ps.setString(4, ticket.getShowtime().getShowtimeId());
            ps.setString(5, ticket.getStatus().name());
            ps.setString(6, ticket.getInvoiceId());    
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Thêm hóa đơn
    public boolean insert(Invoice inv) {
        String sql = "INSERT INTO Invoice(invoiceId, customerId, employeeId, invoiceDate, totalAmount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, inv.getInvoiceId());
            ps.setString(2, inv.getCustomerId());
            ps.setString(3, inv.getEmployeeId());
            ps.setTimestamp(4, Timestamp.valueOf(inv.getInvoiceDate()));
            ps.setDouble(5, inv.getTotalAmount());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getNextTicketIdByShowtimeId(String showtimeId) {
        String sql = "SELECT ticketId FROM Ticket WHERE showtimeId = ? ORDER BY ticketId DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, showtimeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("ticketId");
                int lastDash = lastId.lastIndexOf("-");
                int number = Integer.parseInt(lastId.substring(lastDash + 1)); // ← lấy số sau dấu - cuối
                return showtimeId + "-" + String.format("%03d", number + 1);  // ← format đúng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showtimeId + "-001";
    }
    public String getNextInvoiceID() {
        String sql = "SELECT invoiceId FROM invoice ORDER BY invoiceId DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("invoiceId");
                int number = Integer.parseInt(lastId.substring(3)); 
                return String.format("INV%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "INV001";
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