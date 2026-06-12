package cinema.dao;

import cinema.DBConnection;
import cinema.enums.SeatType;
import cinema.models.Seat;
import cinema.models.SeatLayout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {

    public static class SeatView {
        private int rowIndex;
        private int colIndex;
        private String seatLabel;
        private String seatType;
        private int active;

        public SeatView(int rowIndex, int colIndex, String seatLabel, String seatType, int active) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
            this.seatLabel = seatLabel;
            this.seatType = seatType;
            this.active = active;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColIndex() {
            return colIndex;
        }

        public String getSeatLabel() {
            return seatLabel;
        }

        public String getSeatType() {
            return seatType;
        }

        public int getActive() {
            return active;
        }
    }

    private Connection getConn() throws Exception {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            throw new Exception("Không kết nối được MySQL. Kiểm tra DBConnection, user, password, IP và MySQL Connector.");
        }

        return conn;
    }

    public List<Seat> getByRoom(String roomId) throws Exception {
        List<Seat> list = new ArrayList<>();

        String sql = "SELECT * FROM Seat WHERE roomId = ? AND active = 1 ORDER BY rowIndex, colIndex";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat s = new Seat(
                            rs.getInt("rowIndex"),
                            rs.getInt("colIndex"),
                            rs.getString("seatLabel"),
                            SeatType.valueOf(rs.getString("seatType"))
                    );

                    s.setActive(rs.getBoolean("active"));
                    list.add(s);
                }
            }
        }

        return list;
    }

    public List<SeatView> getSeatViewsByRoom(String roomId) throws Exception {
        List<SeatView> list = new ArrayList<>();

        String sql = "SELECT rowIndex, colIndex, seatLabel, seatType, active " +
                "FROM Seat " +
                "WHERE roomId = ? " +
                "ORDER BY rowIndex, colIndex";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SeatView seat = new SeatView(
                            rs.getInt("rowIndex"),
                            rs.getInt("colIndex"),
                            rs.getString("seatLabel"),
                            rs.getString("seatType"),
                            rs.getInt("active")
                    );

                    list.add(seat);
                }
            }
        }

        return list;
    }
    public int getSeatId(String roomId, int rowIndex, int colIndex) throws Exception {
        String sql = "SELECT seatId FROM Seat WHERE roomId = ? AND rowIndex = ? AND colIndex = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setInt(2, rowIndex);
            ps.setInt(3, colIndex);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seatId");
                } else {
                    throw new Exception("Không tìm thấy ghế với roomId: " + roomId + ", rowIndex: " + rowIndex + ", colIndex: " + colIndex);
                }
            }
        }
    }
    public void insert(String roomId, Seat s) throws Exception {
        String sql = "INSERT INTO Seat(roomId, rowIndex, colIndex, seatLabel, seatType, active) " +
                "VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setInt(2, s.getRowIndex());
            ps.setInt(3, s.getColIndex());
            ps.setString(4, s.getSeatLabel());
            ps.setString(5, s.getSeatType().name());

            ps.executeUpdate();
        }
    }

    public void insertByLayout(String roomId, SeatLayout layout) throws Exception {
        Seat[][] seats = layout.getSeats();

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);

            try {
                String sql = "INSERT INTO Seat(roomId, rowIndex, colIndex, seatLabel, seatType, active) " +
                        "VALUES (?, ?, ?, ?, ?, 1)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 0; i < seats.length; i++) {
                        for (int j = 0; j < seats[i].length; j++) {
                            Seat s = seats[i][j];

                            ps.setString(1, roomId);
                            ps.setInt(2, s.getRowIndex());
                            ps.setInt(3, s.getColIndex());
                            ps.setString(4, s.getSeatLabel());
                            ps.setString(5, s.getSeatType().name());
                            ps.addBatch();
                        }
                    }

                    ps.executeBatch();
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void insertOrUpdateSeats(Connection conn, String roomId, String[][] draftSeatTypes) throws Exception {
        String sql =
                "INSERT INTO Seat(roomId, rowIndex, colIndex, seatLabel, seatType, active) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "seatLabel = VALUES(seatLabel), " +
                "seatType = VALUES(seatType), " +
                "active = VALUES(active)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < draftSeatTypes.length; i++) {
                char rowChar = (char) ('A' + i);

                for (int j = 0; j < draftSeatTypes[i].length; j++) {
                    String viewType = draftSeatTypes[i][j];
                    String seatLabel = rowChar + String.valueOf(j + 1);

                    String dbSeatType;
                    int active;

                    if ("EMPTY".equalsIgnoreCase(viewType)) {
                        dbSeatType = "REGULAR";
                        active = 0;
                    } else {
                        dbSeatType = viewType;
                        active = 1;
                    }

                    ps.setString(1, roomId);
                    ps.setInt(2, i);
                    ps.setInt(3, j);
                    ps.setString(4, seatLabel);
                    ps.setString(5, dbSeatType);
                    ps.setInt(6, active);

                    ps.addBatch();
                }
            }

            ps.executeBatch();
        }
    }

    public void deactivateOutOfLayoutSeats(Connection conn, String roomId, int rows, int cols) throws Exception {
        String sql = "UPDATE Seat SET active = 0 WHERE roomId = ? AND (rowIndex >= ? OR colIndex >= ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.setInt(2, rows);
            ps.setInt(3, cols);

            ps.executeUpdate();
        }
    }

    public void deleteByRoom(String roomId) throws Exception {
        String sql = "DELETE FROM Seat WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }

    public void deleteByRoom(Connection conn, String roomId) throws Exception {
        String sql = "DELETE FROM Seat WHERE roomId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }
    public void updateSeatStatusToSold(Connection conn, int seatId) throws Exception {
    String sql = "UPDATE Seat SET status = 'Sold' WHERE seatId = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, seatId);
        ps.executeUpdate();
    }
    }
 

    }