public class Ticket{
    private String id;
    private double price;
    private Seat seat;
    private ShowTime showtime;
    private TicketStatus status;
    private static int autoId = 1;

    public Ticket(Seat seat, ShowTime showtime){
        this.id = String.format("T%06d", autoId++);
        this.showtime = showtime;
        if (!showtime.bookSeat(seat.getRowIndex(), seat.getColIndex()))
            throw new IllegalStateException("Ghế đã được đặt trước!");
        this.price = showtime.calculateSeatPrice(seat.getRowIndex(),seat.getColIndex());
        this.seat = seat;
        this.status = TicketStatus.Sold;
    }
    public void setStatus(TicketStatus status){
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public double getPrice() {
        return price;
    }
    public Seat getSeat() {
        return seat;
    }
    public ShowTime getShowtime() {
        return showtime;
    }
    public TicketStatus getStatus() {
        return status;
    }

}