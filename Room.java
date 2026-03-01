public class Room {
    private String roomId;
    private String name;
    private int capacity;
    private String type; // 2D, 3D, IMAX
    private SeatLayout seatLayout;
    private boolean active;

    public Room(String roomId, String name, int capacity, String type, SeatLayout seatLayout) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.seatLayout = seatLayout;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public SeatLayout getSeatLayout() {
        return seatLayout;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return String.format("| %-6s | %-15s | %3d ghế | %-6s |", 
          roomId, name, capacity, type);
    }
}