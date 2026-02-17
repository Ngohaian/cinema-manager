import java.util.Scanner;
public enum TicketStatus{
    Available,
    Sold,
    Used;
}
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
    }
    
}