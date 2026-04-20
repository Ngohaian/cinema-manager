package cinema.form.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class NhanVienManagerPanel extends JPanel {

    private JTextField txtSearch;
    private JComboBox<String> cbStatus;
    private JComboBox<String> cbRole;
    private JSlider sliderSalary;
    private JTable table;
    private JButton btnAdd;

    public NhanVienManagerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(40, 30, 40, 30));
        container.setBackground(new Color(248, 250, 252));

        container.add(createFilterPanel());
        container.add(Box.createVerticalStrut(30));
        container.add(createHeader());
        container.add(Box.createVerticalStrut(15));
        container.add(createTable());

        add(container, BorderLayout.CENTER);
    }

    // ===== FILTER =====
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        panel.add(new JLabel("Tìm kiếm"));
        panel.add(new JLabel("Trạng thái"));
        panel.add(new JLabel("Chức vụ"));
        panel.add(new JLabel("Mức lương"));

        txtSearch = new JTextField();

        cbStatus = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});

        cbRole = new JComboBox<>(new String[]{
                "Nhân viên bán vé",
                "Nhân viên check vé"
        });

        sliderSalary = new JSlider();

        panel.add(txtSearch);
        panel.add(cbStatus);
        panel.add(cbRole);
        panel.add(sliderSalary);

        return panel;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));

        JLabel title = new JLabel("Danh sách nhân viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnAdd = new JButton("+ Thêm nhân viên");
        btnAdd.setBackground(new Color(0, 146, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        panel.add(title, BorderLayout.WEST);
        panel.add(btnAdd, BorderLayout.EAST);

        return panel;
    }

    // ===== TABLE =====
    private JScrollPane createTable() {
        table = new JTable();

        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Mã NV",
                        "Tên NV",
                        "Chức vụ",
                        "Email",
                        "SĐT",
                        "Mức lương",
                        "Ngày vào làm",
                        "Trạng thái"
                }
        ));

        table.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        return scroll;
    }
}