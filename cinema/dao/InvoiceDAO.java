package cinema.dao;

import cinema.models.;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    private List<Invoice> invoiceList;

    public InvoiceDAO() {
        invoiceList = new ArrayList<>();
    }

    public void addInvoice(Invoice invoice) {
        invoiceList.add(invoice);
    }

    public Invoice findById(String invoiceId) {
        for (Invoice invoice : invoiceList) {
            if (invoice.getInvoiceId().equals(invoiceId)) {
                return invoice;
            }
        }
        return null;
    }

    public void updateInvoice(Invoice updatedInvoice) {
        for (int i = 0; i < invoiceList.size(); i++) {
            if (invoiceList.get(i).getInvoiceId().equals(updatedInvoice.getInvoiceId())) {
                invoiceList.set(i, updatedInvoice);
                return;
            }
        }
    }

    public void deleteInvoice(String invoiceId) {
        invoiceList.removeIf(invoice -> invoice.getInvoiceId().equals(invoiceId));
    }

    public List<Invoice> getAllInvoices() {
        return invoiceList;
    }
}