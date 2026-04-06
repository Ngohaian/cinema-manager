package cinema.models;

import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private String invoiceId;
    private String customerId;
    private String invoiceDate;
    private double totalAmount;

    private List<Ticket> tickets;

    public Invoice() {
        tickets = new ArrayList<>();
    }

    public Invoice(String invoiceId, String customerId, String invoiceDate) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(String ticketId) {
        tickets.removeIf(t -> t.getTicketId().equals(ticketId));
    }

    public double calculateTotalAmount() {
        totalAmount = 0;
        for (Ticket t : tickets) {
            totalAmount += t.getPrice();
        }
        return totalAmount;
    }

    public void completeInvoice() {
        calculateTotalAmount();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return calculateTotalAmount();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}