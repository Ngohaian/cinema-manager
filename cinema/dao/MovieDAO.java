package cinema.dao;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import cinema.DBConnection;
import cinema.models.Movie;
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
                    m.setGenreId(rs.getString("genreId"));
                    m.setDuration(rs.getInt("duration"));
                    m.setActive(Boolean.parseBoolean(rs.getString("active")));
                    m.setPoster(rs.getString("poster"));
                    list.add(m);
                }
                
            }
        catch(SQLException ex){
            System.out.print("Co loi" + ex.getMessage());
        }
        return list;
    }
}
