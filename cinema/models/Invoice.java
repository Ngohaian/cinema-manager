package cinema.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private String invoiceId;
    private String customerId;
    private String employeeId;
    private LocalDateTime invoiceDate;
    private double totalAmount;

    private List<Ticket> tickets;

    public Invoice() {
        tickets = new ArrayList<>();
    }

    public Invoice(String invoiceId, String customerId,String employeeId, LocalDateTime invoiceDate, double totalAmount) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.employeeId = employeeId;
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

    public String getCustomerId() {
        return customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
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

    // ===== SET =====

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}