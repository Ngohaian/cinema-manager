package cinema.dao;

import cinema.DBConnection;
import cinema.enums.SeatType;
import cinema.models.Seat;
import cinema.models.SeatLayout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {

    public List<Seat> getByRoom(String roomId) throws Exception {
        List<Seat> list = new ArrayList<>();

        String sql = "SELECT * FROM Seat WHERE roomId = ? AND active = 1 ORDER BY rowIndex, colIndex";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ResultSet rs = ps.executeQuery();

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

        return list;
    }

    public void insert(String roomId, Seat s) throws Exception {
        String sql = "INSERT INTO Seat(roomId, rowIndex, colIndex, seatLabel, seatType, active) " +
                     "VALUES (?, ?, ?, ?, ?, 1)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setInt(2, s.getRowIndex());
            ps.setInt(3, s.getColIndex());
            ps.setString(4, s.getSeatLabel());
            ps.setString(5, s.getSeatType().name());

            ps.executeUpdate();
        }
    }

    public void deleteByRoom(String roomId) throws Exception {
        String sql = "DELETE FROM Seat WHERE roomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }

    public void insertByLayout(String roomId, SeatLayout layout) throws Exception {
        Seat[][] seats = layout.getSeats();

        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                insert(roomId, seats[i][j]);
            }
        }
    }
}