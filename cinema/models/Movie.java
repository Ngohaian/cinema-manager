package cinema.models;
import cinema.enums.GenreType;
import cinema.enums.MovieStatus;
public class Movie {
    private String id;
    private String title;
    private int genreId;
    private int duration;
    private MovieStatus active;
    private String poster;
    public Movie() {
    }
    public Movie(String id, String title, int genreId, int duration, MovieStatus active) {
        this.id = id;
        this.title = title;
        this.genreId = genreId;
        this.duration = (duration > 0) ? duration : 0;
        this.active = active;
    }
    public GenreType getGenreFromId(int genreId){
        switch (genreId) {
            case 1: return GenreType.HoatHinh;
            case 2: return GenreType.GiaDinh;
            case 3: return GenreType.Hai;
            case 4: return GenreType.PhieuLuu;
            case 5: return GenreType.HanhDong;
            case 6: return GenreType.KinhDi;
            case 7: return GenreType.LangMan;
            default: return null;
        }
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public GenreType getGenre() { return getGenreFromId(genreId); }
    public int getDuration() { return duration; }
    public MovieStatus getStatus() { return active; }
    public String getPoster() { return poster; }

    public void setId(String id){this.id = id;}
    public void setTitle(String title){this.title = title;}
    public void setGenreId(int genreId){this.genreId = genreId;}
    public void setDuration(int durartion){this.duration = durartion;}
    public void setActive(MovieStatus active) { this.active = active; }
    public void setPoster(String poster) { this.poster = poster; }

}