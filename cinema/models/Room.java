package cinema.models;

public class Room {
    private String roomId;
    private String name;
    private int capacity;
    private String type;
    private SeatLayout seatLayout;
    private boolean active;

    public Room(String roomId, String name, int capacity, String type, SeatLayout seatLayout) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.seatLayout = seatLayout;
        this.active = true;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SeatLayout getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(SeatLayout seatLayout) {
        this.seatLayout = seatLayout;
    }

    public boolean isActive() {
        return active;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}