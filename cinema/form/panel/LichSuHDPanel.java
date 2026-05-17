package cinema.form.panel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class LichSuHDPanel extends JPanel {

    private JTable DSTable;

    private JTextField txtSearch;
    private JTextField txtFrom;
    private JTextField txtTo;

    private JComboBox<String> cbStatus;

    public LichSuHDPanel() {

        initComponents();

        setTable(DSTable);

        loadData();
    }

    // ================= LOAD DATA =================

    private void loadData() {

        DefaultTableModel model =
                (DefaultTableModel) DSTable.getModel();

        model.setRowCount(0);

        model.addRow(new Object[]{
                "INV001",
                "Nguyễn Văn A",
                "15/05/2026",
                "90.000 đ",
                "Đã thanh toán"
        });

        model.addRow(new Object[]{
                "INV002",
                "Trần Văn B",
                "15/05/2026",
                "120.000 đ",
                "Đã hủy"
        });

        model.addRow(new Object[]{
                "INV003",
                "Lê Văn C",
                "16/05/2026",
                "150.000 đ",
                "Đã thanh toán"
        });

        model.addRow(new Object[]{
                "INV004",
                "Phạm Thị D",
                "16/05/2026",
                "200.000 đ",
                "Hoàn tiền"
        });
    }

    // ================= UI =================

    private void initComponents() {

        setBackground(new Color(245,247,250));

        setLayout(new BorderLayout(0,20));

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        // ================= FILTER =================

        JPanel filterWrapper =
                new JPanel(new BorderLayout());

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

        pSearch.setOpaque(false);

        pSearch.setLayout(
                new BoxLayout(
                        pSearch,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lbSearch =
                new JLabel("Tìm kiếm");

        lbSearch.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txtSearch = new JTextField();

        styleInput(txtSearch, "Nhập mã hóa đơn...");

        pSearch.add(lbSearch);

        pSearch.add(Box.createVerticalStrut(5));

        pSearch.add(txtSearch);

        // ===== FROM =====

        JPanel pFrom = new JPanel();

        pFrom.setOpaque(false);

        pFrom.setLayout(
                new BoxLayout(
                        pFrom,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lbFrom =
                new JLabel("Từ ngày");

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

        pTo.setOpaque(false);

        pTo.setLayout(
                new BoxLayout(
                        pTo,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lbTo =
                new JLabel("Đến ngày");

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

        pStatus.setOpaque(false);

        pStatus.setLayout(
                new BoxLayout(
                        pStatus,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lbStatus =
                new JLabel("Trạng thái");

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
                        "Đã hủy",
                        "Hoàn tiền"
                }
        );

        cbStatus.setPreferredSize(
                new Dimension(200,35)
        );

        pStatus.add(lbStatus);

        pStatus.add(Box.createVerticalStrut(5));

        pStatus.add(cbStatus);

        // ===== ADD =====

        filterPanel.add(pSearch);

        filterPanel.add(pFrom);

        filterPanel.add(pTo);

        filterPanel.add(pStatus);

        filterWrapper.add(filterPanel);

        // ================= TITLE =================

        JLabel title =
                new JLabel("Lịch sử hóa đơn");

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

        DSTable.setModel(
                new DefaultTableModel(
                        new Object[][]{},
                        new String[]{
                                "Mã HD",
                                "Khách hàng",
                                "Ngày lập",
                                "Tổng tiền",
                                "Trạng thái"
                        }
                ){
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ){
                        return false;
                    }
                }
        );

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

    // ================= INPUT STYLE =================

    private void styleInput(
            JTextField txt,
            String placeholder
    ){

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
                new java.awt.event.FocusAdapter(){

                    public void focusGained(
                            java.awt.event.FocusEvent e
                    ){

                        if(txt.getText().equals(placeholder)){

                            txt.setText("");

                            txt.setForeground(Color.BLACK);
                        }
                    }

                    public void focusLost(
                            java.awt.event.FocusEvent e
                    ){

                        if(txt.getText().isEmpty()){

                            txt.setText(placeholder);

                            txt.setForeground(Color.GRAY);
                        }
                    }
                }
        );
    }

    // ================= TABLE STYLE =================

    public void setTable(JTable table){

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
                .setPreferredWidth(120);

        table.getColumnModel().getColumn(1)
                .setPreferredWidth(220);

        table.getColumnModel().getColumn(2)
                .setPreferredWidth(150);

        table.getColumnModel().getColumn(3)
                .setPreferredWidth(150);

        table.getColumnModel().getColumn(4)
                .setPreferredWidth(180);

        // ===== STATUS COLOR =====

        table.getColumnModel().getColumn(4)
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
                            ){

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
                                    else if(status.equals("Đã hủy")){

                                        c.setForeground(
                                                new Color(220,50,50)
                                        );
                                    }
                                    else{

                                        c.setForeground(
                                                new Color(255,140,0)
                                        );
                                    }
                                }

                                return c;
                            }
                        }
                );
    }
}