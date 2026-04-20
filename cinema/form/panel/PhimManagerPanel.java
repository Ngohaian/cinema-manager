package cinema.form.panel;
import javax.swing.table.DefaultTableCellRenderer;

import cinema.models.Movie;


public class PhimManagerPanel extends javax.swing.JPanel {

    public PhimManagerPanel() {
        initComponents();
        setTable(DSPhimTable);
        LoadTableMovie();
    }
    private javax.swing.ImageIcon scaleImage(java.net.URL imagePath, int w, int h) {
        try {
            java.awt.Image img = new javax.swing.ImageIcon(imagePath).getImage();
            java.awt.Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            return new javax.swing.ImageIcon(scaled);
        } catch (Exception e) {
            return null; 
        }
    }
    public void generateRowTable(Movie m){
        var posterURL = getClass().getResource(m.getPoster());
        javax.swing.ImageIcon posterIcon = null;
        var editIcon = getClass().getResource("/cinema/images/edit(black).png");
        if (posterURL != null) {
            posterIcon = scaleImage(posterURL, 30, 45); 
        } else {
            System.out.println("Không tìm thấy ảnh tại: " + m.getPoster());
        }
        
        String multiLineTitle = "<html><body style='width: 400px'>" + m.getTitle() + "</body></html>";
        var model = (javax.swing.table.DefaultTableModel) DSPhimTable.getModel();
        model.addRow(new Object []{
                posterIcon,    
                m.getId(),
                multiLineTitle,
                m.getDuration(),
                m.getGenre(),
                m.getActive(),
                editIcon != null ? new javax.swing.ImageIcon(editIcon) : null
        });
    }
    public void LoadTableMovie(){
        var list = new cinema.dao.MovieDAO().getDSPhim();
        for(Movie m : list){
            generateRowTable(m);
        }
    }          
    public void setTable(javax.swing.JTable Table){
        Table.setRowHeight(60);
        var header = Table.getTableHeader();
        header.setBackground(new java.awt.Color(235,235,235));
        header.setPreferredSize(new java.awt.Dimension(0,40));
        header.setFont(new java.awt.Font("Segoe UI",java.awt.Font.BOLD,15));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        Table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        Table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        Table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        Table.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
        
        Table.getColumnModel().getColumn(0).setPreferredWidth(45); 
        Table.getColumnModel().getColumn(1).setPreferredWidth(50); 
        Table.getColumnModel().getColumn(2).setPreferredWidth(430);
        Table.getColumnModel().getColumn(3).setPreferredWidth(70);
        Table.getColumnModel().getColumn(4).setPreferredWidth(70);
        Table.getColumnModel().getColumn(5).setPreferredWidth(70);
        Table.getColumnModel().getColumn(6).setPreferredWidth(40);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        ThemPhimDialog = new javax.swing.JDialog();
        ThemPhimPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
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
        DSPhimTable = new javax.swing.JTable() {
            @Override
            public Class getColumnClass(int columnIndex) {
                // Cột 0 và 6 là Icon
                if (columnIndex == 0 || columnIndex == 6) {
                    return javax.swing.Icon.class;
                }
                return super.getColumnClass(columnIndex);
            }

        };
        jLabel5 = new javax.swing.JLabel();
        btnThemPhim = new javax.swing.JButton();

        ThemPhimDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ThemPhimDialog.setTitle("Thêm phim mới");
        ThemPhimDialog.setAlwaysOnTop(true);
        ThemPhimDialog.setBackground(new java.awt.Color(248, 250, 252));
        ThemPhimDialog.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ThemPhimDialog.setModal(true);

        ThemPhimPanel.setBackground(new java.awt.Color(248, 250, 252));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel6.setText("Tên phim");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThemPhimPanelLayout = new javax.swing.GroupLayout(ThemPhimPanel);
        ThemPhimPanel.setLayout(ThemPhimPanelLayout);
        ThemPhimPanelLayout.setHorizontalGroup(
            ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        ThemPhimPanelLayout.setVerticalGroup(
            ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(270, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ThemPhimDialogLayout = new javax.swing.GroupLayout(ThemPhimDialog.getContentPane());
        ThemPhimDialog.getContentPane().setLayout(ThemPhimDialogLayout);
        ThemPhimDialogLayout.setHorizontalGroup(
            ThemPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ThemPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ThemPhimDialogLayout.setVerticalGroup(
            ThemPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ThemPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(248, 250, 252));
        setPreferredSize(new java.awt.Dimension(759, 779));

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
        cbTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hoạt hình", "Gia đình", "Hài", "Phiêu lưu", "Hành động", "Kinh dị", "Lãng mạn" }));

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
                "Ảnh", "Mã phim", "Tiêu đề", "Thời lượng", "Thể loại", "Trạng thái", "Thao tác"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
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
            DSPhimTable.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout CardPhimPanelLayout = new javax.swing.GroupLayout(CardPhimPanel);
        CardPhimPanel.setLayout(CardPhimPanelLayout);
        CardPhimPanelLayout.setHorizontalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        CardPhimPanelLayout.setVerticalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CardPhimPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Danh sách phim");

        btnThemPhim.setBackground(new java.awt.Color(0, 146, 255));
        btnThemPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemPhim.setForeground(new java.awt.Color(255, 255, 255));
        btnThemPhim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/add.png"))); // NOI18N
        btnThemPhim.setText("Thêm phim mới");
        btnThemPhim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThemPhim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThemPhimMouseClicked(evt);
            }
        });
        btnThemPhim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemPhimActionPerformed(evt);
            }
        });

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
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void btnThemPhimActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void btnThemPhimMouseClicked(java.awt.event.MouseEvent evt) {                                         

        ThemPhimDialog.setLocationRelativeTo(null); 
        ThemPhimDialog.setVisible(true);
    }                                        

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           
   
   
    // Variables declaration - do not modify                     
    private javax.swing.JPanel CardPhimPanel;
    private javax.swing.JTable DSPhimTable;
    private javax.swing.JPanel PhanLoaiPanel;
    private javax.swing.JDialog ThemPhimDialog;
    private javax.swing.JPanel ThemPhimPanel;
    private javax.swing.JButton btnThemPhim;
    private javax.swing.JComboBox<String> cbTheLoai;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lMaxThoiGianPhim;
    private javax.swing.JLabel lMinThoiGianPhim;
    private javax.swing.JTextField txtTimPhim;
    // End of variables declaration                   
}
