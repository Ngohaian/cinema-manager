public class Room {
    private String roomId;
    private String name;
    private int capacity;
    private String type; // 2D, 3D, IMAX

    public Room(String roomId, String name, int capacity, String type) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("| %-6s | %-15s | %3d ghế | %-6s |", 
          roomId, name, capacity, type);
    }
}