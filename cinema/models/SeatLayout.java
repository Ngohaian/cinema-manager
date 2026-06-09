package cinema.models;

import cinema.enums.SeatType;

public class SeatLayout {

    private int numberOfRows;
    private int seatsPerRow;
    private int vipStartRow;
    private int vipEndRow;
    private int coupleRow;
    private Seat[][] seats;

    public SeatLayout(int numberOfRows, int seatsPerRow, int vipStartRow, int vipEndRow, int coupleRow) {
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
        this.vipStartRow = vipStartRow;
        this.vipEndRow = vipEndRow;
        this.coupleRow = coupleRow;
        generateSeats();
    }

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

    private SeatType determineSeatType(int row) {
        if (row == coupleRow) {
            return SeatType.COUPLE;
        }

        if (row >= vipStartRow && row <= vipEndRow) {
            return SeatType.VIP;
        }

        return SeatType.REGULAR;
    }

    public Seat getSeat(int row, int col) {
        return seats[row][col];
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getVipStartRow() {
        return vipStartRow;
    }

    public int getVipEndRow() {
        return vipEndRow;
    }

    public int getCoupleRow() {
        return coupleRow;
    }

    public int getTotalSeats() {
        return numberOfRows * seatsPerRow;
    }

    public int countVipSeats() {
        int count = 0;

        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.getSeatType() == SeatType.VIP) {
                    count++;
                }
            }
        }

        return count;
    }

    public int countCoupleSeats() {
        int count = 0;

        for (Seat[] row : seats) {
            for (Seat seat : row) {
                if (seat.getSeatType() == SeatType.COUPLE) {
                    count++;
                }
            }
        }

        return count;
    }
}