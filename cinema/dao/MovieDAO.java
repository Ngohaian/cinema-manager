package cinema.dao;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import cinema.DBConnection;
import cinema.models.Movie;
import cinema.enums.MovieStatus;
public class MovieDAO {
    public List<Movie> getDSPhim(){
        List<Movie> list = new ArrayList<>();
        String sql = "Select * from movie";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(); ){
                
                while(rs.next()){
                    Movie m = new Movie();
                    m.setId(rs.getString("movieId"));
                    m.setTitle(rs.getString("title"));
                    m.setGenreId(rs.getInt("genreId"));
                    m.setDuration(rs.getInt("duration"));
                    m.setActive(MovieStatus.fromInt(rs.getInt("active")));
                    m.setPoster(rs.getString("poster"));
                    list.add(m);
                }
                
            }
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return list;
    }
    public List<Movie> GetAvailableMovies(){
        List<Movie> ds = new ArrayList<>();
        String sql = "SELECT DISTINCT m.* FROM movie m JOIN showtime s ON m.movieId = s.movieId WHERE m.active = 1 AND s.startTime >= now() AND DAY(s.startTime) = DAY(NOW()) AND s.active=1";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Movie m = new Movie();
                    m.setId(rs.getString("movieId"));
                    m.setTitle(rs.getString("title"));
                    m.setGenreId(rs.getInt("genreId"));
                    m.setDuration(rs.getInt("duration"));
                    m.setActive(MovieStatus.fromInt(rs.getInt("active")));
                    m.setPoster(rs.getString("poster"));
                    ds.add(m);
                }
            }
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return ds;
    }
    public boolean InsertMovie(Movie m){
        String sql ="Insert into movie (movieId, title, genreId, duration, poster, active) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1,m.getId());
            ps.setString(2,m.getTitle());
            ps.setInt(3,m.getGenreId());
            ps.setInt(4,m.getDuration());
            ps.setString(5,m.getPoster());
            ps.setInt(6,m.getActive().getValue());
            return ps.executeUpdate()>0;
        }catch(SQLException ex){
            System.out.print("Loi "+ex);
        }
        return false;
    }
    public boolean UpdateMovie(Movie m){
        String sql = "Update movie set title=?, genreId=?, duration=?, active=?, poster=? where movieId=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, m.getTitle());
            ps.setInt(2, m.getGenreId());
            ps.setInt(3, m.getDuration());
            ps.setInt(4, m.getActive().getValue());
            ps.setString(5, m.getPoster());
            ps.setString(6, m.getId());
            return ps.executeUpdate() > 0;
        }
        catch(SQLException ex){
            System.out.println("Loi "+ ex);
            return false;
        }
    }
    public Movie getById(String id){
        String sql = "Select * from movie where movieId=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    Movie m = new Movie();
                    m.setId(rs.getString("movieId"));
                    m.setTitle(rs.getString("title"));
                    m.setGenreId(rs.getInt("genreId"));
                    m.setDuration(rs.getInt("duration"));
                    m.setActive(MovieStatus.fromInt(rs.getInt("active")));
                    m.setPoster(rs.getString("poster"));
                    return m;
                }
            }
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return null;
    }
    public List<String> getDSTheLoai(){
        java.util.List<String> dsTheLoai = new java.util.ArrayList<>();
        dsTheLoai.add("Tất cả");
        String sql="Select genreName from genre";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    dsTheLoai.add(rs.getString(1));
                }    
                return dsTheLoai;
            }
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return null;
    }
    public String getNextMovieID() {
        String sql = "SELECT movieId FROM Movie ORDER BY movieId DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("movieId");
                int number = Integer.parseInt(lastId.substring(1)); 
                return String.format("M%03d", number + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "M001";
    }
    public List<Movie> searchMovies(String key, int statusIdx, int genreIdx, int maxDuration ){
        java.util.List<Movie> dsLoc = new java.util.ArrayList<>();
        String sql = "Select * from movie where ";
        sql += "duration <= " + maxDuration;
        if (key != null && !key.trim().isEmpty()) {
            sql += " AND (title LIKE ? OR movieId = ?)";
        }
        if (statusIdx > 0) {
            sql += " AND active = ?";
        }
        if (genreIdx > 0) {
            sql += " AND genreId = ?";
        }
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
           int paramIdx=1;
           if (key != null && !key.trim().isEmpty()) {
               ps.setString(paramIdx++, "%" + key + "%");
               ps.setString(paramIdx++, key);
           }
           if (statusIdx > 0) {
               ps.setInt(paramIdx++, statusIdx);
           }
           if (genreIdx > 0) {
               ps.setInt(paramIdx++, genreIdx );
           }

           ResultSet rs = ps.executeQuery();
           while (rs.next()) {
                Movie m = new Movie();
                m.setId(rs.getString("movieId"));
                m.setTitle(rs.getString("title"));
                m.setGenreId(rs.getInt("genreId"));
                m.setDuration(rs.getInt("duration"));
                m.setActive(MovieStatus.fromInt(rs.getInt("active")));
                m.setPoster(rs.getString("poster"));
                dsLoc.add(m);
           }
       } catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
       }
       return dsLoc;
   } 
    public int getMaxDuration(){
        String sql = "SELECT MAX(duration) AS max_duration FROM movie";
        int max=0;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    max = rs.getInt("max_duration");
            }
        }        
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return max; 
    }
    public int getMinDuration(){
        String sql = "SELECT MIN(duration) AS min_duration FROM movie";
        int min=0;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    min = rs.getInt("min_duration");
            }
        }        
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return min; 
    }
    public Object[][] getTop5Movie(){
        String sql = "SELECT p.title, SUM(v.price) AS `Doanh Thu`, COUNT(v.ticketId) AS `Tong Ve` " +
                     "FROM movie p JOIN showtime sc ON p.movieId = sc.movieId JOIN ticket v ON sc.showtimeId = v.showtimeId " +
                     "                     where month(sc.startTime) = month(current_date()) and year(sc.startTime) = year(current_date())"+
                     "GROUP BY p.movieId, p.title ORDER BY SUM(v.price) DESC LIMIT 5";
        java.util.List<Object[]> list = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tenPhim = rs.getString("title");
                long doanhThu = rs.getLong("Doanh Thu");
                int tongVe = rs.getInt("Tong Ve");
                list.add(new Object[]{tenPhim, doanhThu, tongVe});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new Object[0][]);
    }
}
