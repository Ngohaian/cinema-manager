package cinema.dao;

import cinema.DBConnection;
import cinema.models.Room;
import java.sql.*;
import java.util.*;
public class RoomDAO {
    public List<Room> getAll() throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE active = 1";
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            Room r = new Room(
                rs.getString("roomId"),
                rs.getString("'name"),
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
        String sql = "SELECT * FROM Room WHERE roomId = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            Room r = new Room(
                rs.getString("roomId"),
                rs.getString("name"),
                rs.getInt("capacity"),
                rs.getString("type"),
                null  
            );
            r.setActive(rs.getBoolean("active"));
            return r;
        }
        return null;
    }

}