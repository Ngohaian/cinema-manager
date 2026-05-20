package cinema.form.panel;
import cinema.dao.MovieDAO;
import cinema.enums.GenreType;
import static cinema.enums.GenreType.getNameGenreType;
import cinema.enums.MovieStatus;
import static cinema.enums.MovieStatus.getNameMovieStatus;
import javax.swing.table.DefaultTableCellRenderer;

import cinema.models.Movie;

public class PhimManagerPanel extends javax.swing.JPanel {
    private MovieDAO movieDao = new MovieDAO();
    private java.util.List<Movie> list = movieDao.getDSPhim();
    public PhimManagerPanel() {
        initComponents();
        setTable(DSPhimTable);
        cbTheLoai.addItem("Tất cả");
        setCBGenre(cbTheLoai);
        cbTrangThai.addItem("Tất cả");
        setCBStatus(cbTrangThai);
        setCBGenre(cbThemTheLoai);
        setCBStatus(cbThemTrangThai);
        setCBGenre(cbSuaTheLoai);
        setCBStatus(cbSuaTrangThai);
        setjSlider();
        LoadTableMovie(list);
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
                getNameGenreType(m.getGenre()),
                getNameMovieStatus(m.getActive()),
                editIcon != null ? new javax.swing.ImageIcon(editIcon) : null
        });
    }
    public void LoadTableMovie(java.util.List<Movie> ds){
        var model = (javax.swing.table.DefaultTableModel) DSPhimTable.getModel();
        model.setRowCount(0);
        for(Movie m : ds){
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
        Table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int column = DSPhimTable.columnAtPoint(e.getPoint());
                if (column == 6) {
                    DSPhimTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                } else {
                    DSPhimTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            } 
        });
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = DSPhimTable.rowAtPoint(e.getPoint());
                int col = DSPhimTable.columnAtPoint(e.getPoint());
                
                String movieId = Table.getValueAt(row,1).toString();
                Movie m = movieDao.getById(movieId);
                if (col == 6) {
                    setSuaPhimDialog(m);
                }
            }
        });
    }
    private javax.swing.JComboBox<String> setCBGenre(javax.swing.JComboBox<String> ComboBox){
        for(GenreType g : GenreType.values()){
            ComboBox.addItem(getNameGenreType(g));
        }
        return ComboBox;
    }
    private javax.swing.JComboBox<String> setCBStatus(javax.swing.JComboBox<String> ComboBox){
        for(MovieStatus g : MovieStatus.values()){
            ComboBox.addItem(getNameMovieStatus(g));
        }
        return ComboBox;
    }

    private void setjSlider(){
        int max = movieDao.getMaxDuration();
        int min = movieDao.getMinDuration();
        jSThoiLuongToiDa.setMaximum(max);
        jSThoiLuongToiDa.setMinimum(min);
        jSThoiLuongToiDa.setValue(max);
        lMaxThoiGianPhim.setText(String.valueOf(max));
        jSThoiLuongToiDa.addChangeListener(e -> {
            lMaxThoiGianPhim.setText(String.valueOf(jSThoiLuongToiDa.getValue()));
            LoadTableMovie(applyFilters());
        });
    }
    private void setSuaPhimDialog(Movie m){
        txtSuaMaPhim.setText(m.getId());
        txtSuaTenPhim.setText(m.getTitle());
        txtSuaThoiLuong.setText(String.valueOf(String.valueOf(m.getDuration())));
        cbSuaTheLoai.setSelectedItem(getNameGenreType(m.getGenre()));
        cbSuaTrangThai.setSelectedItem(getNameMovieStatus(m.getActive()));
        txtSuaURLAnh.setText(m.getPoster());
        SuaPhimDialog.setSize(530, 520);
        SuaPhimDialog.setResizable(false);
        SuaPhimDialog.setLocationRelativeTo(null); 
        SuaPhimDialog.setVisible(true);
    }
    private java.util.List<Movie> applyFilters() {
        String searchTitle = txtTimPhim.getText().trim().toLowerCase();
        int statusIdx = cbTrangThai.getSelectedIndex();
        int genreIdx = cbTheLoai.getSelectedIndex();
        int maxDuration = jSThoiLuongToiDa.getValue();
        java.util.List<Movie> filteredList = movieDao.searchMovies(searchTitle, statusIdx, genreIdx, maxDuration);
        return filteredList;
    }
    private void resetThemPhim(){
        String maPhim = movieDao.getNextMovieID();
        txtThemMaPhim.setText(maPhim);
        txtThemTenPhim.setText("");
        txtThemThoiLuong.setText("");
        txtURLThemAnh.setText("");
        cbThemTrangThai.setSelectedIndex(0);
        cbThemTheLoai.setSelectedIndex(0);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        ThemPhimDialog = new javax.swing.JDialog();
        ThemPhimPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtThemTenPhim = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbThemTrangThai = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtURLThemAnh = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbThemTheLoai = new javax.swing.JComboBox<>();
        btnThemAnh = new javax.swing.JButton();
        txtThemMaPhim = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txtThemThoiLuong = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ThemAnhDialog = new javax.swing.JFileChooser();
        SuaPhimDialog = new javax.swing.JDialog();
        SuaPhimPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtSuaTenPhim = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cbSuaTrangThai = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtSuaURLAnh = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbSuaTheLoai = new javax.swing.JComboBox<>();
        btnSuaAnh = new javax.swing.JButton();
        txtSuaThoiLuong = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        txtSuaMaPhim = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        PhanLoaiPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimPhim = new javax.swing.JTextField();
        jSThoiLuongToiDa = new javax.swing.JSlider();
        cbTrangThai = new javax.swing.JComboBox<>();
        cbTheLoai = new javax.swing.JComboBox<>();
        lMinThoiGianPhim = new javax.swing.JLabel();
        lMaxThoiGianPhim = new javax.swing.JLabel();
        CardPhimPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DSPhimTable = new javax.swing.JTable() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
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
        ThemPhimDialog.setBackground(new java.awt.Color(248, 250, 252));
        ThemPhimDialog.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ThemPhimDialog.setModal(true);

        ThemPhimPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtThemTenPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel7.setText("Tên phim");

        cbThemTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel8.setText("Thời lượng");

        txtURLThemAnh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel9.setText("Thể loại");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel10.setText("Trạng thái");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel11.setText("URL ảnh");

        cbThemTheLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnThemAnh.setBackground(new java.awt.Color(0, 146, 255));
        btnThemAnh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThemAnh.setForeground(new java.awt.Color(255, 255, 255));
        btnThemAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/add.png"))); // NOI18N
        btnThemAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThemAnhMouseClicked(evt);
            }
        });

        txtThemMaPhim.setEditable(false);
        txtThemMaPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton1.setBackground(new java.awt.Color(0, 146, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/add.png"))); // NOI18N
        jButton1.setText("Thêm mới");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtThemThoiLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel18.setText("Mã phim");

        javax.swing.GroupLayout ThemPhimPanelLayout = new javax.swing.GroupLayout(ThemPhimPanel);
        ThemPhimPanel.setLayout(ThemPhimPanelLayout);
        ThemPhimPanelLayout.setHorizontalGroup(
            ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addGap(400, 400, 400))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ThemPhimPanelLayout.createSequentialGroup()
                        .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ThemPhimPanelLayout.createSequentialGroup()
                                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(cbThemTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                                        .addComponent(txtURLThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(btnThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel11)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ThemPhimPanelLayout.createSequentialGroup()
                                .addGap(450, 450, 450)
                                .addComponent(jLabel6)
                                .addGap(0, 25, Short.MAX_VALUE)))
                        .addGap(30, 30, 30))
                    .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ThemPhimPanelLayout.createSequentialGroup()
                        .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtThemTenPhim, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtThemMaPhim, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtThemThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(cbThemTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(30, 30, 30))))
        );
        ThemPhimPanelLayout.setVerticalGroup(
            ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThemPhimPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtThemMaPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(6, 6, 6)
                .addComponent(txtThemTenPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(20, 20, 20)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbThemTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThemThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ThemPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbThemTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtURLThemAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout ThemPhimDialogLayout = new javax.swing.GroupLayout(ThemPhimDialog.getContentPane());
        ThemPhimDialog.getContentPane().setLayout(ThemPhimDialogLayout);
        ThemPhimDialogLayout.setHorizontalGroup(
            ThemPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ThemPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ThemPhimDialogLayout.setVerticalGroup(
            ThemPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThemPhimDialogLayout.createSequentialGroup()
                .addComponent(ThemPhimPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        ThemAnhDialog.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh jpg","jpg"));

        SuaPhimDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        SuaPhimDialog.setTitle("Sửa thông tin phim");
        SuaPhimDialog.setBackground(new java.awt.Color(248, 250, 252));
        SuaPhimDialog.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        SuaPhimDialog.setModal(true);

        SuaPhimPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtSuaTenPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel13.setText("Tên phim");

        cbSuaTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel14.setText("Thời lượng");

        txtSuaURLAnh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel15.setText("Thể loại");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel16.setText("Trạng thái");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel17.setText("URL ảnh");

        cbSuaTheLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnSuaAnh.setBackground(new java.awt.Color(0, 146, 255));
        btnSuaAnh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSuaAnh.setForeground(new java.awt.Color(255, 255, 255));
        btnSuaAnh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/add.png"))); // NOI18N
        btnSuaAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuaAnhMouseClicked(evt);
            }
        });

        txtSuaThoiLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButton2.setBackground(new java.awt.Color(0, 146, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Lưu");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        txtSuaMaPhim.setEditable(false);
        txtSuaMaPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel19.setText("Mã phim");

        javax.swing.GroupLayout SuaPhimPanelLayout = new javax.swing.GroupLayout(SuaPhimPanel);
        SuaPhimPanel.setLayout(SuaPhimPanelLayout);
        SuaPhimPanelLayout.setHorizontalGroup(
            SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SuaPhimPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtSuaMaPhim)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SuaPhimPanelLayout.createSequentialGroup()
                        .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16)
                            .addComponent(txtSuaThoiLuong)
                            .addComponent(cbSuaTrangThai, 0, 205, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SuaPhimPanelLayout.createSequentialGroup()
                                .addComponent(txtSuaURLAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnSuaAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel17)))
                    .addGroup(SuaPhimPanelLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(241, 241, 241))
                    .addComponent(txtSuaTenPhim, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SuaPhimPanelLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(cbSuaTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SuaPhimPanelLayout.createSequentialGroup()
                        .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SuaPhimPanelLayout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jLabel12)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        SuaPhimPanelLayout.setVerticalGroup(
            SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SuaPhimPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSuaMaPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addGap(6, 6, 6)
                .addComponent(txtSuaTenPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(20, 20, 20)
                .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSuaThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbSuaTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSuaAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(SuaPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbSuaTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSuaURLAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 33, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout SuaPhimDialogLayout = new javax.swing.GroupLayout(SuaPhimDialog.getContentPane());
        SuaPhimDialog.getContentPane().setLayout(SuaPhimDialogLayout);
        SuaPhimDialogLayout.setHorizontalGroup(
            SuaPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SuaPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        SuaPhimDialogLayout.setVerticalGroup(
            SuaPhimDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SuaPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        txtTimPhim.setToolTipText("Nhập vào phim hoặc mã phim bạn muốn tìm kiếm");
        txtTimPhim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimPhimKeyReleased(evt);
            }
        });

        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTrangThaiActionPerformed(evt);
            }
        });

        cbTheLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTheLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTheLoaiActionPerformed(evt);
            }
        });

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
                        .addComponent(jSThoiLuongToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(jSThoiLuongToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
            Class<?>[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
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
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }// </editor-fold>                        
                                         

    private void btnThemPhimMouseClicked(java.awt.event.MouseEvent evt) {                                         

        ThemPhimDialog.setSize(530, 520);
        String maPhim = movieDao.getNextMovieID();
        txtThemMaPhim.setText(maPhim);
        ThemPhimDialog.setResizable(false);
        ThemPhimDialog.setLocationRelativeTo(null); 
        ThemPhimDialog.setVisible(true);
    }                                        

    private void btnThemAnhMouseClicked(java.awt.event.MouseEvent evt) {                                        
        int result = ThemAnhDialog.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = ThemAnhDialog.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            String normalizedPath = path.replace("\\", "/");
            txtURLThemAnh.setText(normalizedPath);
        }
    }                                       

    private void cbTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {                                            
        LoadTableMovie(applyFilters());
    }                                           

    private void cbTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        LoadTableMovie(applyFilters());
    }                                         

    private void btnSuaAnhMouseClicked(java.awt.event.MouseEvent evt) {                                       

        int result = ThemAnhDialog.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = ThemAnhDialog.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            String normalizePath =  path.replace("\\", "/");
            txtSuaURLAnh.setText(normalizePath);
        }
        
    }                                      

    private void txtTimPhimKeyReleased(java.awt.event.KeyEvent evt) {                                       
        LoadTableMovie(applyFilters());
    }                                      

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
        String id= txtThemMaPhim.getText().trim();
        String tenPhim = txtThemTenPhim.getText().trim();       
        String urlAnh = txtURLThemAnh.getText().trim();
        int statusIdx = cbTrangThai.getSelectedIndex()+1;
        MovieStatus status = MovieStatus.fromInt(statusIdx);
        int genreIdx = cbTheLoai.getSelectedIndex()+1;
        if(tenPhim.equals("") || txtThemThoiLuong.getText().trim().equals("") || urlAnh.equals("")){
            javax.swing.JOptionPane.showMessageDialog(parentWindow, "Vui lòng nhập đầy đủ dữ liệu", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            int thoiLuong = Integer.parseInt(txtThemThoiLuong.getText());
            if(thoiLuong <= 0){
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Thời lượng phải lớn hơn 0", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            Movie m = new Movie(id,tenPhim, genreIdx, thoiLuong, status, urlAnh);
            boolean isSuccess = movieDao.InsertMovie(m);
            if (isSuccess) {
                javax.swing.JOptionPane.showMessageDialog(parentWindow, "Thêm phim thành công!");
                resetThemPhim();
                ThemPhimDialog.dispose();
                list = movieDao.getDSPhim();
                LoadTableMovie(list);
            } else {
                javax.swing.JOptionPane.showMessageDialog(parentWindow, "Thêm phim thất bại!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }catch(NumberFormatException ex){
                System.out.print("Loi: "+ex);
        }
    }                                        

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {                                      
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
        String id= txtSuaMaPhim.getText().trim();
        String tenPhim = txtSuaTenPhim.getText().trim();       
        String urlAnh = txtSuaURLAnh.getText().trim();
        int statusIdx = cbSuaTrangThai.getSelectedIndex()+1;
        MovieStatus status = MovieStatus.fromInt(statusIdx);
        int genreIdx = cbSuaTheLoai.getSelectedIndex()+1;
        if(tenPhim.equals("") || txtSuaThoiLuong.getText().trim().equals("") || urlAnh.equals("")){
            javax.swing.JOptionPane.showMessageDialog(parentWindow, "Vui lòng nhập đầy đủ dữ liệu", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            int thoiLuong = Integer.parseInt(txtSuaThoiLuong.getText());
            if(thoiLuong <= 0){
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Thời lượng phải lớn hơn 0", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
            Movie m = new Movie(id,tenPhim, genreIdx, thoiLuong, status, urlAnh);
            boolean isSuccess = movieDao.UpdateMovie(m);
            if (isSuccess) {
                javax.swing.JOptionPane.showMessageDialog(parentWindow, "Sửa phim thành công!");
                SuaPhimDialog.dispose();
                list = movieDao.getDSPhim();
                LoadTableMovie(list);
            } else {
                javax.swing.JOptionPane.showMessageDialog(parentWindow, "Sửa phim thất bại!", "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }catch(NumberFormatException ex){
                System.out.print("Loi: "+ex);
        }
    }                                     
   
   
    // Variables declaration - do not modify                     
    private javax.swing.JPanel CardPhimPanel;
    private javax.swing.JTable DSPhimTable;
    private javax.swing.JPanel PhanLoaiPanel;
    private javax.swing.JDialog SuaPhimDialog;
    private javax.swing.JPanel SuaPhimPanel;
    private javax.swing.JFileChooser ThemAnhDialog;
    private javax.swing.JDialog ThemPhimDialog;
    private javax.swing.JPanel ThemPhimPanel;
    private javax.swing.JButton btnSuaAnh;
    private javax.swing.JButton btnThemAnh;
    private javax.swing.JButton btnThemPhim;
    private javax.swing.JComboBox<String> cbSuaTheLoai;
    private javax.swing.JComboBox<String> cbSuaTrangThai;
    private javax.swing.JComboBox<String> cbTheLoai;
    private javax.swing.JComboBox<String> cbThemTheLoai;
    private javax.swing.JComboBox<String> cbThemTrangThai;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSlider jSThoiLuongToiDa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lMaxThoiGianPhim;
    private javax.swing.JLabel lMinThoiGianPhim;
    private javax.swing.JTextField txtSuaMaPhim;
    private javax.swing.JTextField txtSuaTenPhim;
    private javax.swing.JTextField txtSuaThoiLuong;
    private javax.swing.JTextField txtSuaURLAnh;
    private javax.swing.JTextField txtThemMaPhim;
    private javax.swing.JTextField txtThemTenPhim;
    private javax.swing.JTextField txtThemThoiLuong;
    private javax.swing.JTextField txtTimPhim;
    private javax.swing.JTextField txtURLThemAnh;
    // End of variables declaration                   
}
