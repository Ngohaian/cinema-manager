package cinema.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Invoice {

    private String invoiceId;
    private String customerId;
    private Date invoiceDate;
    private double totalAmount;

    private List<Ticket> tickets;

    public Invoice() {
        tickets = new ArrayList<>();
    }

    public Invoice(String invoiceId, String customerId, Date invoiceDate, double totalAmount) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.tickets = new ArrayList<>();
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (Ticket ticket : tickets) {
            total += ticket.getPrice();
        }
        this.totalAmount = total;
        return total;
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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}