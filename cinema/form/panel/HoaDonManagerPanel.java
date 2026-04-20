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

    public HoaDonManagerPanel() {
        initComponents();
        setTable(DSTable);
        loadTableInvoice();
        initSearch();
        initAction();
    }

    // ================= LOAD DATA =================
    public void generateRowTable(Invoice inv){
        var editIcon = getClass().getResource("/cinema/images/edit(black).png");

        DefaultTableModel model = (DefaultTableModel) DSTable.getModel();

        model.addRow(new Object[]{
                inv.getInvoiceId(),
                "Nguyễn Văn A",
                "0912345678",
                inv.getInvoiceDate() != null ? inv.getInvoiceDate().toString() : "Chưa có ngày",
                "EMP001",
                String.format("%.0f đ", inv.getTotalAmount()),
                "Đã thanh toán",
                editIcon != null ? new ImageIcon(editIcon) : null
        });
    }

    public void loadTableInvoice(){
        DefaultTableModel model = (DefaultTableModel) DSTable.getModel();
        model.setRowCount(0);

        List<Invoice> list = invoiceDAO.getAll();
        for(Invoice inv : list){
            generateRowTable(inv);
        }
    }

    // ================= SEARCH =================
    private void initSearch(){
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e){ filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e){ filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e){ filter(); }

            private void filter(){
                String keyword = txtSearch.getText();

                TableRowSorter<TableModel> sorter = new TableRowSorter<>(DSTable.getModel());
                DSTable.setRowSorter(sorter);

                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        });
    }

    // ================= ACTION =================
    private void initAction(){
        // CLICK ICON EDIT
        DSTable.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = DSTable.getSelectedRow();
                int col = DSTable.getSelectedColumn();

                if(col == 7){
                    String id = DSTable.getValueAt(row,0).toString();
                    JOptionPane.showMessageDialog(null,"Sửa hóa đơn: " + id);
                }
            }
        });

        // BUTTON
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"Mở form thêm hóa đơn");
        });
    }

    // ================= TABLE STYLE =================
    public void setTable(JTable table){
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI",0,14));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(235,235,235));
        header.setFont(new Font("Segoe UI",Font.BOLD,15));
        header.setPreferredSize(new Dimension(0,40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(center);

        table.getColumnModel().getColumn(7).setMaxWidth(40);
    }

    // ================= UI =================
    private void initComponents() {

        setBackground(new Color(248,250,252));

        // ===== FILTER PANEL =====
        filterPanel = new JPanel();
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(204,204,204)));

        txtSearch = new JTextField();
        txtFrom = new JTextField();
        txtTo = new JTextField();

        txtSearch.setFont(new Font("Segoe UI",0,14));
        txtSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        txtFrom.setFont(new Font("Segoe UI",0,14));
        txtFrom.setBorder(BorderFactory.createTitledBorder("Từ ngày"));

        txtTo.setFont(new Font("Segoe UI",0,14));
        txtTo.setBorder(BorderFactory.createTitledBorder("Đến ngày"));

        GroupLayout fp = new GroupLayout(filterPanel);
        filterPanel.setLayout(fp);

        fp.setHorizontalGroup(
            fp.createSequentialGroup()
                .addGap(30)
                .addComponent(txtSearch,300,300,300)
                .addGap(30)
                .addComponent(txtFrom,150,150,150)
                .addGap(20)
                .addComponent(txtTo,150,150,150)
                .addGap(30)
        );

        fp.setVerticalGroup(
            fp.createSequentialGroup()
                .addGap(20)
                .addGroup(fp.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(txtSearch,30,30,30)
                    .addComponent(txtFrom,30,30,30)
                    .addComponent(txtTo,30,30,30)
                )
                .addGap(20)
        );

        // ===== TABLE =====
        DSTable = new JTable();

        DSTable.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Mã HD","Khách hàng","SĐT","Ngày lập","Nhân viên","Tổng tiền","Trạng thái",""
            }
        ){
            public Class<?> getColumnClass(int col){
                if(col == 7) return Icon.class;
                return String.class;
            }
            public boolean isCellEditable(int r,int c){ return false; }
        });

        scrollPane = new JScrollPane(DSTable);

        // ===== TITLE =====
        title = new JLabel("Danh sách hóa đơn");
        title.setFont(new Font("Segoe UI",0,24));

        // ===== BUTTON =====
        btnAdd = new JButton("Thêm hóa đơn");
        btnAdd.setBackground(new Color(0,146,255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI",Font.BOLD,14));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setContentAreaFilled(true);

        // ===== MAIN LAYOUT =====
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGap(30)
                .addGroup(layout.createParallelGroup()
                    .addComponent(filterPanel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(title)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 300, Short.MAX_VALUE)
                        .addComponent(btnAdd,210,210,210)
                    )
                    .addComponent(scrollPane)
                )
                .addGap(30)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(40)
                .addComponent(filterPanel,100,100,100)
                .addGap(50)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(title)
                    .addComponent(btnAdd,40,40,40)
                )
                .addGap(20)
                .addComponent(scrollPane,400,400,400)
                .addGap(30)
        );
    }

    // ================= VARIABLES =================
    private JPanel filterPanel;
    private JScrollPane scrollPane;
    private JTable DSTable;

    private JTextField txtSearch;
    private JTextField txtFrom;
    private JTextField txtTo;

    private JLabel title;
    private JButton btnAdd;
}
