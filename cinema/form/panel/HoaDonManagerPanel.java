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
    }

    // ================= LOAD DATA =================

    public void generateRowTable(Invoice inv) {

        String date = "Chưa có ngày";

        if (inv.getInvoiceDate() != null) {
            date = new SimpleDateFormat("dd/MM/yyyy")
                    .format(inv.getInvoiceDate());
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

        DefaultTableModel model =
                (DefaultTableModel) DSTable.getModel();

        model.setRowCount(0);

        List<Invoice> list = invoiceDAO.getAll();

        if (list == null || list.isEmpty()) {

            model.addRow(new Object[]{
                    "INV001",
                    "Nguyễn Văn A",
                    "0912345678",
                    "01/04/2026",
                    "EMP01",
                    "90000 đ",
                    "Đã thanh toán",
                    editIcon()
            });

            model.addRow(new Object[]{
                    "INV002",
                    "Nguyễn Văn B",
                    "0987654321",
                    "02/04/2026",
                    "EMP02",
                    "120000 đ",
                    "Chưa thanh toán",
                    editIcon()
            });

            model.addRow(new Object[]{
                    "INV003",
                    "Trần Văn C",
                    "0909123456",
                    "03/04/2026",
                    "EMP01",
                    "150000 đ",
                    "Đã thanh toán",
                    editIcon()
            });

            model.addRow(new Object[]{
                    "INV004",
                    "Lê Thị D",
                    "0977123456",
                    "04/04/2026",
                    "EMP03",
                    "80000 đ",
                    "Chưa thanh toán",
                    editIcon()
            });

            return;
        }

        for (Invoice inv : list) {
            generateRowTable(inv);
        }
    }

    private ImageIcon editIcon() {

        var url =
                getClass().getResource(
                        "/cinema/images/edit(black).png"
                );

        return url != null
                ? new ImageIcon(url)
                : null;
    }

    // ================= UI =================

    private void initComponents() {

        setBackground(new Color(245, 247, 250));

        setLayout(new BorderLayout(0, 20));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        // ================= FILTER PANEL =================

        JPanel filterWrapper = new JPanel(new BorderLayout());

        filterWrapper.setBackground(Color.WHITE);

        filterWrapper.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220,220,220)
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JPanel filterPanel =
                new JPanel(new GridLayout(1,4,20,0));

        filterPanel.setOpaque(false);

        // ===== SEARCH =====

        JPanel pSearch = new JPanel();

        pSearch.setLayout(
                new BoxLayout(
                        pSearch,
                        BoxLayout.Y_AXIS
                )
        );

        pSearch.setOpaque(false);

        JLabel lbSearch = new JLabel("Tìm kiếm");

        lbSearch.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtSearch = new JTextField();

        styleInput(txtSearch, "Nhập từ khóa...");

        pSearch.add(lbSearch);

        pSearch.add(Box.createVerticalStrut(5));

        pSearch.add(txtSearch);

        // ===== FROM =====

        JPanel pFrom = new JPanel();

        pFrom.setLayout(
                new BoxLayout(
                        pFrom,
                        BoxLayout.Y_AXIS
                )
        );

        pFrom.setOpaque(false);

        JLabel lbFrom = new JLabel("Từ ngày");

        lbFrom.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtFrom = new JTextField();

        styleInput(txtFrom, "dd/MM/yyyy");

        pFrom.add(lbFrom);

        pFrom.add(Box.createVerticalStrut(5));

        pFrom.add(txtFrom);

        // ===== TO =====

        JPanel pTo = new JPanel();

        pTo.setLayout(
                new BoxLayout(
                        pTo,
                        BoxLayout.Y_AXIS
                )
        );

        pTo.setOpaque(false);

        JLabel lbTo = new JLabel("Đến ngày");

        lbTo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtTo = new JTextField();

        styleInput(txtTo, "dd/MM/yyyy");

        pTo.add(lbTo);

        pTo.add(Box.createVerticalStrut(5));

        pTo.add(txtTo);

        // ===== STATUS =====

        JPanel pStatus = new JPanel();

        pStatus.setLayout(
                new BoxLayout(
                        pStatus,
                        BoxLayout.Y_AXIS
                )
        );

        pStatus.setOpaque(false);

        JLabel lbStatus = new JLabel("Trạng thái");

        lbStatus.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cbStatus = new JComboBox<>(
                new String[]{
                        "Tất cả",
                        "Đã thanh toán",
                        "Chưa thanh toán"
                }
        );

        cbStatus.setPreferredSize(
                new Dimension(200,35)
        );

        pStatus.add(lbStatus);

        pStatus.add(Box.createVerticalStrut(5));

        pStatus.add(cbStatus);

        // ADD

        filterPanel.add(pSearch);

        filterPanel.add(pFrom);

        filterPanel.add(pTo);

        filterPanel.add(pStatus);

        filterWrapper.add(filterPanel);

        // ================= TITLE =================

        JLabel title =
                new JLabel("Danh sách hóa đơn");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        JPanel titlePanel =
                new JPanel(new BorderLayout());

        titlePanel.setOpaque(false);

        titlePanel.add(title, BorderLayout.WEST);

        // ================= TOP =================

        JPanel topContainer = new JPanel();

        topContainer.setOpaque(false);

        topContainer.setLayout(
                new BoxLayout(
                        topContainer,
                        BoxLayout.Y_AXIS
                )
        );

        topContainer.add(filterWrapper);

        topContainer.add(Box.createVerticalStrut(25));

        topContainer.add(titlePanel);

        // ================= TABLE =================

        DSTable = new JTable();

        DSTable.setModel(new DefaultTableModel(
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

                if(columnIndex == 7){
                    return Icon.class;
                }

                return String.class;
            }

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        });

        JScrollPane scrollPane =
                new JScrollPane(DSTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220)
                )
        );

        add(topContainer, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= SEARCH =================

    private void initSearch() {

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

                        String keyword =
                                txtSearch.getText();

                        if(keyword.equals("Nhập từ khóa...")){
                            keyword = "";
                        }

                        TableRowSorter<TableModel> sorter =
                                new TableRowSorter<>(
                                        DSTable.getModel()
                                );

                        DSTable.setRowSorter(sorter);

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

            String selected =
                    cbStatus.getSelectedItem().toString();

            TableRowSorter<TableModel> sorter =
                    new TableRowSorter<>(
                            DSTable.getModel()
                    );

            DSTable.setRowSorter(sorter);

            if(selected.equals("Tất cả")){
                sorter.setRowFilter(null);
            }
            else{
                sorter.setRowFilter(
                        RowFilter.regexFilter(
                                selected,
                                6
                        )
                );
            }
        });
    }

    // ================= INPUT STYLE =================

    private void styleInput(
            JTextField txt,
            String placeholder
    ) {

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txt.setPreferredSize(
                new Dimension(200,35)
        );

        txt.setMaximumSize(
                new Dimension(Integer.MAX_VALUE,35)
        );

        txt.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200,200,200)
                )
        );

        txt.setText(placeholder);

        txt.setForeground(Color.GRAY);

        txt.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    public void focusGained(
                            java.awt.event.FocusEvent e
                    ) {

                        if(txt.getText().equals(placeholder)){

                            txt.setText("");

                            txt.setForeground(Color.BLACK);
                        }
                    }

                    public void focusLost(
                            java.awt.event.FocusEvent e
                    ) {

                        if(txt.getText().isEmpty()){

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
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.setShowGrid(false);

        table.setShowHorizontalLines(true);

        JTableHeader header =
                table.getTableHeader();

        header.setBackground(
                new Color(240,240,240)
        );

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        header.setPreferredSize(
                new Dimension(0,40)
        );

        table.getColumnModel().getColumn(0)
                .setPreferredWidth(80);

        table.getColumnModel().getColumn(1)
                .setPreferredWidth(180);

        table.getColumnModel().getColumn(2)
                .setPreferredWidth(120);

        table.getColumnModel().getColumn(3)
                .setPreferredWidth(120);

        table.getColumnModel().getColumn(4)
                .setPreferredWidth(100);

        table.getColumnModel().getColumn(5)
                .setPreferredWidth(120);

        table.getColumnModel().getColumn(6)
                .setPreferredWidth(150);

        table.getColumnModel().getColumn(7)
                .setMaxWidth(70);

        // ===== ICON =====

        table.getColumnModel().getColumn(7)
                .setCellRenderer(
                        new DefaultTableCellRenderer(){

                            @Override
                            protected void setValue(
                                    Object value
                            ) {

                                if(value instanceof Icon){

                                    setIcon((Icon) value);

                                    setText("");

                                    setHorizontalAlignment(CENTER);
                                }
                                else{
                                    super.setValue(value);
                                }
                            }
                        }
                );

        // ===== STATUS COLOR =====

        table.getColumnModel().getColumn(6)
                .setCellRenderer(
                        new DefaultTableCellRenderer(){

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

                                if(!isSelected){

                                    String status =
                                            value.toString();

                                    if(status.equals("Đã thanh toán")){

                                        c.setForeground(
                                                new Color(0,150,0)
                                        );
                                    }
                                    else{

                                        c.setForeground(
                                                new Color(220,50,50)
                                        );
                                    }
                                }

                                return c;
                            }
                        }
                );
    }
}