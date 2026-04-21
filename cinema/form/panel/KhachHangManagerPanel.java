package cinema.form.panel;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class KhachHangManagerPanel extends JPanel {

    private JTextField txtSearch;
    private JComboBox<String> cbStatus;
    private JComboBox<String> cbRank;
    private JSlider sliderSpending;
    private JLabel lblRange;
    private JTable table;
    private JButton btnAdd;

    public KhachHangManagerPanel() {
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
        panel.add(new JLabel("Hạng hội viên"));
        panel.add(new JLabel("Chi tiêu (triệu)"));

        txtSearch = new JTextField();

        cbStatus = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});

        cbRank = new JComboBox<>(new String[]{
                "Standard",
                "Gold",
                "Diamond"
        });

        sliderSpending = new JSlider(0, 100, 50);
        lblRange = new JLabel("0 - 50");

        sliderSpending.addChangeListener(e -> {
            lblRange.setText("0 - " + sliderSpending.getValue());
        });

        panel.add(txtSearch);
        panel.add(cbStatus);
        panel.add(cbRank);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBackground(Color.WHITE);
        sliderPanel.add(lblRange, BorderLayout.NORTH);
        sliderPanel.add(sliderSpending, BorderLayout.CENTER);

        panel.add(sliderPanel);

        return panel;
    }

    // ===== HEADER =====
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));

        JLabel title = new JLabel("Danh sách khách hàng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnAdd = new JButton("+ Thêm khách hàng");
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
                        "Mã KH",
                        "Tên KH",
                        "SĐT",
                        "Email",
                        "Hạng thành viên",
                        "Tổng chi tiêu",
                        "Điểm tích lũy",
                        "Trạng thái"
                }
        ){
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        });

        table.setRowHeight(40); 
        table.setShowVerticalLines(false); 
        table.setShowHorizontalLines(true); 
        table.setGridColor(new Color(235,235,235));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(235,235,235)); 
        header.setFont(new Font("Segoe UI", Font.BOLD, 15)); 

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204))); 
        
        return scroll;
    }
}
