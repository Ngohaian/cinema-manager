import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Showtime đại diện cho 1 suất chiếu cụ thể.
 * Mỗi suất có trạng thái ghế riêng.
 */
public class ShowTime {

    private String showtimeId;

    private Movie movie;
    private Screen screen;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private SeatStatus[][] seatStatus;
    private List<Ticket> tickets;
    private int ticketCounter = 1;

    private double basePrice;
    private double vipExtra;
    private double coupleExtra;

    private boolean active;

    public ShowTime(String showtimeId,
                    Movie movie,
                    Screen screen,
                    LocalDateTime startTime,
                    double basePrice) {

        this.showtimeId = showtimeId;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.basePrice = basePrice;
        this.tickets = new ArrayList<>();
        

        // Tính endTime theo thời lượng phim
        this.endTime = startTime.plusMinutes(movie.getDuration());

        this.vipExtra = 30;
        this.coupleExtra = 50;

        this.active = true;

        initializeSeatStatus();
    }

    /**
     * Khởi tạo trạng thái ghế ban đầu = AVAILABLE
     */
    private void initializeSeatStatus() {

        int rows = screen.getSeatLayout().getNumberOfRows();
        int cols = screen.getSeatLayout().getSeatsPerRow();

        seatStatus = new SeatStatus[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                seatStatus[i][j] = SeatStatus.AVAILABLE;
    }
    public Ticket generateTicket(Seat seat){
        int rows = screen.getSeatLayout().getNumberOfRows();
        int cols = screen.getSeatLayout().getSeatsPerRow();
        int row = seat.getRowIndex();
        int col = seat.getColIndex();
        if (row<0 || row >= rows || col >= cols || col < 0)
            throw new IllegalArgumentException("Ghế không tồn tại!");
        if (!bookSeat(row, col))
            throw new IllegalStateException("Ghế đã được đặt trước!");

            String ticketID = getShowtimeId() + String.format("-%03d", ticketCounter++);
            Ticket ticket = new Ticket(ticketID, seat, this);
            tickets.add(ticket);
            return ticket;
        }

    // =============================
    // Kiểm tra và đặt ghế
    // =============================

    public boolean isSeatAvailable(int row, int col) {
        return seatStatus[row][col] == SeatStatus.AVAILABLE;
    }

    public boolean bookSeat(int row, int col) {

        if (!isSeatAvailable(row, col))
            return false;

        seatStatus[row][col] = SeatStatus.BOOKED;
        return true;
    }

    public boolean cancelSeat(int row, int col) {

        if (seatStatus[row][col] == SeatStatus.BOOKED) {
            seatStatus[row][col] = SeatStatus.AVAILABLE;
            return true;
        }

        return false;
    }

    /**
     * Tính giá vé theo loại ghế
     */
    public double calculateSeatPrice(int row, int col) {

        Seat seat = screen.getSeatLayout().getSeat(row, col);

        double price = basePrice;

        if (seat.getSeatType() == SeatType.VIP)
            price += vipExtra;

        if (seat.getSeatType() == SeatType.COUPLE)
            price += coupleExtra;

        return price;
    }

    // =============================
    // Thống kê
    // =============================

    public int getBookedSeatCount() {

        int count = 0;

        for (SeatStatus[] row : seatStatus)
            for (SeatStatus status : row)
                if (status == SeatStatus.BOOKED)
                    count++;

        return count;
    }

    public int getAvailableSeatCount() {

        int count = 0;

        for (SeatStatus[] row : seatStatus)
            for (SeatStatus status : row)
                if (status == SeatStatus.AVAILABLE)
                    count++;

        return count;
    }

    public double getOccupancyRate() {

        int total = screen.getSeatLayout().getTotalSeats();
        int booked = getBookedSeatCount();

        return (double) booked / total * 100;
    }

    /**
     * Kiểm tra trùng lịch chiếu phòng
     */
    public boolean overlaps(LocalDateTime start, LocalDateTime end) {

        return !(end.isBefore(startTime) || start.isAfter(endTime));
    }

    /**
     * Hiển thị sơ đồ ghế suất chiếu
     */
    public void displaySeatMap() {

        for (SeatStatus[] row : seatStatus) {

            for (SeatStatus status : row) {

                if (status == SeatStatus.AVAILABLE)
                    System.out.print("[ ]");
                else
                    System.out.print("[X]");
            }

            System.out.println();
        }
    }

    // ===== Getter =====

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public String getShowtimeId() {
        return showtimeId;
    }
}