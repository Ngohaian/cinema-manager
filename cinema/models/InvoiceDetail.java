package cinema.models;
public class InvoiceDetail {

    private String invoiceDetailId;
    private String itemName;
    private String itemType;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    public enum ItemType {
        TICKET,
        FOOD,
        DRINK
    }
    public InvoiceDetail() {
    }

    public InvoiceDetail(String invoiceDetailId, String itemName,
                         String itemType, int quantity, double unitPrice) {
        this.invoiceDetailId = invoiceDetailId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();
    }

    public double calculateTotalPrice() {
        return quantity * unitPrice;
    }

    public String getInvoiceDetailId() {
        return invoiceDetailId;
    }

    public void setInvoiceDetailId(String invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
