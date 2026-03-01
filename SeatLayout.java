
/**
 * SeatLayout quản lý sơ đồ ghế của phòng chiếu.
 * Trong thực tế mỗi phòng có layout riêng.
 */
public class SeatLayout {

    private int numberOfRows;
    private int seatsPerRow;

    private int vipStartRow;
    private int vipEndRow;
    private int coupleRow;

    private Seat[][] seats;

    public SeatLayout(int numberOfRows,
                      int seatsPerRow,
                      int vipStartRow,
                      int vipEndRow,
                      int coupleRow) {

        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
        this.vipStartRow = vipStartRow;
        this.vipEndRow = vipEndRow;
        this.coupleRow = coupleRow;

        generateSeats();
    }

    /**
     * Khởi tạo toàn bộ ghế phòng chiếu
     * Gán nhãn A1, A2, B1...
     */
    private void generateSeats() {

        seats = new Seat[numberOfRows][seatsPerRow];

        for (int i = 0; i < numberOfRows; i++) {

            char rowChar = (char) ('A' + i);

            for (int j = 0; j < seatsPerRow; j++) {

                String label = rowChar + String.valueOf(j + 1);

                SeatType type = determineSeatType(i);

                seats[i][j] = new Seat(i, j, label, type);
            }
        }
    }

    /**
     * Xác định loại ghế theo hàng
     */
    private SeatType determineSeatType(int row) {

        if (row >= vipStartRow && row <= vipEndRow)
            return SeatType.VIP;

        if (row == coupleRow)
            return SeatType.COUPLE;

        return SeatType.REGULAR;
    }

    public Seat getSeat(int row, int col) {
        return seats[row][col];
    }

    public Seat findSeatByLabel(String label) {

        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.getSeatLabel().equalsIgnoreCase(label)) {
                    return seat;
                }
            }
        }
        return null;
    }

    public int getTotalSeats() {
        return numberOfRows * seatsPerRow;
    }

    public int countVipSeats() {

        int count = 0;

        for (Seat[] row : seats)
            for (Seat seat : row)
                if (seat.getSeatType() == SeatType.VIP)
                    count++;

        return count;
    }

    public int countCoupleSeats() {

        int count = 0;

        for (Seat[] row : seats)
            for (Seat seat : row)
                if (seat.getSeatType() == SeatType.COUPLE)
                    count++;

        return count;
    }

    /**
     * Hiển thị layout ghế
     */
    public void displayLayout() {

        for (Seat[] row : seats) {
            for (Seat seat : row) {
                System.out.print(seat.getSeatLabel() + " ");
            }
            System.out.println();
        }
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public Seat[][] getSeats() {
        return seats;
    }
}