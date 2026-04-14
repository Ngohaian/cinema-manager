package cinema.models;
import cinema.enums.GenreType;
public class Movie {
    private String id;
    private String title;
    private String genreId;
    private int duration;
    private boolean active;
    private String poster;
    public Movie() {
    }
    public Movie(String id, String title, String genreId, int duration) {
        this.id = id;
        this.title = title;
        this.genreId = genreId;
        this.duration = (duration > 0) ? duration : 0;
        this.active = true;
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
    public String getGenre() { return genreId; }
    public int getDuration() { return duration; }
    public boolean isActive() { return active; }
    public String getPoster() { return poster; }

    public void setId(String id){this.id = id;}
    public void setTitle(String title){this.title = title;}
    public void setGenreId(String genreId){this.genreId = genreId;}
    public void setDuration(int durartion){this.duration = durartion;}
    public void setActive(boolean active) { this.active = active; }
    public void setPoster(String poster) { this.poster = poster; }

}