package cinema.form.panel;

import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HoaDonManagerPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private InvoiceDAO dao = new InvoiceDAO();

    public HoaDonManagerPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        String[] columns = {"Mã hóa đơn", "Mã khách hàng", "Ngày", "Tổng tiền"};

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);

        List<Invoice> list = dao.getAll();

        for (Invoice inv : list) {
            model.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerId(),
                    inv.getInvoiceDate(),
                    inv.getTotalAmount()
            });
        }
    }

    public static void main(String[] args) {
        new HoaDonManagerPanel().setVisible(true);
    }
}