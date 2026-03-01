/**
 * Seat đại diện cho GHẾ VẬT LÝ trong phòng chiếu.
 * Không chứa trạng thái đặt vé theo suất chiếu.
 * Trạng thái ghế nằm trong Showtime.
 */
public class Seat {

    private int rowIndex;
    private int colIndex;
    private String seatLabel;
    private SeatType seatType;
    private boolean active; 

    public Seat(int rowIndex, int colIndex, String seatLabel, SeatType seatType) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.seatLabel = seatLabel;
        this.seatType = seatType;
        this.active = true;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ====== tiện ích thực tế ======

    public boolean isVip() {
        return seatType == SeatType.VIP;
    }

    public boolean isCouple() {
        return seatType == SeatType.COUPLE;
    }

    @Override
    public String toString() {
        return seatLabel + " (" + seatType + ")";
    }
}