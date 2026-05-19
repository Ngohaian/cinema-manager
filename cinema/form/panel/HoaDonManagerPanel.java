package cinema.form.panel;

import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;
import cinema.models.Seat;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    // ================= LOAD DATA =================

    public void generateRowTable(Invoice inv) {

        String date = "Chưa có ngày";

        if (inv.getInvoiceDate() != null) {
            date = inv.getInvoiceDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        ((DefaultTableModel) DSTable.getModel()).addRow(new Object[]{
                inv.getInvoiceId(),
                inv.getCustomerId(),
                inv.getPhone(),
                date,
                inv.getEmployeeId(),
                String.format("%,.0f đ", inv.getTotalAmount()),
                inv.getStatus(),
                editIcon()
        });
    }

    public void loadTableInvoice() {

        DefaultTableModel model =
                (DefaultTableModel) DSTable.getModel();

        model.setRowCount(0);

        List<Invoice> list = invoiceDAO.getAll();

        if (list == null || list.isEmpty()) {
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

    // ================= UI =================

    private void initComponents() {

        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 20));

        setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        // ================= FILTER =================

        JPanel filterWrapper = new JPanel(new BorderLayout());

        filterWrapper.setBackground(Color.WHITE);

        filterWrapper.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        JPanel filterPanel = new JPanel(
                new GridLayout(1, 4, 20, 0)
        );

        filterPanel.setOpaque(false);

        // SEARCH
        JPanel pSearch = makeFilterGroup("Tìm kiếm");

        txtSearch = new JTextField();

        styleInput(txtSearch, "Nhập từ khóa...");

        pSearch.add(txtSearch);

        // FROM
        JPanel pFrom = makeFilterGroup("Từ ngày");

        txtFrom = new JTextField();

        styleInput(txtFrom, "dd/MM/yyyy");

        pFrom.add(txtFrom);

        // TO
        JPanel pTo = makeFilterGroup("Đến ngày");

        txtTo = new JTextField();

        styleInput(txtTo, "dd/MM/yyyy");

        pTo.add(txtTo);

        // STATUS
        JPanel pStatus = makeFilterGroup("Trạng thái");

        cbStatus = new JComboBox<>(new String[]{
                "Tất cả",
                "Đã thanh toán",
                "Chưa thanh toán",
                "Đã hủy"
        });

        cbStatus.setPreferredSize(new Dimension(200, 35));

        pStatus.add(cbStatus);

        filterPanel.add(pSearch);
        filterPanel.add(pFrom);
        filterPanel.add(pTo);
        filterPanel.add(pStatus);

        filterWrapper.add(filterPanel);

        // ================= TITLE =================

        JLabel title = new JLabel("Danh sách hóa đơn");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        JPanel titlePanel = new JPanel(new BorderLayout());

        titlePanel.setOpaque(false);

        titlePanel.add(title, BorderLayout.WEST);

        JPanel topContainer = new JPanel();

        topContainer.setOpaque(false);

        topContainer.setLayout(
                new BoxLayout(topContainer, BoxLayout.Y_AXIS)
        );

        topContainer.add(filterWrapper);

        topContainer.add(Box.createVerticalStrut(25));

        topContainer.add(titlePanel);

        // ================= TABLE =================

        DSTable = new JTable();

        DSTable.setModel(
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Mã HD",
                                "Khách hàng",
                                "SĐT",
                                "Ngày lập",
                                "Nhân viên",
                                "Tổng tiền",
                                "Trạng thái",
                                "Thao tác"
                        }
                ) {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return columnIndex == 7
                                ? Icon.class
                                : String.class;
                    }

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                }
        );

        JScrollPane scrollPane = new JScrollPane(DSTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 220, 220)
                )
        );

        add(topContainer, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel makeFilterGroup(String label) {

        JPanel p = new JPanel();

        p.setLayout(
                new BoxLayout(p, BoxLayout.Y_AXIS)
        );

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

        txtSearch.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        filter();
                    }

                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        filter();
                    }

                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        filter();
                    }

                    private void filter() {

                        String keyword = txtSearch.getText();

                        if (keyword.equals("Nhập từ khóa...")) {
                            keyword = "";
                        }

                        sorter.setRowFilter(
                                RowFilter.regexFilter(
                                        "(?i)" + keyword
                                )
                        );
                    }
                }
        );
    }

    // ================= FILTER STATUS =================

    private void initFilterStatus() {

        cbStatus.addActionListener(e -> {

            String status =
                    cbStatus.getSelectedItem().toString();

            if (status.equals("Tất cả")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(
                        RowFilter.regexFilter(status, 6)
                );
            }
        });
    }

    // ================= FILTER DATE =================

    private void initFilterDate() {

    javax.swing.event.DocumentListener listener =
            new javax.swing.event.DocumentListener() {

        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            filterDate();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            filterDate();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            filterDate();
        }

        private void filterDate() {

            try {

                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("dd/MM/yyyy");

                java.util.Date fromDate =
                        sdf.parse(txtFrom.getText().trim());

                java.util.Date toDate =
                        sdf.parse(txtTo.getText().trim());

                sorter.setRowFilter(new RowFilter<TableModel, Integer>() {

                    @Override
                    public boolean include(
                            Entry<? extends TableModel,
                            ? extends Integer> entry) {

                        try {

                            String dateStr =
                                    entry.getStringValue(3).split(" ")[0];

                            java.util.Date rowDate =
                                    sdf.parse(dateStr);

                            return !rowDate.before(fromDate)
                                    && !rowDate.after(toDate);

                        } catch (Exception ex) {
                            return true;
                        }
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

    // ================= ACTION =================

    private void initAction() {

        DSTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent evt
                    ) {

                        int row =
                                DSTable.rowAtPoint(evt.getPoint());

                        int col =
                                DSTable.columnAtPoint(evt.getPoint());

                        if (col != 7) return;

                        String maHD =
                                DSTable.getValueAt(row, 0).toString();

                        String tenKH =
                                DSTable.getValueAt(row, 1).toString();

                        String sdt =
                                DSTable.getValueAt(row, 2).toString();

                        String ngay =
                                DSTable.getValueAt(row, 3).toString();

                        String maNV =
                                DSTable.getValueAt(row, 4).toString();

                        String tongTien =
                                DSTable.getValueAt(row, 5).toString();

                        String trangThai =
                                DSTable.getValueAt(row, 6).toString();

                        int modelRow =
                                DSTable.convertRowIndexToModel(row);

                        showInvoiceDialog(
                                maHD,
                                tenKH,
                                sdt,
                                ngay,
                                maNV,
                                tongTien,
                                trangThai,
                                modelRow
                        );
                    }
                }
        );
    }

    // ================= DIALOG =================

    private void showInvoiceDialog(
            String maHD,
            String tenKH,
            String sdt,
            String ngay,
            String maNV,
            String tongTien,
            String trangThai,
            int modelRow
    ) {

        JDialog dialog = new JDialog();

        dialog.setTitle("Chi tiết hóa đơn");

        dialog.setSize(700, 600);

        dialog.setLocationRelativeTo(null);

        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(Color.WHITE);

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 15, 30
                )
        );

        JLabel lblTitle =
                new JLabel("THÔNG TIN HÓA ĐƠN");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        lblTitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(lblTitle, BorderLayout.NORTH);

        JTextArea area = new JTextArea();

        area.setEditable(false);

        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        area.setText(
                "Mã hóa đơn: " + maHD +
                "\nKhách hàng: " + tenKH +
                "\nSĐT: " + sdt +
                "\nNgày lập: " + ngay +
                "\nNhân viên: " + maNV +
                "\nTổng tiền: " + tongTien +
                "\nTrạng thái: " + trangThai
        );

        root.add(new JScrollPane(area), BorderLayout.CENTER);

        JButton btnHuy =
                new JButton("Hủy hóa đơn");

        btnHuy.setBackground(
                new Color(220, 50, 50)
        );

        btnHuy.setForeground(Color.WHITE);

        if (trangThai.equals("Đã hủy")) {

            btnHuy.setEnabled(false);

            btnHuy.setText("Đã hủy");
        }

        btnHuy.addActionListener(e -> {

            int confirm =
                    JOptionPane.showConfirmDialog(
                            dialog,
                            "Bạn có chắc muốn hủy hóa đơn?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok =
                    invoiceDAO.cancelInvoiceAndTickets(maHD);

            if (ok) {

                DefaultTableModel model =
                        (DefaultTableModel)
                                DSTable.getModel();

                model.setValueAt(
                        "Đã hủy",
                        modelRow,
                        6
                );

                btnHuy.setEnabled(false);

                btnHuy.setText("Đã hủy");

                JOptionPane.showMessageDialog(
                        dialog,
                        "Hủy hóa đơn thành công!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Hủy thất bại!"
                );
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        south.add(btnHuy);

        root.add(south, BorderLayout.SOUTH);

        dialog.setContentPane(root);

        dialog.setVisible(true);
    }

    // ================= STYLE INPUT =================

    private void styleInput(
            JTextField txt,
            String placeholder
    ) {

        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txt.setPreferredSize(new Dimension(200, 35));

        txt.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 200, 200)
                )
        );

        txt.setText(placeholder);

        txt.setForeground(Color.GRAY);

        txt.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    public void focusGained(
                            java.awt.event.FocusEvent e
                    ) {

                        if (txt.getText().equals(placeholder)) {

                            txt.setText("");

                            txt.setForeground(Color.BLACK);
                        }
                    }

                    public void focusLost(
                            java.awt.event.FocusEvent e
                    ) {

                        if (txt.getText().isEmpty()) {

                            txt.setText(placeholder);

                            txt.setForeground(Color.GRAY);
                        }
                    }
                }
        );
    }

    // ================= TABLE STYLE =================

    public void setTable(JTable table) {

        table.setRowHeight(45);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.setShowGrid(false);

        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();

        header.setBackground(
                new Color(240, 240, 240)
        );

        header.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        header.setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(7)
                .setCellRenderer(
                        new DefaultTableCellRenderer() {

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
                        }
                );

        table.getColumnModel().getColumn(6)
                .setCellRenderer(
                        new DefaultTableCellRenderer() {

                            @Override
                            public Component getTableCellRendererComponent(
                                    JTable table,
                                    Object value,
                                    boolean isSelected,
                                    boolean hasFocus,
                                    int row,
                                    int column
                            ) {

                                Component c =
                                        super.getTableCellRendererComponent(
                                                table,
                                                value,
                                                isSelected,
                                                hasFocus,
                                                row,
                                                column
                                        );

                                if (!isSelected) {

                                    String s = value.toString();

                                    if (s.equals("Đã thanh toán")) {
                                        c.setForeground(
                                                new Color(0, 150, 0)
                                        );
                                    } else if (s.equals("Đã hủy")) {
                                        c.setForeground(
                                                Color.GRAY
                                        );
                                    } else {
                                        c.setForeground(
                                                new Color(220, 50, 50)
                                        );
                                    }
                                }

                                return c;
                            }
                        }
                );
    }
}