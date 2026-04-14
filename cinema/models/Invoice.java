public Invoice() {
}

public Invoice(String invoiceId, String customerId, String invoiceDate, double totalAmount) {
    this.invoiceId = invoiceId;
    this.customerId = customerId;
    this.invoiceDate = invoiceDate;
    this.totalAmount = totalAmount;
}
public double calculateTotalAmount(List<Ticket> tickets) {
    double total = 0;
    for (Ticket ticket : tickets) {
        if(ticket.getInvoiceId().equals(this.invoiceId)) {
            total += ticket.getPrice();
        }
    }
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

public String getInvoiceDate() {
    return invoiceDate;
}

public void setInvoiceDate(String invoiceDate) {
    this.invoiceDate = invoiceDate;
}

public double getTotalAmount() {
    return totalAmount;
}

public void setTotalAmount(double totalAmount) {
    this.totalAmount = totalAmount;
}