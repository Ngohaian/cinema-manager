package cinema.dao;

import cinema.DBConnection;
import cinema.models.Room;
import cinema.models.SeatLayout;
import java.sql.*;
import java.util.*;

public class RoomDAO {

    private SeatLayoutDAO seatLayoutDao = new SeatLayoutDAO();

    public List<Room> getAll() throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE active = 1";
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            Room r = new Room(
                rs.getString("roomId"),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("type"),
                null
            );
            r.setActive(rs.getBoolean("active"));
            list.add(r);
        }
        return list;
    }

    public Room getById(String id) throws Exception {
        Room r = null;
        String sql = "SELECT * FROM Room WHERE roomId = ?";
        
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
            	SeatLayout layout = seatLayoutDao.getByRoomId(id);
                r = new Room(
                    rs.getString("roomId"),
                    rs.getString("name"),
                    rs.getInt("capacity"),
                    rs.getString("type"),
                    layout
                );
                r.setActive(rs.getBoolean("active"));
            }
        } catch(Exception ex) {
            System.out.println("Loi: " + ex);
        }
        return r;
    }
}