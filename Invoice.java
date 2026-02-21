import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private String invoiceId;
    private String invoiceDate;
    private String customerName;
    private String customerPhone;
    private List<InvoiceDetail> invoiceDetails;
    private double totalAmount;

    public Invoice() {
        invoiceDetails = new ArrayList<>();
    }

    public Invoice(String invoiceId, String invoiceDate,
                   String customerName, String customerPhone) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.invoiceDetails = new ArrayList<>();
    }

    public void addInvoiceDetail(InvoiceDetail detail) {
        invoiceDetails.add(detail);
    }

    public void removeInvoiceDetail(String invoiceDetailId) {
        invoiceDetails.removeIf(
            d -> d.getInvoiceDetailId().equals(invoiceDetailId)
        );
    }

    public double calculateTotalAmount() {
        totalAmount = 0;
        for (InvoiceDetail d : invoiceDetails) {
            totalAmount += d.getTotalPrice();
        }
        return totalAmount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public double getTotalAmount() {
        return calculateTotalAmount();
    }
}