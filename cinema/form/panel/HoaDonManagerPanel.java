package cinema.form.panel;
import cinema.dao.CustomerDAO;
import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;
import cinema.models.Seat;
import cinema.models.Ticket;
import cinema.models.Customer;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDonManagerPanel extends JPanel {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    private JTable DSTable;
    private TableRowSorter<TableModel> sorter;

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
        initFilterDate();
        initAction();
    }

    // ===================== LOAD DATA =====================

    public void generateRowTable(Invoice inv) {
        String date = "Chưa có ngày";
        if (inv.getInvoiceDate() != null) {
            date = inv.getInvoiceDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        ((DefaultTableModel) DSTable.getModel()).addRow(new Object[]{
                inv.getInvoiceId(),
                inv.getCustomerId(),
                inv.getEmployeeId(),
                date,
                String.format("%,.0f đ", inv.getTotalAmount()),
                inv.getStatus() != null ? inv.getStatus() : "Đã thanh toán",
                editIcon()
        });
    }

    public void loadTableInvoice() {
        DefaultTableModel model = (DefaultTableModel) DSTable.getModel();
        model.setRowCount(0);
        List<Invoice> list = invoiceDAO.getAll();
        if (list == null || list.isEmpty()) return;
        for (Invoice inv : list) generateRowTable(inv);
    }

    private ImageIcon editIcon() {
        var url = getClass().getResource("/cinema/images/edit(black).png");
        return url != null ? new ImageIcon(url) : null;
    }

    // ===================== UI CHÍNH =====================

    private void initComponents() {
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ---------- FILTER ----------
        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setBackground(Color.WHITE);
        filterWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

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
        cbStatus = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán", "Đã hủy"});
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setPreferredSize(new Dimension(200, 35));
        pStatus.add(cbStatus);

        filterPanel.add(pSearch);
        filterPanel.add(pFrom);
        filterPanel.add(pTo);
        filterPanel.add(pStatus);
        filterWrapper.add(filterPanel);

        // ---------- TITLE ----------
        JLabel title = new JLabel("Danh sách hóa đơn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);

        JPanel topContainer = new JPanel();
        topContainer.setOpaque(false);
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(filterWrapper);
        topContainer.add(Box.createVerticalStrut(20));
        topContainer.add(titlePanel);

        // ---------- TABLE ----------
        DSTable = new JTable();
        DSTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HD", "Khách hàng", "Nhân viên", "Ngày lập", "Tổng tiền", "Trạng thái", "Thao tác"}
        ) {
            @Override
            public Class<?> getColumnClass(int col) { return col == 6 ? Icon.class : String.class; }
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
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

    // ===================== SEARCH =====================

    private void initSearch() {
        sorter = new TableRowSorter<>(DSTable.getModel());
        DSTable.setRowSorter(sorter);
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String kw = txtSearch.getText();
                if (kw.equals("Nhập từ khóa...")) kw = "";
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + kw));
            }
        });
    }

    // ===================== FILTER STATUS =====================

    private void initFilterStatus() {
        cbStatus.addActionListener(e -> {
            String sel = cbStatus.getSelectedItem().toString();
            if (sel.equals("Tất cả")) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter(sel, 5));
        });
    }

    // ===================== FILTER DATE =====================

    private void initFilterDate() {
        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterDate(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterDate(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterDate(); }

            private void filterDate() {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date fromDate = sdf.parse(txtFrom.getText().trim());
                    java.util.Date toDate   = sdf.parse(txtTo.getText().trim());
                    sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            try {
                                String dateStr = entry.getStringValue(3).split(" ")[0];
                                java.util.Date rowDate = sdf.parse(dateStr);
                                return !rowDate.before(fromDate) && !rowDate.after(toDate);
                            } catch (Exception ex) { return true; }
                        }
                    });
                } catch (Exception ex) {
                    sorter.setRowFilter(null);
                }
            }
        };
        txtFrom.getDocument().addDocumentListener(listener);
        txtTo.getDocument().addDocumentListener(listener);
    }

    // ===================== ACTION =====================

    private void initAction() {
        DSTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = DSTable.rowAtPoint(evt.getPoint());
                int col = DSTable.columnAtPoint(evt.getPoint());
                if (col != 6) return;

                String maHD      = DSTable.getValueAt(row, 0).toString();
                String maKH      = DSTable.getValueAt(row, 1).toString();
                String maNV      = DSTable.getValueAt(row, 2).toString();
                String ngay      = DSTable.getValueAt(row, 3).toString();
                String tongTien  = DSTable.getValueAt(row, 4).toString();
                String trangThai = DSTable.getValueAt(row, 5).toString();
                int modelRow     = DSTable.convertRowIndexToModel(row);

                showInvoiceDialog(maHD, maKH, maNV, ngay, tongTien, trangThai, modelRow);
            }
        });
    }


    // ===================== DIALOG CHI TIẾT =====================
    private void showInvoiceDialog(
            String maHD, String maKH, String maNV,
            String ngay, String tongTien, String trangThai, int modelRow) {

        JDialog dialog = new JDialog();
        dialog.setTitle("Chi tiết hóa đơn");
        dialog.setSize(760, 580);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setModal(true);

        // ===== ROOT =====
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(24, 36, 20, 36));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(30, 40, 60));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        root.add(lblTitle, BorderLayout.NORTH);

        // ===== BODY =====
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        // ----- THÔNG TIN CHUNG -----

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 10, 6, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill   = GridBagConstraints.NONE;

        JPanel pnlInfo = new JPanel(new java.awt.GridLayout(2, 2, 15, 10)); 
        pnlInfo.setBackground(java.awt.Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1), "Thông tin chung"
        ));
        pnlInfo.add(new JLabel("<html><b>Mã hóa đơn:</b> " + maHD + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Ngày lập:</b> " + ngay + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Mã nhân viên:</b> " + maNV +  "</html>"));
        pnlInfo.add(new JLabel("<html><b>Mã khách: </b> " + maKH + "</html>"));
        body.add(pnlInfo);
        body.add(Box.createVerticalStrut(15));

        String[] columnHeaders = {"Mã Vé", "Loại Ghế", "Tên Ghế", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
        JTable tblDetails = new JTable(tableModel);
        tblDetails.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblDetails.setRowHeight(25);
        tblDetails.getTableHeader().setBackground(java.awt.Color.WHITE);
        List<Ticket> tickets = invoiceDAO.getTicketsByInvoiceId(maHD);
        double finalTotal=0;
        for (Ticket ticket : tickets) {
            Seat ghe = ticket.getSeat();
            finalTotal += ticket.getPrice();
            String tenGhe = (char) ('A' + ghe.getRowIndex()) + String.valueOf(ghe.getColIndex() + 1);
            String loaiGhe = ghe.getSeatType() != null ? ghe.getSeatType().toString() : "STANDARD";

            tableModel.addRow(new Object[]{
                ticket.getTicketId(),
                loaiGhe,
                tenGhe,
                String.format("%,.0f VNĐ", ticket.getPrice()),
                String.format("%,.0f VNĐ", ticket.getPrice())
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(tblDetails);
        scrollPane.setBackground(java.awt.Color.WHITE);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 150));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết vị trí ghế đặt"));
        body.add(scrollPane);
        body.add(Box.createVerticalStrut(10));

        // ----- TỔNG TIỀN -----
        JPanel pnlTotals = new JPanel(new GridBagLayout());
        pnlTotals.setBackground(Color.WHITE);
        pnlTotals.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlTotals.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        GridBagConstraints tc = new GridBagConstraints();
        tc.anchor = GridBagConstraints.EAST;
        tc.weightx = 1;
        tc.insets = new Insets(2, 0, 2, 0);

        tc.gridy = 0; tc.gridx = 0;
        JLabel lblTong = new JLabel("Tổng tiền:   " + String.format("%,.0f VNĐ", finalTotal));
        lblTong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlTotals.add(lblTong, tc);

        CustomerDAO customerdao = new CustomerDAO();
        Customer customer = customerdao.getCustomerById(maKH);
        double discount = customer.calculateDiscount(finalTotal);
        double totalAfter= finalTotal - discount;
        tc.gridy = 1;
        JLabel lblGiam = new JLabel("Giảm giá:   "+ String.format("%,.0f VNĐ", discount));
        lblGiam.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblGiam.setForeground(new Color(41, 128, 185));
        pnlTotals.add(lblGiam, tc);

        tc.gridy = 2;
        JLabel lblThanhToan = new JLabel("Tổng thanh toán:   " + String.format("%,.0f VNĐ", totalAfter));
        lblThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblThanhToan.setForeground(new Color(200, 30, 30));
        pnlTotals.add(lblThanhToan, tc);

        body.add(pnlTotals);
        root.add(body, BorderLayout.CENTER);

        // ===== SOUTH: trạng thái + nút hủy =====
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JPanel pnlSt = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSt.setBackground(Color.WHITE);
        JLabel lblStLbl = new JLabel("Trạng thái: ");
        lblStLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblStatusVal = new JLabel(trangThai);
        lblStatusVal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatusVal.setForeground(statusColor(trangThai));
        pnlSt.add(lblStLbl);
        pnlSt.add(lblStatusVal);

        JButton btnHuy = new JButton("Hủy hóa đơn");
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnHuy.setBackground(new Color(220, 50, 50));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setBorderPainted(false);
        btnHuy.setPreferredSize(new Dimension(160, 38));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (trangThai.equals("Đã hủy")) disableHuyBtn(btnHuy);

        btnHuy.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "<html>Bạn có chắc muốn <b>hủy hóa đơn " + maHD + "</b>?<br>"
                            + "Các vé sẽ được chuyển sang trạng thái <b>Canceled</b>.</html>",
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            boolean ok = invoiceDAO.cancelInvoiceAndTickets(maHD);
            if (ok) {
                DefaultTableModel m = (DefaultTableModel) DSTable.getModel();
                m.setValueAt("Đã hủy", modelRow, 5);

                lblStatusVal.setText("Đã hủy");
                lblStatusVal.setForeground(Color.GRAY);
                disableHuyBtn(btnHuy);

                JOptionPane.showMessageDialog(dialog,
                        "Đã hủy hóa đơn " + maHD + ".\nCác vé đã chuyển sang Canceled.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Có lỗi xảy ra. Vui lòng thử lại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlSouth.add(pnlSt, BorderLayout.WEST);
        pnlSouth.add(btnHuy, BorderLayout.EAST);
        root.add(pnlSouth, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    // ===================== HELPERS =====================

    private void disableHuyBtn(JButton btn) {
        btn.setEnabled(false);
        btn.setBackground(new Color(180, 180, 180));
        btn.setText("Đã hủy");
    }

    private Color statusColor(String status) {
        if ("Đã thanh toán".equals(status)) return new Color(0, 150, 0);
        if ("Đã hủy".equals(status))        return Color.GRAY;
        return new Color(220, 50, 50);
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel("<html><b>" + text + "</b></html>");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JLabel plainLabel(String text) {
        JLabel l = new JLabel(text != null ? text : "");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private double parseTien(String s) {
        try { return Double.parseDouble(s.replaceAll("[^0-9]", "")); }
        catch (Exception e) { return 0; }
    }

    // ===================== INPUT STYLE =====================

    private void styleInput(JTextField txt, String placeholder) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        txt.setText(placeholder);
        txt.setForeground(Color.GRAY);
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText(""); txt.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(placeholder); txt.setForeground(Color.GRAY);
                }
            }
        });
    }

    // ===================== TABLE STYLE =====================

    public void setTable(JTable table) {
        table.setRowHeight(46);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(235, 235, 235));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(232, 240, 254));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 242, 245));
        header.setForeground(new Color(50, 50, 70));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 44));
        header.setReorderingAllowed(false);

        // Mã HD | Khách hàng | Nhân viên | Ngày lập | Tổng tiền | Trạng thái | Thao tác
        int[] widths = {100, 100, 110, 170, 120, 150, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        table.getColumnModel().getColumn(6).setMaxWidth(80);
        table.getColumnModel().getColumn(6).setMinWidth(80);

        // Căn giữa: Mã HD, Nhân viên, Ngày lập, Tổng tiền
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 1, 2, 3, 4}) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerRender);
        }

        // Icon cột Thao tác
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Icon) {
                    setIcon((Icon) value); setText(""); setHorizontalAlignment(CENTER);
                } else { super.setValue(value); }
            }
        });

        // Màu + căn giữa cột Trạng thái
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected && value != null) {
                    c.setForeground(statusColor(value.toString()));
                }
                return c;
            }
        });
    }
}