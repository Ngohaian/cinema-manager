package cinema.models;
import cinema.enums.TicketStatus;

public class Ticket{
    private String ticketId;
    private String invoiceId;
    private double price;
    private Seat seat;
    private ShowTime showtime;
    private TicketStatus status;

    public Ticket() {
    }
    public Ticket(String ticketId, Seat seat, ShowTime showtime){
        this.ticketId = ticketId;
        this.showtime = showtime;
        this.price = showtime.calculateSeatPrice(seat.getRowIndex(),seat.getColIndex());
        this.seat = seat;
        this.status = TicketStatus.Available;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    public void setShowtime(ShowTime showtime) {
        this.showtime = showtime;
    }
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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
    public String getInvoiceId() {
        return invoiceId;
    }
}