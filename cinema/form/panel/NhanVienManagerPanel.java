package cinema.form.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        container.add(Box.createVerticalStrut(50)); 
        container.add(createHeader());
        container.add(Box.createVerticalStrut(20));
        container.add(createTable());

        add(container, BorderLayout.CENTER);
    }

    // ===== FILTER =====
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 100)); 

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
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
        btnAdd.setPreferredSize(new Dimension(210, 40)); 
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));

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
        ) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        });

        table.setRowHeight(40); 
        table.setShowVerticalLines(false); 
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(235, 235, 235)); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(235, 235, 235));
        header.setFont(new Font("Segoe UI", Font.BOLD, 15)); 

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));

        return scroll;
    }
}
