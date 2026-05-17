
package cinema.form.panel;
import cinema.dao.MovieDAO;
import cinema.enums.GenreType;
import static cinema.enums.GenreType.getNameGenreType;
import cinema.models.Movie;
import cinema.enums.MovieStatus;
import static cinema.enums.MovieStatus.fromInt;
import static cinema.enums.MovieStatus.getNameMovieStatus;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JPanel;


public class PhimPanel extends javax.swing.JPanel {
    MovieDAO movieDao;
    java.util.List<Movie> movies = new java.util.ArrayList<>(); 
    java.util.List<String> listGenre ;
    private java.util.Map<Movie, JPanel> movieCardCache = new java.util.HashMap<>();
    private java.util.Map<String, javax.swing.ImageIcon> iconCache = new java.util.HashMap<>();
    private javax.swing.Timer searchTimer;
    private java.util.function.Consumer<Movie> onBookTicket;
    public PhimPanel() {
        initComponents();
        movieDao= new MovieDAO();
        this.movies = movieDao.getDSPhim();
        movieCardCache.clear();
        cbTrangThai.addItem("Tất cả");
        setCBStatus(cbTrangThai);
        setjSlider();
        cbTheLoai.addItem("Tất cả");
        setCBGenre(cbTheLoai);
        addMovies(movies);
    }
    public void setOnBookTicket(java.util.function.Consumer<Movie> action) {
        this.onBookTicket = action;
    }
    private boolean checkMovieHasShowtime(Movie m) {
        java.util.List<cinema.models.Movie> availableMovies = movieDao.GetAvailableMovies();
        for (cinema.models.Movie movie : availableMovies) {
            if (movie.getId().equals(m.getId())) {
                return true;
            }
        }
        return false;
    }
    private javax.swing.JPanel createMovieCard(Movie m){
        javax.swing.JPanel card = new javax.swing.JPanel(new java.awt.BorderLayout());
        card.setPreferredSize(new java.awt.Dimension(185, 320));
        card.setBackground(java.awt.Color.WHITE);
        card.putClientProperty("Component.arc", 15);
        card.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 1, true));

        javax.swing.JLayeredPane lp = new javax.swing.JLayeredPane();
        javax.swing.JPanel pnlMain = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlMain.setBackground(java.awt.Color.WHITE);

        // Poster
        javax.swing.JLabel lblPoster = new javax.swing.JLabel();
        lblPoster.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        var posterURL = getClass().getResource(m.getPoster());
        lblPoster.setIcon(scaleImage(posterURL, 185, 220));

        // Thông tin
        javax.swing.JPanel pnlInfo = new javax.swing.JPanel();
        pnlInfo.setLayout(new javax.swing.BoxLayout(pnlInfo, javax.swing.BoxLayout.Y_AXIS));
        pnlInfo.setBackground(java.awt.Color.WHITE);
        pnlInfo.setBorder(new javax.swing.border.EmptyBorder(8, 10, 10, 10));

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("<html><body style='width: 150px; text-align: center;'><b>" + m.getTitle() + "</b></body></html>");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // Căn giữa component trong BoxLayout
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Căn giữa nội dung label

        //Thời lượng và thể loại
        javax.swing.JPanel pnlBottom = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlBottom.setBackground(java.awt.Color.WHITE);
        pnlBottom.setMaximumSize(new java.awt.Dimension(200, 20));

        javax.swing.JLabel lblTime = new javax.swing.JLabel(m.getDuration() + "m");
        lblTime.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblTime.setForeground(java.awt.Color.GRAY);

        javax.swing.JLabel lblGenreText = new javax.swing.JLabel(m.getGenre().name());
        lblGenreText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
        lblGenreText.setForeground(new java.awt.Color(59, 130, 246));
        pnlBottom.add(lblTime, java.awt.BorderLayout.WEST);
        pnlBottom.add(lblGenreText, java.awt.BorderLayout.EAST);
        
        pnlInfo.add(lblTitle);
        pnlInfo.add(javax.swing.Box.createVerticalGlue());
        pnlInfo.add(javax.swing.Box.createVerticalStrut(10));
        pnlInfo.add(pnlBottom);

        pnlMain.add(lblPoster, java.awt.BorderLayout.CENTER);
        pnlMain.add(pnlInfo, java.awt.BorderLayout.SOUTH);

        ///Lớp phủ
        JPanel pnlOverlay = new JPanel(new java.awt.GridBagLayout());
        pnlOverlay.setBackground(new java.awt.Color(30, 41, 59, 140));
        pnlOverlay.setVisible(false);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 0, 10, 0);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL; 
        gbc.ipadx = 40;
        gbc.ipady = 10;
    
        java.awt.Font buttonFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);

        JButton btnDetail = new JButton("Chi tiết");
        btnDetail.setFont(buttonFont);
        btnDetail.setFocusPainted(false);
        btnDetail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        JButton btnBuy = new JButton("Mua vé");
        btnBuy.setFont(buttonFont);
        btnBuy.setBackground(new java.awt.Color(59, 130, 246));
        btnBuy.setForeground(java.awt.Color.WHITE);
        btnBuy.setFocusPainted(false);
        btnBuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        pnlOverlay.add(btnDetail, gbc);
        gbc.gridy = 1;
        pnlOverlay.add(btnBuy, gbc);
        pnlMain.setBounds(0, 0, 185, 320);
        pnlOverlay.setBounds(0, 0, 185, 320); 

        lp.add(pnlMain, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lp.add(pnlOverlay, javax.swing.JLayeredPane.PALETTE_LAYER);
        card.add(lp, java.awt.BorderLayout.CENTER);

        lp.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = lp.getWidth();
                int h = lp.getHeight();
                pnlMain.setBounds(0, 0, w, h);
                pnlOverlay.setBounds(0, 0, w, h);  
            }
        });

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                pnlOverlay.setVisible(true);
                card.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!card.getVisibleRect().contains(e.getPoint())) {
                    pnlOverlay.setVisible(false);
                    card.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 1, true));
                }
            }
        });
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showMovieDetail(m); 
            }
        });
        btnBuy.addActionListener(e -> {
            java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (m.getActive() != MovieStatus.fromInt(1)) { 
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Phim này hiện tại chưa mở bán vé hoặc đã ngừng chiếu!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE );
                return;
            }
            else if(!checkMovieHasShowtime(m)){
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Hôm nay hiện không có hoặc không còn suất chiếu cho phim này!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE );
                return;
            }
            if (onBookTicket != null) {
                onBookTicket.accept(m);
            }
        });
        return card;
    }
    private void showMovieDetail(Movie m) {
        javax.swing.JDialog detailDialog = new javax.swing.JDialog((java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), "Chi tiết phim", true);
        detailDialog.setLayout(new java.awt.BorderLayout());
        detailDialog.getContentPane().setBackground(java.awt.Color.WHITE);

        javax.swing.JPanel pnlContent = new javax.swing.JPanel(new java.awt.GridBagLayout());
        pnlContent.setBackground(java.awt.Color.WHITE);
        pnlContent.setBorder(new javax.swing.border.EmptyBorder(40, 40, 40, 40));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(0, 40, 10, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        javax.swing.JLabel lblPoster = new javax.swing.JLabel();
        var posterURL = getClass().getResource(m.getPoster());
        lblPoster.setIcon(scaleImage(posterURL, 200, 280));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 5;
        pnlContent.add(lblPoster, gbc);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("<html><h2 style='color:#1e293b; margin:0;'>" + m.getTitle() + "</h2></html>");
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1;
        pnlContent.add(lblTitle, gbc);

        String info = String.format("<html>"
                + "<p style='margin-bottom:5px;'><b>Thể loại:</b> <span style='color:#3b82f6;'>%s</span></p>"
                + "<p style='margin-bottom:5px;'><b>Thời lượng:</b> %d phút</p>"
                + "<p style='margin-bottom:5px;'><b>Trạng thái:</b> %s</p>"
                + "</html>", 
                getNameGenreType(m.getGenre()), m.getDuration(), getNameMovieStatus(m.getActive()));

        javax.swing.JLabel lblInfo = new javax.swing.JLabel(info);
        lblInfo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        gbc.gridy = 1;
        pnlContent.add(lblInfo, gbc);
        
        javax.swing.JButton btnBooking = new javax.swing.JButton("Đặt vé ngay");
        btnBooking.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnBooking.setBackground(new java.awt.Color(59, 130, 246)); 
        btnBooking.setForeground(java.awt.Color.WHITE);
        btnBooking.setFocusPainted(false);
        btnBooking.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBooking.addActionListener(e -> {
            java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
            detailDialog.dispose();
            if (m.getActive() != MovieStatus.fromInt(1)) { 
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Phim này hiện tại chưa mở bán vé hoặc đã ngừng chiếu!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            else if(!checkMovieHasShowtime(m)){
                javax.swing.JOptionPane.showMessageDialog(
                parentWindow, "Hôm nay hiện không có hoặc không còn suất chiếu cho phim này!", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE );
                return;
            }
            if (onBookTicket != null) {
                onBookTicket.accept(m);
            }
        });

        gbc.gridx = 1; 
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.insets = new java.awt.Insets(15, 40, 10, 10); 
        gbc.fill = java.awt.GridBagConstraints.NONE; 
        gbc.anchor = java.awt.GridBagConstraints.WEST; 
        pnlContent.add(btnBooking, gbc);

        detailDialog.add(pnlContent, java.awt.BorderLayout.CENTER);

        detailDialog.pack();
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
        detailDialog.setLocationRelativeTo(parentWindow);
        detailDialog.setVisible(true);
    }
    private javax.swing.ImageIcon scaleImage(java.net.URL imagePath, int w, int h) {
        if (imagePath == null) return null;
        String key = imagePath.toString() + "_" + w + "_" + h;
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }
        try {  
            java.awt.Image img = new javax.swing.ImageIcon(imagePath).getImage();
            java.awt.Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            javax.swing.ImageIcon scaledIcon = new javax.swing.ImageIcon(scaled);
            iconCache.put(key, scaledIcon);
            return scaledIcon;
        } catch (Exception e) {
            return null; 
        }
    }
    private void addMovies(java.util.List<Movie> listToDisplay) {
        CardPhimPanel.removeAll();
        CardPhimPanel.setLayout(new java.awt.GridLayout(0, 4, 30, 30));
        for (Movie m : listToDisplay) {
            JPanel movieCard;
            if (movieCardCache.containsKey(m.getId())) {
                movieCard = movieCardCache.get(m.getId());
            } else {
                movieCard = createMovieCard(m);
                movieCardCache.put(m, movieCard);
            }
            CardPhimPanel.add(movieCard);
    }
    CardPhimPanel.revalidate();
    CardPhimPanel.repaint();
    }    
    private javax.swing.JComboBox setCBGenre(javax.swing.JComboBox ComboBox){
        for(GenreType g : GenreType.values()){
            ComboBox.addItem(getNameGenreType(g));
        }
        return ComboBox;
    }
    private javax.swing.JComboBox setCBStatus(javax.swing.JComboBox ComboBox){
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
            addMovies(applyFilters());
        });
    }
    private java.util.List<Movie> applyFilters() {
        String searchTitle = txtTimPhim.getText().trim().toLowerCase();
        int statusIdx = cbTrangThai.getSelectedIndex();
        int genreIdx = cbTheLoai.getSelectedIndex();
        int maxDuration = jSThoiLuongToiDa.getValue();
        java.util.List<Movie> filteredList = movieDao.searchMovies(searchTitle, statusIdx, genreIdx, maxDuration);
        return filteredList;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

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

        setBackground(new java.awt.Color(248, 250, 252));

        PhanLoaiPanel.setBackground(new java.awt.Color(255, 255, 255));
        PhanLoaiPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel1.setLabelFor(txtTimPhim);
        jLabel1.setText("Tìm phim");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel2.setLabelFor(cbTrangThai);
        jLabel2.setText("Trạng thái");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel3.setLabelFor(cbTheLoai);
        jLabel3.setText("Thể loại");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setLabelFor(jSThoiLuongToiDa);
        jLabel4.setText("Thời lượng tối đa");

        txtTimPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimPhim.setToolTipText("Nhập vào phim bạn muốn tìm kiếm");
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                        .addGap(115, 115, 115))
                    .addComponent(txtTimPhim))
                .addGap(39, 39, 39)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addGap(90, 90, 90))
                    .addComponent(cbTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addGap(103, 103, 103))
                    .addComponent(cbTheLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
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

        javax.swing.GroupLayout CardPhimPanelLayout = new javax.swing.GroupLayout(CardPhimPanel);
        CardPhimPanel.setLayout(CardPhimPanelLayout);
        CardPhimPanelLayout.setHorizontalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 982, Short.MAX_VALUE)
        );
        CardPhimPanelLayout.setVerticalGroup(
            CardPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CardPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(CardPhimPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(396, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void cbTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {                                            
        addMovies(applyFilters());
    }                                           

    private void cbTheLoaiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        addMovies(applyFilters());
    }                                         

    private void txtTimPhimKeyReleased(java.awt.event.KeyEvent evt) {                                       
        if (searchTimer != null && searchTimer.isRunning()) {
            searchTimer.stop(); 
        }

        searchTimer = new javax.swing.Timer(300, e -> {
            addMovies(applyFilters());
        });
        searchTimer.setRepeats(false);
        searchTimer.start();
    }                                      


    // Variables declaration - do not modify                     
    private javax.swing.JPanel CardPhimPanel;
    private javax.swing.JPanel PhanLoaiPanel;
    private javax.swing.JComboBox<String> cbTheLoai;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSlider jSThoiLuongToiDa;
    private javax.swing.JLabel lMaxThoiGianPhim;
    private javax.swing.JLabel lMinThoiGianPhim;
    private javax.swing.JTextField txtTimPhim;
    // End of variables declaration                   

    
}
