package cinema.dao;

import cinema.DBConnection;
import cinema.models.Invoice;
import cinema.models.Seat;
import cinema.models.Ticket;
import cinema.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    // ================= LẤY DANH SÁCH HÓA ĐƠN =================

    public List<Invoice> getAll() {

        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Invoice inv = new Invoice();
                inv.setInvoiceId(rs.getString("invoiceId"));
                inv.setCustomerId(rs.getString("customerId"));
                inv.setInvoiceDate(rs.getTimestamp("invoiceDate").toLocalDateTime());
                inv.setTotalAmount(rs.getDouble("totalAmount"));
                inv.setEmployeeId(rs.getString("EmployeeId"));
                inv.setStatus(rs.getString("status"));
                list.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= THÊM VÉ =================

    public void insertTicket(Ticket ticket) {

        String sql = """
            INSERT INTO Ticket
            (ticketId, price, seatId, showtimeId, status, invoiceId)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        SeatDao seatDao = new SeatDao();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, ticket.getTicketId());

            ps.setDouble(2, ticket.getPrice());

            Seat s = ticket.getSeat();

            int seatId = seatDao.getSeatId(
                    ticket.getShowtime().getRoom().getRoomId(),
                    s.getRowIndex(),
                    s.getColIndex()
            );

            ps.setInt(3, seatId);

            ps.setString(4, ticket.getShowtime().getShowtimeId());

            ps.setString(5, ticket.getStatus().name());

            ps.setString(6, ticket.getInvoiceId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= THÊM HÓA ĐƠN =================

    public boolean insert(Invoice inv) {

        String sql = """
            INSERT INTO Invoice
            (
                invoiceId,
                customerId,
                employeeId,
                invoiceDate,
                totalAmount,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, inv.getInvoiceId());

            ps.setString(2, inv.getCustomerId());

            ps.setString(3, inv.getEmployeeId());

            ps.setTimestamp(
                    4,
                    Timestamp.valueOf(inv.getInvoiceDate())
            );

            ps.setDouble(5, inv.getTotalAmount());

            // trạng thái mặc định
            ps.setString(6, "Đã thanh toán");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= TẠO MÃ VÉ =================

    public String getNextTicketIdByShowtimeId(String showtimeId) {

        String sql = """
            SELECT ticketId
            FROM Ticket
            WHERE showtimeId = ?
            ORDER BY ticketId DESC
            LIMIT 1
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, showtimeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String lastId = rs.getString("ticketId");

                int lastDash = lastId.lastIndexOf("-");

                int number = Integer.parseInt(
                        lastId.substring(lastDash + 1)
                );

                return showtimeId + "-"
                        + String.format("%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return showtimeId + "-001";
    }

    // ================= TẠO MÃ HÓA ĐƠN =================

    public String getNextInvoiceID() {

        String sql = """
            SELECT invoiceId
            FROM Invoice
            ORDER BY invoiceId DESC
            LIMIT 1
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                String lastId = rs.getString("invoiceId");

                int number = Integer.parseInt(
                        lastId.substring(3)
                );

                return String.format("INV%03d", number + 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "INV001";
    }

    // ================= XÓA HÓA ĐƠN =================

    public boolean delete(String id) {

        String sql = """
            DELETE FROM Invoice
            WHERE invoiceId = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= UPDATE HÓA ĐƠN =================

    public boolean update(Invoice inv) {

        String sql = """
            UPDATE Invoice
            SET customerId = ?,
                totalAmount = ?
            WHERE invoiceId = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, inv.getCustomerId());

            ps.setDouble(2, inv.getTotalAmount());

            ps.setString(3, inv.getInvoiceId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= CHI TIẾT VÉ =================

    public List<Ticket> getTicketsByInvoiceId(String invoiceId) {

        List<Ticket> list = new ArrayList<>();

        String sql = """
            SELECT
                t.ticketId,
                t.price,
                t.status,
                s.rowIndex,
                s.colIndex,
                s.seatType
            FROM Ticket t
            JOIN Seat s
                ON t.seatId = s.seatId
            WHERE t.invoiceId = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, invoiceId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Ticket t = new Ticket();

                t.setTicketId(
                        rs.getString("ticketId")
                );

                t.setPrice(
                        rs.getDouble("price")
                );

                t.setInvoiceId(invoiceId);

                t.setStatus(
                        cinema.enums.TicketStatus.valueOf(
                                rs.getString("status")
                        )
                );

                Seat seat = new Seat();

                seat.setRowIndex(
                        rs.getInt("rowIndex")
                );

                seat.setColIndex(
                        rs.getInt("colIndex")
                );

                t.setSeat(seat);

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= HỦY HÓA ĐƠN =================

    public boolean cancelInvoiceAndTickets(String invoiceId) {

        String sqlTicket = "UPDATE Ticket SET status = 'Canceled', invoiceID =NULL  WHERE invoiceId = ?";

        String sqlInvoice = """
            UPDATE Invoice
            SET status = 'Đã hủy'
            WHERE invoiceId = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement ps1 = conn.prepareStatement(sqlTicket);
                    PreparedStatement ps2 = conn.prepareStatement(sqlInvoice)
            ) {

                ps1.setString(1, invoiceId);
                ps1.executeUpdate();

                ps2.setString(1, invoiceId);
                ps2.executeUpdate();

                conn.commit();

                return true;

            } catch (Exception e) {

                conn.rollback();

                e.printStackTrace();

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // ================= DOANH THU HÔM NAY =================

    public double getTodayRevenue() {

        String sql = """
            SELECT IFNULL(SUM(totalAmount), 0) AS revenue
            FROM Invoice
            WHERE DATE(invoiceDate) = CURDATE()
            AND status = 'Đã thanh toán'
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getDouble("revenue");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= TỔNG VÉ BÁN HÔM NAY =================

    public int getTodayTicketSold() {

        String sql = """
            SELECT COUNT(*) AS total
            FROM Ticket t
            JOIN Invoice i
                ON t.invoiceId = i.invoiceId
            WHERE DATE(i.invoiceDate) = CURDATE()
            AND t.status IN ('Sold', 'Used')
            AND i.status = 'Đã thanh toán'
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= DOANH THU THEO NGÀY =================

    public double getRevenueByDay(String date) {

        String sql = """
            SELECT IFNULL(SUM(totalAmount), 0) AS revenue
            FROM Invoice
            WHERE DATE(invoiceDate) = ?
            AND status = 'Đã thanh toán'
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, date);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("revenue");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= DOANH THU THEO THÁNG =================

    public double getRevenueByMonth(int month, int year) {

        String sql = """
            SELECT IFNULL(SUM(totalAmount), 0) AS revenue
            FROM Invoice
            WHERE MONTH(invoiceDate) = ?
            AND YEAR(invoiceDate) = ?
            AND status = 'Đã thanh toán'
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("revenue");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= DOANH THU THEO NĂM =================

    public double getRevenueByYear(int year) {

        String sql = """
            SELECT IFNULL(SUM(totalAmount), 0) AS revenue
            FROM Invoice
            WHERE YEAR(invoiceDate) = ?
            AND status = 'Đã thanh toán'
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, year);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("revenue");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

}