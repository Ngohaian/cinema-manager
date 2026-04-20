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
}
