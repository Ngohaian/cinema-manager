package cinema.dao;

import cinema.DBConnection;
import cinema.models.Movie;
import java.sql.*;
import java.util.*;

public class MovieDAO {

    // =========================
    // GET ALL
    // =========================
    public List<Movie> getAll() throws Exception {

        List<Movie> list = new ArrayList<>();

        String sql = "SELECT * FROM Movie WHERE active = 1";

        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {

            Movie m = new Movie(
                    rs.getString("movieId"),
                    rs.getString("title"),
                    String.valueOf(rs.getInt("genreId")), // ⚠️ convert int → String
                    rs.getInt("duration")
            );

            m.setActive(rs.getBoolean("active"));

            list.add(m);
        }

        return list;
    }

    // =========================
    // GET BY ID (QUAN TRỌNG)
    // =========================
    public Movie getById(String id) throws Exception {

        String sql = "SELECT * FROM Movie WHERE movieId=?";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            Movie m = new Movie(
                    rs.getString("movieId"),
                    rs.getString("title"),
                    String.valueOf(rs.getInt("genreId")),
                    rs.getInt("duration")
            );

            m.setActive(rs.getBoolean("active"));

            return m;
        }

        return null;
    }

    // =========================
    // INSERT
    // =========================
    public void insert(Movie m) throws Exception {

        String sql = "INSERT INTO Movie(movieId, title, genreId, duration, active) VALUES (?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, m.getId());
        ps.setString(2, m.getTitle());
        ps.setInt(3, Integer.parseInt(m.getGenre())); // ⚠️ String → int
        ps.setInt(4, m.getDuration());
        ps.setBoolean(5, m.isActive());

        ps.executeUpdate();
    }

    // =========================
    // UPDATE
    // =========================
    public void update(Movie m) throws Exception {

        String sql = "UPDATE Movie SET title=?, genreId=?, duration=?, active=? WHERE movieId=?";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, m.getTitle());
        ps.setInt(2, Integer.parseInt(m.getGenre()));
        ps.setInt(3, m.getDuration());
        ps.setBoolean(4, m.isActive());
        ps.setString(5, m.getId());

        ps.executeUpdate();
    }

    // =========================
    // DELETE (soft delete)
    // =========================
    public void delete(String id) throws Exception {

        String sql = "UPDATE Movie SET active = 0 WHERE movieId=?";

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, id);
        ps.executeUpdate();
    }
}