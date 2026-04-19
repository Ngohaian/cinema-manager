package cinema.dao;

import cinema.DBConnection;
import cinema.models.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ShowTimeDAO {
	private MovieDAO movieDAO = new MovieDAO();
	private RoomDAO roomDAO = new RoomDAO();

	public List<ShowTime> getAll() throws Exception {
		List<ShowTime> list = new ArrayList<>();
		String sql = "SELECT * FROM Showtime WHERE active = 1";
		Connection conn = DBConnection.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		while(rs.next()) {
			String movieId = rs.getString("movieId");
			String roomId = rs.getString("roomId");

			Movie movie = movieDAO.getById("movieId");
			Room room = roomDAO.getById("roomId");

			LocalDateTime start = rs.getTimestamp("startTime").toLocalDateTime();

			ShowTime s = new ShowTime(
				rs.getString("showtimeId"),
				movie,
				room,
				start,
				rs.getDouble("basePrice")
			);
			list.add(s);
		}
		return list;
	}
	public void insert(ShowTime s) throws Exception {
		String sql = "INSERT INTO Showtime VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection conn = DBConnection.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, s.getShowtimeId());
		ps.setString(2, s.getMovie().getId());
		ps.setString(3, s.getRoom().getRoomId());
		ps.setTimestamp(4, Timestamp.valueOf(s.getStartTime()));
		ps.setTimestamp(5, Timestamp.valueOf(s.getEndTime()));
		ps.setDouble(6, s.getBasePrice());
		ps.setDouble(7, 30); // vipExtra
		ps.setDouble(8, 50); // coupleExtra
		ps.setBoolean(9, true);
	} 
}