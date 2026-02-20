public class Ticket{
    String id;
    double price;
    //ShowTime showtime;
    //Seat seat;
    TicketStatus status;

    public Ticket(String id, double price, TicketStatus status){
        this.id = id;
        this.price = price;
        this.status = status;
        //this.showtime = showtime;
        //this.seat = seat;
    }
    public void setStatus(TicketStatus status){
        this.status = status;
    }

}