package cinema.dao;

import cinema.DBConnection;
import cinema.models.Room;
import cinema.models.Seat;
import cinema.models.SeatLayout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAll() throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT r.roomId, r.name, r.capacity, r.type, r.active, "
                + "sl.numberOfRows, sl.seatsPerRow, sl.vipStartRow, sl.vipEndRow, sl.coupleRow "
                + "FROM Room r "
                + "LEFT JOIN SeatLayout sl ON r.roomId = sl.roomId "
                + "WHERE r.active = 1 "
                + "ORDER BY r.roomId";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRoom(rs));
            }
        }
        return list;
    }

    public List<Room> search(String keyword, String type) throws Exception {
        List<Room> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.roomId, r.name, r.capacity, r.type, r.active, ");
        sql.append("sl.numberOfRows, sl.seatsPerRow, sl.vipStartRow, sl.vipEndRow, sl.coupleRow ");
        sql.append("FROM Room r ");
        sql.append("LEFT JOIN SeatLayout sl ON r.roomId = sl.roomId ");
        sql.append("WHERE r.active = 1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(r.roomId) LIKE ? OR LOWER(r.name) LIKE ?) ");
            String key = "%" + keyword.trim().toLowerCase() + "%";
            params.add(key);
            params.add(key);
        }

        if (type != null && !type.equalsIgnoreCase("Tất cả")) {
            sql.append("AND LOWER(r.type) = ? ");
            params.add(type.trim().toLowerCase());
        }

        sql.append("ORDER BY r.roomId");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRoom(rs));
                }
            }
        }
        return list;
    }

    public Room getById(String id) throws Exception {
        String sql = "SELECT r.roomId, r.name, r.capacity, r.type, r.active, "
                + "sl.numberOfRows, sl.seatsPerRow, sl.vipStartRow, sl.vipEndRow, sl.coupleRow "
                + "FROM Room r "
                + "LEFT JOIN SeatLayout sl ON r.roomId = sl.roomId "
                + "WHERE r.roomId = ? AND r.active = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        }
        return null;
    }

    public void insert(Room room) throws Exception {
        String insertRoom = "INSERT INTO Room(roomId, name, capacity, type, active) VALUES (?, ?, ?, ?, 1)";
        String insertLayout = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String insertSeat = "INSERT INTO Seat(roomId, rowIndex, colIndex, seatLabel, seatType, active) VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(insertRoom)) {
                    ps.setString(1, room.getRoomId());
                    ps.setString(2, room.getName());
                    ps.setInt(3, room.getCapacity());
                    ps.setString(4, room.getType());
                    ps.executeUpdate();
                }

                SeatLayout layout = room.getSeatLayout();
                if (layout != null) {
                    try (PreparedStatement ps = conn.prepareStatement(insertLayout)) {
                        ps.setString(1, room.getRoomId());
                        ps.setInt(2, layout.getNumberOfRows());
                        ps.setInt(3, layout.getSeatsPerRow());
                        ps.setInt(4, layout.getVipStartRow());
                        ps.setInt(5, layout.getVipEndRow());
                        ps.setInt(6, layout.getCoupleRow());
                        ps.executeUpdate();
                    }

                    Seat[][] seats = layout.getSeats();
                    try (PreparedStatement ps = conn.prepareStatement(insertSeat)) {
                        for (Seat[] row : seats) {
                            for (Seat seat : row) {
                                ps.setString(1, room.getRoomId());
                                ps.setInt(2, seat.getRowIndex());
                                ps.setInt(3, seat.getColIndex());
                                ps.setString(4, seat.getSeatLabel());
                                ps.setString(5, seat.getSeatType().name());
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
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

    public void update(Room room) throws Exception {
        String updateRoom = "UPDATE Room SET name = ?, capacity = ?, type = ? WHERE roomId = ?";
        String updateLayout = "UPDATE SeatLayout SET numberOfRows = ?, seatsPerRow = ?, vipStartRow = ?, vipEndRow = ?, coupleRow = ? WHERE roomId = ?";
        String insertLayout = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(updateRoom)) {
                    ps.setString(1, room.getName());
                    ps.setInt(2, room.getCapacity());
                    ps.setString(3, room.getType());
                    ps.setString(4, room.getRoomId());
                    ps.executeUpdate();
                }

                SeatLayout layout = room.getSeatLayout();
                if (layout != null) {
                    int affected;
                    try (PreparedStatement ps = conn.prepareStatement(updateLayout)) {
                        ps.setInt(1, layout.getNumberOfRows());
                        ps.setInt(2, layout.getSeatsPerRow());
                        ps.setInt(3, layout.getVipStartRow());
                        ps.setInt(4, layout.getVipEndRow());
                        ps.setInt(5, layout.getCoupleRow());
                        ps.setString(6, room.getRoomId());
                        affected = ps.executeUpdate();
                    }

                    if (affected == 0) {
                        try (PreparedStatement ps = conn.prepareStatement(insertLayout)) {
                            ps.setString(1, room.getRoomId());
                            ps.setInt(2, layout.getNumberOfRows());
                            ps.setInt(3, layout.getSeatsPerRow());
                            ps.setInt(4, layout.getVipStartRow());
                            ps.setInt(5, layout.getVipEndRow());
                            ps.setInt(6, layout.getCoupleRow());
                            ps.executeUpdate();
                        }
                    }
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

    public void delete(String roomId) throws Exception {
        String deleteRoom = "UPDATE Room SET active = 0 WHERE roomId = ?";
        String deleteSeats = "UPDATE Seat SET active = 0 WHERE roomId = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(deleteSeats)) {
                    ps.setString(1, roomId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteRoom)) {
                    ps.setString(1, roomId);
                    ps.executeUpdate();
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

    public boolean existsRoomId(String roomId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Room WHERE roomId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        SeatLayout layout = null;
        int numberOfRows = rs.getInt("numberOfRows");
        if (!rs.wasNull()) {
            layout = new SeatLayout(
                    numberOfRows,
                    rs.getInt("seatsPerRow"),
                    rs.getInt("vipStartRow"),
                    rs.getInt("vipEndRow"),
                    rs.getInt("coupleRow")
            );
        }

        Room room = new Room(
                rs.getString("roomId"),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("type"),
                layout
        );
        room.setActive(rs.getBoolean("active"));
        return room;
    }
}
