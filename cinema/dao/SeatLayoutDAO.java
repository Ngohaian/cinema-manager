package cinema.dao;

import cinema.DBConnection;
import cinema.models.SeatLayout;

import java.lang.reflect.Method;
import java.sql.*;

public class SeatLayoutDAO {

    private Connection getConn() throws Exception {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            throw new Exception("Không kết nối được MySQL. Kiểm tra DBConnection, user, password, IP và MySQL Connector.");
        }

        return conn;
    }

    public SeatLayout getByRoomId(String roomId) throws Exception {
        String sql = "SELECT * FROM SeatLayout WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SeatLayout(
                            rs.getInt("numberOfRows"),
                            rs.getInt("seatsPerRow"),
                            rs.getInt("vipStartRow"),
                            rs.getInt("vipEndRow"),
                            rs.getInt("coupleRow")
                    );
                }
            }
        }

        return null;
    }

    public void insert(String roomId, SeatLayout layout) throws Exception {
        int vipStartRow = getIntValue(layout, "getVipStartRow", -1);
        int vipEndRow = getIntValue(layout, "getVipEndRow", -1);
        int coupleRow = getIntValue(layout, "getCoupleRow", -1);

        insert(
                roomId,
                layout.getNumberOfRows(),
                layout.getSeatsPerRow(),
                vipStartRow,
                vipEndRow,
                coupleRow
        );
    }

    public void insert(String roomId, int rows, int cols,
                       int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setInt(2, rows);
            ps.setInt(3, cols);
            ps.setInt(4, vipStartRow);
            ps.setInt(5, vipEndRow);
            ps.setInt(6, coupleRow);

            ps.executeUpdate();
        }
    }

    public void insert(Connection conn, String roomId, int rows, int cols,
                       int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.setInt(2, rows);
            ps.setInt(3, cols);
            ps.setInt(4, vipStartRow);
            ps.setInt(5, vipEndRow);
            ps.setInt(6, coupleRow);

            ps.executeUpdate();
        }
    }

    public void update(String roomId, SeatLayout layout) throws Exception {
        int vipStartRow = getIntValue(layout, "getVipStartRow", -1);
        int vipEndRow = getIntValue(layout, "getVipEndRow", -1);
        int coupleRow = getIntValue(layout, "getCoupleRow", -1);

        update(
                roomId,
                layout.getNumberOfRows(),
                layout.getSeatsPerRow(),
                vipStartRow,
                vipEndRow,
                coupleRow
        );
    }

    public void update(String roomId, int rows, int cols,
                       int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "UPDATE SeatLayout " +
                "SET numberOfRows = ?, seatsPerRow = ?, vipStartRow = ?, vipEndRow = ?, coupleRow = ? " +
                "WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rows);
            ps.setInt(2, cols);
            ps.setInt(3, vipStartRow);
            ps.setInt(4, vipEndRow);
            ps.setInt(5, coupleRow);
            ps.setString(6, roomId);

            ps.executeUpdate();
        }
    }

    public void update(Connection conn, String roomId, int rows, int cols,
                       int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "UPDATE SeatLayout " +
                "SET numberOfRows = ?, seatsPerRow = ?, vipStartRow = ?, vipEndRow = ?, coupleRow = ? " +
                "WHERE roomId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rows);
            ps.setInt(2, cols);
            ps.setInt(3, vipStartRow);
            ps.setInt(4, vipEndRow);
            ps.setInt(5, coupleRow);
            ps.setString(6, roomId);

            ps.executeUpdate();
        }
    }

    public void insertOrUpdate(String roomId, SeatLayout layout) throws Exception {
        int vipStartRow = getIntValue(layout, "getVipStartRow", -1);
        int vipEndRow = getIntValue(layout, "getVipEndRow", -1);
        int coupleRow = getIntValue(layout, "getCoupleRow", -1);

        insertOrUpdate(
                roomId,
                layout.getNumberOfRows(),
                layout.getSeatsPerRow(),
                vipStartRow,
                vipEndRow,
                coupleRow
        );
    }

    public void insertOrUpdate(String roomId, int rows, int cols,
                               int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "numberOfRows = VALUES(numberOfRows), " +
                "seatsPerRow = VALUES(seatsPerRow), " +
                "vipStartRow = VALUES(vipStartRow), " +
                "vipEndRow = VALUES(vipEndRow), " +
                "coupleRow = VALUES(coupleRow)";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setInt(2, rows);
            ps.setInt(3, cols);
            ps.setInt(4, vipStartRow);
            ps.setInt(5, vipEndRow);
            ps.setInt(6, coupleRow);

            ps.executeUpdate();
        }
    }

    public void insertOrUpdate(Connection conn, String roomId, int rows, int cols,
                               int vipStartRow, int vipEndRow, int coupleRow) throws Exception {

        String sql = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "numberOfRows = VALUES(numberOfRows), " +
                "seatsPerRow = VALUES(seatsPerRow), " +
                "vipStartRow = VALUES(vipStartRow), " +
                "vipEndRow = VALUES(vipEndRow), " +
                "coupleRow = VALUES(coupleRow)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.setInt(2, rows);
            ps.setInt(3, cols);
            ps.setInt(4, vipStartRow);
            ps.setInt(5, vipEndRow);
            ps.setInt(6, coupleRow);

            ps.executeUpdate();
        }
    }

    public void delete(String roomId) throws Exception {
        String sql = "DELETE FROM SeatLayout WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, String roomId) throws Exception {
        String sql = "DELETE FROM SeatLayout WHERE roomId = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }

    private int getIntValue(SeatLayout layout, String methodName, int defaultValue) {
        try {
            Method method = layout.getClass().getMethod(methodName);
            Object value = method.invoke(layout);

            if (value instanceof Integer) {
                return (Integer) value;
            }

            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}