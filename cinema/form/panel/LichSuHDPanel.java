package cinema.form.panel;

import cinema.dao.InvoiceDAO;
import cinema.models.Invoice;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LichSuHDPanel extends JPanel {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    private JTable DSTable;
    private TableRowSorter<TableModel> sorter;

    private JTextField txtSearch;
    private JTextField txtFrom;
    private JTextField txtTo;
    private JComboBox<String> cbStatus;

    public LichSuHDPanel() {
        initComponents();
        setTable(DSTable);
        loadData();
        initSearch();
        initFilterStatus();
        initFilterDate();
    }

    // ================= LOAD DATA =================

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) DSTable.getModel();
        model.setRowCount(0);

        List<Invoice> list = invoiceDAO.getAll();
        if (list == null || list.isEmpty()) return;

        for (Invoice inv : list) {
            String date = "Chưa có ngày";
            if (inv.getInvoiceDate() != null) {
                date = inv.getInvoiceDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            model.addRow(new Object[]{
                    inv.getInvoiceId(),
                    inv.getCustomerId(),
                    date,
                    String.format("%,.0f đ", inv.getTotalAmount()),
                    inv.getStatus() != null ? inv.getStatus() : "Đã thanh toán"
            });
        }
    }

    // ================= SEARCH =================

    private void initSearch() {
        sorter = new TableRowSorter<>(DSTable.getModel());
        DSTable.setRowSorter(sorter);

        txtSearch.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

                    private void filter() {
                        String keyword = txtSearch.getText().trim();
                        if (keyword.equals("Nhập mã hóa đơn...")) keyword = "";
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 0));
                    }
                });
    }

    // ================= FILTER STATUS =================

    private void initFilterStatus() {
        cbStatus.addActionListener(e -> {
            String selected = cbStatus.getSelectedItem().toString();
            if (selected.equals("Tất cả")) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter(selected, 4));
        });
    }

    // ================= FILTER DATE =================

    private void initFilterDate() {
        javax.swing.event.DocumentListener listener =
                new javax.swing.event.DocumentListener() {
                    public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterDate(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterDate(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { filterDate(); }

                    private void filterDate() {
                        try {
                            java.text.SimpleDateFormat sdf =
                                    new java.text.SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date fromDate = sdf.parse(txtFrom.getText().trim());
                            java.util.Date toDate   = sdf.parse(txtTo.getText().trim());

                            sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                                @Override
                                public boolean include(
                                        Entry<? extends TableModel, ? extends Integer> entry) {
                                    try {
                                        java.util.Date rowDate =
                                                sdf.parse(entry.getStringValue(2).split(" ")[0]);
                                        return !rowDate.before(fromDate)
                                                && !rowDate.after(toDate);
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

    // ================= UI =================

    private void initComponents() {
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ===== FILTER =====
        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setBackground(Color.WHITE);
        filterWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel filterPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        filterPanel.setOpaque(false);

        JPanel pSearch = makeFilterGroup("Tìm kiếm");
        txtSearch = new JTextField();
        styleInput(txtSearch, "Nhập mã hóa đơn...");
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
        cbStatus = new JComboBox<>(new String[]{
                "Tất cả", "Đã thanh toán", "Đã hủy", "Hoàn tiền"
        });
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setPreferredSize(new Dimension(200, 35));
        cbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pStatus.add(cbStatus);

        filterPanel.add(pSearch);
        filterPanel.add(pFrom);
        filterPanel.add(pTo);
        filterPanel.add(pStatus);
        filterWrapper.add(filterPanel);

        // ===== TITLE =====
        JLabel title = new JLabel("Lịch sử hóa đơn");
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

        // ===== TABLE =====
        DSTable = new JTable();
        DSTable.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã HD", "Khách hàng", "Ngày lập", "Tổng tiền", "Trạng thái"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
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

    // ================= TABLE STYLE =================

    public void setTable(JTable table) {
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(235, 235, 235));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(232, 240, 254));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(50, 50, 70));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Mã HD | Khách hàng | Ngày lập | Tổng tiền | Trạng thái
        int[] widths = {120, 220, 150, 150, 180};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Căn giữa: Mã HD, Ngày lập, Tổng tiền
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 2, 3}) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerRender);
        }

        // Màu + căn giữa cột Trạng thái
        table.getColumnModel().getColumn(4).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
                        Component c = super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        setHorizontalAlignment(SwingConstants.CENTER);
                        if (!isSelected && value != null) {
                            String status = value.toString();
                            if (status.equals("Đã thanh toán"))
                                c.setForeground(new Color(0, 150, 0));
                            else if (status.equals("Đã hủy"))
                                c.setForeground(new Color(220, 50, 50));
                            else
                                c.setForeground(new Color(255, 140, 0));
                        }
                        return c;
                    }
                });
    }
}