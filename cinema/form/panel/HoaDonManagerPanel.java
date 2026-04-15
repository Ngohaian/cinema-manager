package cinema.form.panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoaDonManagerPanel extends JPanel {

    private JTable table;
    private JTextField txtCustomer, txtEmployee, txtTotal;

    public HoaDonManagerPanel() {
        setLayout(new BorderLayout());

        // ===== TOP =====
        JPanel top = new JPanel(new GridLayout(2, 4, 10, 10));

        top.add(new JLabel("Khách hàng"));
        txtCustomer = new JTextField();
        top.add(txtCustomer);

        top.add(new JLabel("Nhân viên"));
        txtEmployee = new JTextField();
        top.add(txtEmployee);

        top.add(new JLabel("Tổng tiền"));
        txtTotal = new JTextField();
        top.add(txtTotal);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[]{"ID", "Customer", "Employee", "Date", "Total"}, 0
        ));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON =====
        JPanel bottom = new JPanel();

        bottom.add(new JButton("Thêm"));
        bottom.add(new JButton("Sửa"));
        bottom.add(new JButton("Xóa"));

        add(bottom, BorderLayout.SOUTH);
    }
}
