package cinema.dao;

import cinema.DBConnection;
import cinema.models.SeatLayout;
import java.sql.*;

public class SeatLayoutDAO {

    public SeatLayout getByRoomId(String roomId) throws Exception {
        SeatLayout layout = null;
        String sql = "SELECT * FROM SeatLayout WHERE roomId = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                layout = new SeatLayout(
                    rs.getInt("numberOfRows"),
                    rs.getInt("seatsPerRow"),
                    rs.getInt("vipStartRow"),
                    rs.getInt("vipEndRow"),
                    rs.getInt("coupleRow")
                );
            }
        } catch(Exception ex) {
            System.out.println("Loi lay SeatLayout: " + ex);
            throw ex;
        }

        return layout;
    }
    public void insert(String roomId, SeatLayout layout) throws Exception {
        String sql = "INSERT INTO SeatLayout (roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomId);
            ps.setInt(2, layout.getNumberOfRows());
            ps.setInt(3, layout.getSeatsPerRow());
            ps.setInt(4, layout.getVipStartRow());
            ps.setInt(5, layout.getVipEndRow());
            ps.setInt(6, layout.getCoupleRow());

            ps.executeUpdate();
        } catch(Exception ex) {
            System.out.println("Loi insert SeatLayout: " + ex);
            throw ex;
        }
    }

    public void update(String roomId, SeatLayout layout) throws Exception {
        String sql = "UPDATE SeatLayout SET numberOfRows=?, seatsPerRow=?, vipStartRow=?, vipEndRow=?, coupleRow=? WHERE roomId=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, layout.getNumberOfRows());
            ps.setInt(2, layout.getSeatsPerRow());
            ps.setInt(3, layout.getVipStartRow());
            ps.setInt(4, layout.getVipEndRow());
            ps.setInt(5, layout.getCoupleRow());
            ps.setString(6, roomId);

            ps.executeUpdate();
        } catch(Exception ex) {
            System.out.println("Loi update SeatLayout: " + ex);
            throw ex;
        }
    }

    public void delete(String roomId) throws Exception {
        String sql = "DELETE FROM SeatLayout WHERE roomId = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomId);
            ps.executeUpdate();
        } catch(Exception ex) {
            System.out.println("Loi delete SeatLayout: " + ex);
            throw ex;
        }
    }
}