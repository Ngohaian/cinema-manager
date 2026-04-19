
package cinema.form.panel;
import javax.swing.table.DefaultTableCellRenderer;

import cinema.models.Movie;
public class PhimManagerPanel extends javax.swing.JPanel {
    public void generateRowTable(Movie m){
        var posterIcon = getClass().getResource(m.getPoster());
        var editIcon = getClass().getResource("/cinema/images/edit(black).png");
        var model = (javax.swing.table.DefaultTableModel) DSPhimTable.getModel();
        model.addRow(new Object []{
                m.getId(),
                posterIcon != null ? new javax.swing.ImageIcon(posterIcon) : null,
                m.getTitle(),
                m.getGenre(),
                m.isActive(),
                editIcon != null ? new javax.swing.ImageIcon(editIcon) : null
        });
    }
    public void LoadTableMovie(){
        var list = new cinema.dao.MovieDAO().getDSPhim();
        for(Movie m : list){
            generateRowTable(m);
        }
    }
    public PhimManagerPanel() {
        initComponents();
        setTable(DSPhimTable);
        LoadTableMovie();
    }

                         
    private void initComponents() {

        PhanLoaiPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimPhim = new javax.swing.JTextField();
        jSlider1 = new javax.swing.JSlider();
        cbTrangThai = new javax.swing.JComboBox<>();
        cbTheLoai = new javax.swing.JComboBox<>();
        lMinThoiGianPhim = new javax.swing.JLabel();
        lMaxThoiGianPhim = new javax.swing.JLabel();
        CardPhimPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DSPhimTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnThemPhim = new javax.swing.JButton();

        setBackground(new java.awt.Color(248, 250, 252));
        setPreferredSize(new java.awt.Dimension(759, 770));

        PhanLoaiPanel.setBackground(new java.awt.Color(255, 255, 255));
        PhanLoaiPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel1.setText("Tìm phim");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel2.setText("Trạng thái");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel3.setText("Thể loại");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setText("Thời lượng tối đa");

        txtTimPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimPhim.setToolTipText("Nhập vào phim bạn muốn tìm kiếm");

        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang chiếu", "Sắp chiếu", "Đã dừng chiếu" }));

        cbTheLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lMinThoiGianPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        lMaxThoiGianPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lMaxThoiGianPhim.setForeground(new java.awt.Color(102, 102, 102));
        lMaxThoiGianPhim.setText("200");

        javax.swing.GroupLayout PhanLoaiPanelLayout = new javax.swing.GroupLayout(PhanLoaiPanel);
        PhanLoaiPanel.setLayout(PhanLoaiPanelLayout);
        PhanLoaiPanelLayout.setHorizontalGroup(
            PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(115, 115, 115))
                    .addComponent(txtTimPhim))
                .addGap(39, 39, 39)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(90, 90, 90))
                    .addComponent(cbTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(103, 103, 103))
                    .addComponent(cbTheLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(56, 56, 56)
                        .addComponent(lMaxThoiGianPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PhanLoaiPanelLayout.setVerticalGroup(
            PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addComponent(txtTimPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(9, 9, 9)
                        .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(9, 9, 9)
                        .addComponent(cbTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lMaxThoiGianPhim)))
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        CardPhimPanel.setBackground(new java.awt.Color(248, 250, 252));

        DSPhimTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DSPhimTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                
            },
            new String [] {
                "Mã phim", "Ảnh", "Tiêu đề", "Thể loại", "Trạng thái", "Thao tác"
            }
        ) {
            
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                if (columnIndex == 1 || columnIndex == 5) {
                    return javax.swing.Icon.class;
                }
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        DSPhimTable.setShowGrid(false);
        DSPhimTable.setShowHorizontalLines(true);
        DSPhimTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(DSPhimTable);
        if (DSPhimTable.getColumnModel().getColumnCount() > 0) {
            DSPhimTable.getColumnModel().getColumn(0).setResizable(false);
            DSPhimTable.getColumnModel().getColumn(1).setResizable(false);
            DSPhimTable.getColumnModel().getColumn(2).setResizable(false);
            DSPhimTable.getColumnModel().getColumn(3).setResizable(false);
            DSPhimTable.getColumnModel().getColumn(4).setResizable(false);
            DSPhimTable.getColumnModel().getColumn(5).setResizable(false);
        }
        
        javax.swing.GroupLayout CardPhimPanelLayout = new javax.swing.GroupLayout(CardPhimPanel);
        CardPhimPanel.setLayout(CardPhimPanelLayout);
        CardPhimPanelLayout.setHorizontalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        CardPhimPanelLayout.setVerticalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Danh sách phim");

        btnThemPhim.setBackground(new java.awt.Color(0, 146, 255));
        btnThemPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemPhim.setForeground(new java.awt.Color(255, 255, 255));
        btnThemPhim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/add.png"))); // NOI18N
        btnThemPhim.setText("Thêm phim mới");
        btnThemPhim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(524, 524, 524)
                        .addComponent(btnThemPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(CardPhimPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnThemPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(CardPhimPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
        ));
    }// </editor-fold>                        
    public void setTable(javax.swing.JTable Table){
       Table.setRowHeight(40);
        var header = Table.getTableHeader();
        header.setBackground(new java.awt.Color(235,235,235));
        header.setPreferredSize(new java.awt.Dimension(0,40));
        header.setFont(new java.awt.Font("Segoe UI",java.awt.Font.BOLD,15));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        Table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        Table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        Table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
   }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel CardPhimPanel;
    private javax.swing.JTable DSPhimTable;
    private javax.swing.JPanel PhanLoaiPanel;
    private javax.swing.JButton btnThemPhim;
    private javax.swing.JComboBox<String> cbTheLoai;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lMaxThoiGianPhim;
    private javax.swing.JLabel lMinThoiGianPhim;
    private javax.swing.JTextField txtTimPhim;
    // End of variables declaration                   
}
