package cinema.models;
import cinema.enums.TicketStatus;

public class Ticket{
    private String ticketId;
    private double price;
    private Seat seat;
    private ShowTime showtime;
    private TicketStatus status;

    public Ticket(String ticketId, Seat seat, ShowTime showtime){
        this.ticketId = ticketId;
        this.showtime = showtime;
        this.price = showtime.calculateSeatPrice(seat.getRowIndex(),seat.getColIndex());
        this.seat = seat;
        this.status = TicketStatus.Available;
    }
    public void cancel(){
        if (status == TicketStatus.Sold) {
            status = TicketStatus.Canceled;
            showtime.cancelSeat(seat.getRowIndex(), seat.getColIndex());
        }
    }
    public void setStatus(TicketStatus status){
        this.status = status;
    }
    public String getTicketId() {
        return ticketId;
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