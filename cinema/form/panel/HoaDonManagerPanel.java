package cinema.form.panel;

import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HoaDonManagerPanel extends JPanel {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private JTable DSTable;
    private JTextField txtSearch, txtFrom, txtTo;
    private JComboBox<String> cbStatus;
    private JButton btnAdd;

    public HoaDonManagerPanel() {
        initComponents();
        setTable(DSTable);
        loadTableInvoice();
        initSearch();
        initFilterStatus();
        initAction();
    }

    // ================= LOAD DATA =================
    public void generateRowTable(Invoice inv) {
        String date = "Chưa có ngày";
        if (inv.getInvoiceDate() != null) {
            date = new SimpleDateFormat("dd/MM/yyyy").format(inv.getInvoiceDate());
        }

        ((DefaultTableModel) DSTable.getModel()).addRow(new Object[]{
                inv.getInvoiceId(),
                "Nguyễn Văn A",
                "0912345678",
                date,
                "EMP01",
                String.format("%.0f đ", inv.getTotalAmount()),
                "Đã thanh toán",
                editIcon()
        });
    }

    public void loadTableInvoice() {
        DefaultTableModel model = (DefaultTableModel) DSTable.getModel();
        model.setRowCount(0);

        List<Invoice> list = invoiceDAO.getAll();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"INV001", "Nguyễn Văn A", "0912345678", "01/04/2026", "EMP01", "90000 đ", "Đã thanh toán", editIcon()});
            model.addRow(new Object[]{"INV002", "Nguyễn Văn B", "0987654321", "02/04/2026", "EMP02", "120000 đ", "Chưa thanh toán", editIcon()});
            model.addRow(new Object[]{"INV003", "Trần Văn C", "0909123456", "03/04/2026", "EMP01", "150000 đ", "Đã thanh toán", editIcon()});
            model.addRow(new Object[]{"INV004", "Lê Thị D", "0977123456", "04/04/2026", "EMP03", "80000 đ", "Chưa thanh toán", editIcon()});
            model.addRow(new Object[]{"INV005", "Phạm Văn E", "0933123456", "05/04/2026", "EMP02", "200000 đ", "Đã thanh toán", editIcon()});
            model.addRow(new Object[]{"INV006", "Hoàng Văn F", "0944123456", "06/04/2026", "EMP01", "175000 đ", "Chưa thanh toán", editIcon()});
            return;
        }

        for (Invoice inv : list) {
            generateRowTable(inv);
        }
    }

    private ImageIcon editIcon() {
        var url = getClass().getResource("/cinema/images/edit(black).png");
        return url != null ? new ImageIcon(url) : null;
    }

    // ================= UI COMPONENTS =================
    private void initComponents() {
        setBackground(new Color(248, 250, 252));
        setLayout(new BorderLayout(0, 20)); 
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- 1. Filter Panel (Thanh tìm kiếm) ---
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));

        txtSearch = new JTextField();
        txtFrom = new JTextField();
        txtTo = new JTextField();
        cbStatus = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});

        styleInput(txtSearch, "Tìm kiếm...");
        styleInput(txtFrom, "Từ ngày");
        styleInput(txtTo, "Đến ngày");

        filterPanel.add(txtSearch);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(txtFrom);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(txtTo);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(cbStatus);

        // --- 2. Title & Button Panel (Dòng tiêu đề và nút Thêm) ---
        JLabel title = new JLabel("Danh sách hóa đơn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        btnAdd = new JButton("+ Thêm hóa đơn");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(new Color(0, 146, 255));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setPreferredSize(new Dimension(200, 40));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);  // Tiêu đề bên trái
        titlePanel.add(btnAdd, BorderLayout.EAST); // Nút bấm bên phải

        // --- 3. Top Container (Gộp 1 và 2) ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);
        topContainer.add(filterPanel);
        topContainer.add(Box.createVerticalStrut(20)); // Khoảng cách giữa thanh lọc và tiêu đề
        topContainer.add(titlePanel);

        // --- 4. Table Section ---
        DSTable = new JTable();
        DSTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HD", "Khách hàng", "SĐT", "Ngày lập", "Nhân viên", "Tổng tiền", "Trạng thái", ""}
        ) {
            public Class<?> getColumnClass(int col) {
                return col == 7 ? Icon.class : String.class;
            }
            public boolean isCellEditable(int r, int c) { return false; }
        });

        JScrollPane scrollPane = new JScrollPane(DSTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Thêm vào Panel chính
        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= STYLE & LOGIC (GIỮ NGUYÊN) =================
    private void initSearch() {
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            private void filter() {
                String keyword = txtSearch.getText();
                if(keyword.equals("Tìm kiếm...")) keyword = "";
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(DSTable.getModel());
                DSTable.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        });
    }

    private void initFilterStatus() {
        cbStatus.addActionListener(e -> {
            String selected = cbStatus.getSelectedItem().toString();
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(DSTable.getModel());
            DSTable.setRowSorter(sorter);
            if (selected.equals("Tất cả")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(selected, 6));
            }
        });
    }

    private void initAction() {
        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở form thêm hóa đơn"));
        DSTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = DSTable.getSelectedRow();
                int col = DSTable.getSelectedColumn();
                if (col == 7 && row != -1) {
                    String id = DSTable.getValueAt(row, 0).toString();
                    JOptionPane.showMessageDialog(null, "Sửa hóa đơn: " + id);
                }
            }
        });
    }

    private void styleInput(JTextField txt, String placeholder) {
        txt.setFont(new Font("Segoe UI", 0, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        txt.setText(placeholder);
        txt.setForeground(Color.GRAY);

        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(Color.GRAY);
                }
            }
        });
    }

    public void setTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", 0, 14));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(235, 235, 235));
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setMaxWidth(50);

        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Icon) {
                    setIcon((Icon) value);
                    setText("");
                    setHorizontalAlignment(CENTER);
                } else {
                    super.setValue(value);
                }
            }
        });
    }
}
