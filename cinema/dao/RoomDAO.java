package cinema.dao;

import cinema.DBConnection;
import cinema.models.Room;
import cinema.models.SeatLayout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private SeatLayoutDAO seatLayoutDAO = new SeatLayoutDAO();

    public List<Room> getAll() throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE active = 1 ORDER BY roomId";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String roomId = rs.getString("roomId");
                SeatLayout layout = seatLayoutDAO.getByRoomId(roomId);

                Room r = new Room(
                        roomId,
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getString("type"),
                        layout
                );

                r.setActive(rs.getBoolean("active"));
                list.add(r);
            }
        }

        return list;
    }

    public List<Room> search(String keyword) throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room " +
                "WHERE active = 1 AND (roomId LIKE ? OR name LIKE ? OR type LIKE ?) " +
                "ORDER BY roomId";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String roomId = rs.getString("roomId");
                SeatLayout layout = seatLayoutDAO.getByRoomId(roomId);

                Room r = new Room(
                        roomId,
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getString("type"),
                        layout
                );

                r.setActive(rs.getBoolean("active"));
                list.add(r);
            }
        }

        return list;
    }

    public Room getById(String id) throws Exception {
        String sql = "SELECT * FROM Room WHERE roomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                SeatLayout layout = seatLayoutDAO.getByRoomId(id);

                Room r = new Room(
                        rs.getString("roomId"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getString("type"),
                        layout
                );

                r.setActive(rs.getBoolean("active"));
                return r;
            }
        }

        return null;
    }

    public void insert(Room r) throws Exception {
        String sql = "INSERT INTO Room(roomId, name, capacity, type, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getRoomId());
            ps.setString(2, r.getName());
            ps.setInt(3, r.getCapacity());
            ps.setString(4, r.getType());
            ps.setBoolean(5, r.isActive());

            ps.executeUpdate();
        }
    }

    public void update(Room r) throws Exception {
        String sql = "UPDATE Room SET name = ?, capacity = ?, type = ?, active = ? WHERE roomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getName());
            ps.setInt(2, r.getCapacity());
            ps.setString(3, r.getType());
            ps.setBoolean(4, r.isActive());
            ps.setString(5, r.getRoomId());

            ps.executeUpdate();
        }
    }

    public void delete(String roomId) throws Exception {
        String sql = "UPDATE Room SET active = 0 WHERE roomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }
}