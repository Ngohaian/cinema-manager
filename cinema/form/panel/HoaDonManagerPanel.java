package cinema.form.panel;

import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class HoaDonManagerPanel extends JPanel {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private TableRowSorter<TableModel> sorter;

    private JTable DSTable;

    private JTextField txtSearch;
    private JTextField txtFrom;
    private JTextField txtTo;

    private JComboBox<String> cbStatus;

    public HoaDonManagerPanel() {
        initComponents();
        setTable(DSTable);
        loadTableInvoice();
        initSearch();
        initFilterStatus();
        initAction();
        initFilterDate();
    }

    // ================= LOAD DATA =================

    public void generateRowTable(Invoice inv) {
        String date = "Chưa có ngày";
        if (inv.getInvoiceDate() != null) {
            date = inv.getInvoiceDate()
                      .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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
            model.addRow(new Object[]{"INV001","Nguyễn Văn A","0912345678","01/04/2026","EMP01","90000 đ","Đã thanh toán",editIcon()});
            model.addRow(new Object[]{"INV002","Nguyễn Văn B","0987654321","02/04/2026","EMP02","120000 đ","Chưa thanh toán",editIcon()});
            model.addRow(new Object[]{"INV003","Trần Văn C","0909123456","03/04/2026","EMP01","150000 đ","Đã thanh toán",editIcon()});
            model.addRow(new Object[]{"INV004","Lê Thị D","0977123456","04/04/2026","EMP03","80000 đ","Chưa thanh toán",editIcon()});
            return;
        }
        for (Invoice inv : list) generateRowTable(inv);
    }

    private ImageIcon editIcon() {
        var url = getClass().getResource("/cinema/images/edit(black).png");
        return url != null ? new ImageIcon(url) : null;
    }

    // ================= UI =================

    private void initComponents() {
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ===== FILTER PANEL =====
        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setBackground(Color.WHITE);
        filterWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel filterPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        filterPanel.setOpaque(false);

        JPanel pSearch = makeFilterGroup("Tìm kiếm");
        txtSearch = new JTextField();
        styleInput(txtSearch, "Nhập từ khóa...");
        pSearch.add(txtSearch);

        JPanel pFrom = makeFilterGroup("Từ ngày");
        txtFrom = new JTextField();
        styleInput(txtFrom, "dd/MM/yyyy");
        pFrom.add(txtFrom);

        JPanel pTo = makeFilterGroup("Đến ngày");
        txtTo = new JTextField();
        styleInput(txtTo, "dd/MM/yyyy");
        pTo.add(txtTo);

        JPanel pStatus = makeFilterGroup("Trạng thái");
        cbStatus = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        cbStatus.setPreferredSize(new Dimension(200, 35));
        cbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pStatus.add(cbStatus);

        filterPanel.add(pSearch);
        filterPanel.add(pFrom);
        filterPanel.add(pTo);
        filterPanel.add(pStatus);
        filterWrapper.add(filterPanel);

        JLabel title = new JLabel("Danh sách hóa đơn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);

        JPanel topContainer = new JPanel();
        topContainer.setOpaque(false);
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(filterWrapper);
        topContainer.add(Box.createVerticalStrut(25));
        topContainer.add(titlePanel);

        DSTable = new JTable();
        DSTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HD","Khách hàng","SĐT","Ngày lập","Nhân viên","Tổng tiền","Trạng thái","Thao tác"}
        ) {
            @Override public Class<?> getColumnClass(int col) { return col == 7 ? Icon.class : String.class; }
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });

        JScrollPane scrollPane = new JScrollPane(DSTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel makeFilterGroup(String label) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        return p;
    }

    // ================= SEARCH =================

    private void initSearch() {
        sorter = new TableRowSorter<>(DSTable.getModel());
        DSTable.setRowSorter(sorter);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String kw = txtSearch.getText();
                if (kw.equals("Nhập từ khóa...")) kw = "";
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw));
            }
        });
    }

    // ================= FILTER STATUS =================

    private void initFilterStatus() {
        cbStatus.addActionListener(e -> {
            String sel = cbStatus.getSelectedItem().toString();
            if (sel.equals("Tất cả")) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter(sel, 6));
        });
    }

    private void initFilterDate() {
        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterDate(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterDate(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterDate(); }
            private void filterDate() {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date fromDate = sdf.parse(txtFrom.getText().trim());
                    java.util.Date toDate = sdf.parse(txtTo.getText().trim());
                    TableRowSorter<TableModel> s = new TableRowSorter<>(DSTable.getModel());
                    DSTable.setRowSorter(s);
                    s.setRowFilter(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            try {
                                java.util.Date rowDate = sdf.parse(entry.getStringValue(3));
                                return !rowDate.before(fromDate) && !rowDate.after(toDate);
                            } catch (Exception ex) { return true; }
                        }
                    });
                } catch (Exception ex) { DSTable.setRowSorter(null); }
            }
        };
        txtFrom.getDocument().addDocumentListener(listener);
        txtTo.getDocument().addDocumentListener(listener);
    }

    // ================= ACTION (dialog chi tiết) =================

    private void initAction() {
        DSTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = DSTable.rowAtPoint(evt.getPoint());
                int col = DSTable.columnAtPoint(evt.getPoint());
                if (col != 7) return;

                // Lấy dữ liệu dòng
                String maHD      = DSTable.getValueAt(row, 0).toString();
                String tenKH     = DSTable.getValueAt(row, 1).toString();
                String sdt       = DSTable.getValueAt(row, 2).toString();
                String ngay      = DSTable.getValueAt(row, 3).toString();
                String maNV      = DSTable.getValueAt(row, 4).toString();
                String tongTien  = DSTable.getValueAt(row, 5).toString();
                String trangThai = DSTable.getValueAt(row, 6).toString();
                int modelRow     = DSTable.convertRowIndexToModel(row);

                showInvoiceDialog(maHD, tenKH, sdt, ngay, maNV, tongTien, trangThai, modelRow);
            }
        });
    }

    /**
     * Dialog chi tiết hóa đơn – layout giống renderHoaDon.
     * Không có textbox nhập KH, chỉ hiển thị thông tin.
     * Nút "Hủy hóa đơn" cập nhật trạng thái thành "Đã hủy" và trả vé.
     */
    private void showInvoiceDialog(String maHD, String tenKH, String sdt,
                                   String ngay, String maNV,
                                   String tongTien, String trangThai,
                                   int modelRow) {

        JDialog dialog = new JDialog();
        dialog.setTitle("Chi tiết hóa đơn");
        dialog.setSize(560, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        // ---- TITLE ----
        JLabel lblTitle = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(44, 62, 80));
        root.add(lblTitle, BorderLayout.NORTH);

        // ---- BODY ----
        JPanel pnlBody = new JPanel();
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));

        // ---- THÔNG TIN CHUNG ----
        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 15, 10));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Thông tin chung"));

        pnlInfo.add(new JLabel("<html><b>Mã hóa đơn:</b> " + maHD + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Ngày lập:</b> " + ngay + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Nhân viên:</b> " + maNV + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Tổng tiền:</b> " + tongTien + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Khách hàng:</b> " + tenKH + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Số điện thoại:</b> " + sdt + "</html>"));

        pnlInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, pnlInfo.getPreferredSize().height + 50));
        pnlBody.add(pnlInfo);
        pnlBody.add(Box.createVerticalStrut(15));

        // ---- TRẠNG THÁI ----
        JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlStatus.setBackground(Color.WHITE);
        JLabel lblStatusTxt = new JLabel("Trạng thái: ");
        lblStatusTxt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblStatusVal = new JLabel(trangThai);
        lblStatusVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatusVal.setForeground(trangThai.equals("Đã thanh toán") ? new Color(0, 150, 0)
                : trangThai.equals("Đã hủy") ? new Color(150, 150, 150) : new Color(220, 50, 50));
        pnlStatus.add(lblStatusTxt);
        pnlStatus.add(lblStatusVal);
        pnlStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pnlBody.add(pnlStatus);
        pnlBody.add(Box.createVerticalStrut(10));

        root.add(pnlBody, BorderLayout.CENTER);

        // ---- NÚT HỦY HÓA ĐƠN ----
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        pnlBtn.setBackground(Color.WHITE);

        JButton btnHuy = new JButton("Hủy hóa đơn");
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuy.setBackground(new Color(220, 50, 50));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);

        // Nếu hóa đơn đã hủy rồi thì disable nút
        if (trangThai.equals("Đã hủy")) {
            btnHuy.setEnabled(false);
            btnHuy.setBackground(new Color(180, 180, 180));
            btnHuy.setText("Đã hủy");
        }

        btnHuy.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "<html>Bạn có chắc muốn <b>hủy hóa đơn " + maHD + "</b>?<br>"
                  + "Các vé sẽ được trả về trạng thái có sẵn.</html>",
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Cập nhật trạng thái trong bảng
                DefaultTableModel model = (DefaultTableModel) DSTable.getModel();
                model.setValueAt("Đã hủy", modelRow, 6);

                // TODO: gọi invoiceDAO.cancelInvoice(maHD) để cập nhật DB
                // TODO: gọi ticketDAO.returnTickets(maHD) để trả vé về Available

                JOptionPane.showMessageDialog(dialog,
                        "Đã hủy hóa đơn " + maHD + ".\nCác vé đã được trả về trạng thái có sẵn.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);

                lblStatusVal.setText("Đã hủy");
                lblStatusVal.setForeground(new Color(150, 150, 150));
                btnHuy.setEnabled(false);
                btnHuy.setBackground(new Color(180, 180, 180));
                btnHuy.setText("Đã hủy");
            }
        });

        pnlBtn.add(btnHuy);
        root.add(pnlBtn, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    // ================= INPUT STYLE =================

    private void styleInput(JTextField txt, String placeholder) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        txt.setText(placeholder);
        txt.setForeground(Color.GRAY);
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) { txt.setText(""); txt.setForeground(Color.BLACK); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().isEmpty()) { txt.setText(placeholder); txt.setForeground(Color.GRAY); }
            }
        });
    }

    // ================= TABLE STYLE =================

    public void setTable(JTable table) {
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setMaxWidth(70);

        // Icon renderer
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Icon) { setIcon((Icon) value); setText(""); setHorizontalAlignment(CENTER); }
                else super.setValue(value);
            }
        });

        // Status color renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String s = value.toString();
                    if (s.equals("Đã thanh toán"))      c.setForeground(new Color(0, 150, 0));
                    else if (s.equals("Đã hủy"))        c.setForeground(new Color(150, 150, 150));
                    else                                 c.setForeground(new Color(220, 50, 50));
                }
                return c;
            }
        });
    }
}