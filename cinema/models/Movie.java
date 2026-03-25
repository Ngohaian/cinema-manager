package cinema.models;
public class Movie {
    private String id;
    private String title;
    private String genre;
    private int duration;
    private boolean active;

    public Movie(String id, String title, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = (duration > 0) ? duration : 0;
        this.active = true;
    }

    // Getters/Setters (Cần thiết cho các framework sau này như Spring Boot)
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("| %-6s | %-20s | %-12s | %3d phút |", 
                              id, title, genre, duration);
    }
}