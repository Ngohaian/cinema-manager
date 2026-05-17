
package cinema.form.panel;

import java.awt.CardLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import cinema.dao.MovieDAO;
import cinema.dao.ShowTimeDAO;
import static cinema.enums.GenreType.getNameGenreType;
import cinema.models.Movie;
import cinema.models.ShowTime;
import java.awt.Color;
import java.util.List;
public class BanVePanel extends javax.swing.JPanel {
    MovieDAO movieDao ;
    ShowTimeDAO showtimeDao;
    List<Movie> movies ;
    List<ShowTime> showtimes ;
    private PhongManagerPanel seatMap = new PhongManagerPanel();

    private java.util.Map<Movie, JPanel> movieCardCache = new java.util.HashMap<>();
    private javax.swing.Timer searchTimer;
    private int currentStep = 0;
    private JLabel[] stepLabels;
    private final Color BACKGROUND = new Color(248, 250, 252);
    private final Color BLUE = new Color(0, 146, 255);
    private final Color ORANGE = new Color(245, 157, 35);
    private final Color BORDER = new Color(204, 204, 204);
    private final Color HEADER = new Color(235, 235, 235);

    private final Color SEAT_REGULAR = new Color(46, 132, 235);
    private final Color SEAT_VIP = new Color(245, 181, 32);
    private final Color SEAT_COUPLE = new Color(235, 93, 160);
    private final Color SEAT_EMPTY = Color.WHITE;
    private JPanel seatMapPanel;
    private JButton btnEmptySeat;
    private JButton btnRegularSeat;
    private JButton btnVipSeat;
    private JButton btnCoupleSeat;

    public BanVePanel() {
        try{
            movieDao= new MovieDAO();
            showtimeDao = new ShowTimeDAO();
            this.movies = movieDao.GetAvailableMovies();
            this.showtimes = showtimeDao.getAll();
        }
        catch(Exception ex){
            System.out.println("Loi: " + ex.getMessage());
        }
        initComponents();
        addMovies(movies);
        customizeScrollBar(jScrollPane1);
        ShowPanel("ChonPhim");
        setJLabelChon();
        JPanel seatBox = seatMapHelper.createSeatMapBox();
        SoDoGhePanel.add(seatBox, BorderLayout.CENTER);
        seatMapHelper.loadSeatMap(roomId);
    }   
    public void loadData() {
        this.movies = movieDao.GetAvailableMovies();
        addMovies(this.movies);
    }
    private javax.swing.JPanel createMovieCard(Movie m) {
        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setLayout(new java.awt.BorderLayout());
        card.setPreferredSize(new java.awt.Dimension(185, 320)); 
        card.setBackground(java.awt.Color.WHITE);

        card.putClientProperty("Component.arc", 15);
        card.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 1, true));

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

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(
            "<html><body style='width: 150px; text-align: center;'><b>" + m.getTitle() + "</b></body></html>"
        );
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // Căn giữa component trong BoxLayout
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Căn giữa nội dung label

        //Thời lượng và thể loại
        javax.swing.JPanel pnlBottom = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlBottom.setBackground(java.awt.Color.WHITE);
        pnlBottom.setMaximumSize(new java.awt.Dimension(200, 20)); // Khống chế chiều cao dòng cuối

        javax.swing.JLabel lblTime = new javax.swing.JLabel(m.getDuration() + "m");
        lblTime.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblTime.setForeground(java.awt.Color.GRAY);

        javax.swing.JLabel lblGenreText = new javax.swing.JLabel(getNameGenreType(m.getGenre()));
        lblGenreText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
        lblGenreText.setForeground(new java.awt.Color(59, 130, 246));

        pnlBottom.add(lblTime, java.awt.BorderLayout.WEST);
        pnlBottom.add(lblGenreText, java.awt.BorderLayout.EAST);

        pnlInfo.add(lblTitle);
        pnlInfo.add(javax.swing.Box.createVerticalGlue()); 
        pnlInfo.add(javax.swing.Box.createVerticalStrut(10));
        pnlInfo.add(pnlBottom);

        card.add(lblPoster, java.awt.BorderLayout.CENTER);
        card.add(pnlInfo, java.awt.BorderLayout.SOUTH);

        card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        java.awt.Color hoverGrey = new java.awt.Color(204, 204, 204);
        java.awt.Color originalGrey = new java.awt.Color(245, 245, 245); 

        javax.swing.border.Border hoverBorder = new javax.swing.border.LineBorder(hoverGrey, 2, true);
        javax.swing.border.Border normalBorder = new javax.swing.border.LineBorder(originalGrey, 1, true);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(hoverBorder);
                pnlInfo.setBorder(new javax.swing.border.EmptyBorder(3, 10, 15, 10)); 
                card.revalidate();
                card.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(normalBorder);
                pnlInfo.setBorder(new javax.swing.border.EmptyBorder(8, 10, 10, 10));

                card.revalidate();
                card.repaint();
            }
        });
        return card;
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
    private void addMovies(java.util.List<Movie> listToDisplay) {
        DSPhimPanel.removeAll();
        DSPhimPanel.setLayout(new java.awt.GridLayout(0, 4, 30, 30));
        for (Movie m : listToDisplay) {
            JPanel movieCard;
            if (movieCardCache.containsKey(m)) {
                movieCard = movieCardCache.get(m);
            } else {
                movieCard = createMovieCard(m);
                movieCardCache.put(m, movieCard);
                
                movieCard.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        displayShowTime(m);
                    }
                });
            }
            DSPhimPanel.add(movieCard);
        }
        DSPhimPanel.revalidate();
        DSPhimPanel.repaint();
    }
    public void displayShowTime(Movie m){
        List<ShowTime> ST = showtimeDao.getByMovieId(m.getId());
        String[] times = new String[ST.size()];
        for(int i=0; i<ST.size();i++){
            java.time.LocalDateTime start = ST.get(i).getStartTime();
            times[i] = String.format("%02d:%02d", start.getHour(), start.getMinute());
        }
        renderSuatChieu(m, times);
        ShowPanel("ChonSuatChieu"); 
        currentStep=1;
        updateNavigation();
    }
    private void setJLabelChon(){
        stepLabels = new JLabel[]{LChonPhim, LChonSuatChieu, LChonGhe, LHoaDon};
        for (int i = 0; i < stepLabels.length; i++) {
            final int index = i;
            stepLabels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (index < currentStep) {
                        currentStep = index;
                        updateNavigation();
                        String[] panelNames = {"ChonPhim", "ChonSuatChieu", "ChonGhe", "HoaDon"};
                        ShowPanel(panelNames[index]);
                    }
                }
            });
        }
    }
    private void updateNavigation(){
        for(int i=0;i<stepLabels.length;i++){
            if(i==currentStep){
                stepLabels[i].setBackground(Color.white);
            }
            else if(i<currentStep){
                stepLabels[i].setBackground(new java.awt.Color(220,220,242));
                stepLabels[i].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            else{
                stepLabels[i].setBackground(new java.awt.Color(232,232,255));
                stepLabels[i].setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }
    private void customizeScrollBar(JScrollPane JScroll){
        JScroll.getVerticalScrollBar().setUnitIncrement(20);
        JScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI(){

        @Override
        protected void paintThumb(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(new java.awt.Color(180, 180, 180));
            // Bo góc thanh trượt
            g2.fillRoundRect(thumbBounds.x + 4, thumbBounds.y + 2, thumbBounds.width - 8, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        @Override
        protected javax.swing.JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        @Override
        protected javax.swing.JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        private javax.swing.JButton createZeroButton() {
            javax.swing.JButton b = new javax.swing.JButton();
            b.setPreferredSize(new java.awt.Dimension(0, 0));
            return b;
        }
        
        });

        JScroll.setBorder(null);
        JScroll.getViewport().setBackground(java.awt.Color.WHITE);
    }
    private void ShowPanel(String name){
        CardLayout cl = (CardLayout )(ContentPanel.getLayout());
        cl.show(ContentPanel, name);
        jScrollPane1.getVerticalScrollBar().setValue(0);
    }
    public void renderSuatChieu(Movie m, String[] times) {
        ChonSuatChieuPanel.removeAll();
        ChonSuatChieuPanel.setLayout(new java.awt.GridBagLayout()); 

        // --- 1. Card trắng tổng thể ---
        javax.swing.JPanel mainCard = new javax.swing.JPanel();
        mainCard.setLayout(new javax.swing.BoxLayout(mainCard, javax.swing.BoxLayout.Y_AXIS));
        mainCard.setBackground(java.awt.Color.WHITE);
        javax.swing.border.Border line = new javax.swing.border.LineBorder(new java.awt.Color(204,204,204), 1, true);
        javax.swing.border.Border padding = new javax.swing.border.EmptyBorder(30, 30, 30, 30);
        mainCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(line, padding));
        mainCard.setPreferredSize(new java.awt.Dimension(700, 500));

        // --- 2. Phần Header (Ảnh + Thông tin phim) ---
        javax.swing.JPanel pnlHeader = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        pnlHeader.setOpaque(false);

        // Poster bên trái
        javax.swing.JLabel lblPoster = new javax.swing.JLabel();
        var posterURL = getClass().getResource(m.getPoster());
        lblPoster.setIcon(scaleImage(posterURL, 200, 250));
        pnlHeader.add(lblPoster, java.awt.BorderLayout.WEST);

        // Thông tin bên phải
        javax.swing.JPanel pnlInfo = new javax.swing.JPanel();
        pnlInfo.setLayout(new javax.swing.BoxLayout(pnlInfo, javax.swing.BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        
        String lblMultiLineTitle = "<html><body style='width: 400px'>" + m.getTitle() + "</body></html>";
        javax.swing.JLabel lblTitle = new javax.swing.JLabel(lblMultiLineTitle.toUpperCase());
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        String theLoai = getNameGenreType(m.getGenre()); 
        javax.swing.JPanel pnlTags = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        pnlTags.setOpaque(false);
        pnlTags.add(createTag(theLoai, new java.awt.Color(239, 246, 255), new java.awt.Color(59, 130, 246)));
        pnlTags.add(createTag(String.valueOf(m.getDuration() +" phút"), new java.awt.Color(239, 246, 255), new java.awt.Color(59, 130, 246)));
        
        javax.swing.JLabel lblNote = new javax.swing.JLabel("Lưu ý: Suất chiếu hiển thị là lịch chiếu của ngày hôm nay.");
        lblNote.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        lblNote.setFont(new java.awt.Font("Segoe UI", 2, 11));
        lblNote.setForeground(java.awt.Color.GRAY);

        pnlInfo.add(lblTitle);
        pnlInfo.add(javax.swing.Box.createVerticalStrut(10));
        pnlInfo.add(pnlTags);
        pnlInfo.add(javax.swing.Box.createVerticalStrut(15));
        pnlInfo.add(lblNote);

        pnlHeader.add(pnlInfo, java.awt.BorderLayout.CENTER);

        // --- 3. Đường kẻ ngang  ---
        javax.swing.JSeparator sep = new javax.swing.JSeparator();
        sep.setForeground(new java.awt.Color(241, 245, 249));

        // --- 4. Phần Chọn Giờ Chiếu ---
        javax.swing.JLabel lblSelectTime = new javax.swing.JLabel("CHỌN GIỜ CHIẾU");
        lblSelectTime.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblSelectTime.setForeground(new java.awt.Color(51, 65, 85));
        lblSelectTime.setBorder(new javax.swing.border.EmptyBorder(10,10,10,10));
        javax.swing.JPanel pnlGridTime = new javax.swing.JPanel(new java.awt.GridLayout(0, 4, 15, 15));
        pnlGridTime.setOpaque(false);

        for (String time : times) {
            pnlGridTime.add(createTimeButton(time));
        }

        mainCard.add(pnlHeader);
        mainCard.add(javax.swing.Box.createVerticalStrut(25));
        mainCard.add(sep);
        mainCard.add(javax.swing.Box.createVerticalStrut(25));
        mainCard.add(lblSelectTime);
        mainCard.add(javax.swing.Box.createVerticalStrut(15));
        mainCard.add(pnlGridTime);

        ChonSuatChieuPanel.add(mainCard);
        ChonSuatChieuPanel.revalidate();
        ChonSuatChieuPanel.repaint();
    }
    private javax.swing.JLabel createTag(String text, java.awt.Color bg, java.awt.Color fg) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(new java.awt.Font("Segoe UI", 1, 10));
        lbl.setForeground(fg);
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(new javax.swing.border.EmptyBorder(5, 10, 5, 10));
        lbl.putClientProperty("Component.arc", 10);
        return lbl;
    }
    private javax.swing.JButton createTimeButton(String time) {
        javax.swing.JButton btn = new javax.swing.JButton(time);
        btn.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btn.setBackground(new java.awt.Color(248, 250, 252));
        btn.setForeground(new java.awt.Color(51, 65, 85));
        btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(241, 245, 249), 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.putClientProperty("Component.arc", 15);

        btn.addActionListener(e -> {
            ShowPanel("ChonGhe"); 
        });
        return btn;
    }


    private void setupSummaryPanel(javax.swing.JPanel panel, Movie selectedMovie) {
        panel.removeAll();
        panel.setLayout(new java.awt.BorderLayout(20, 0));
        panel.setBackground(java.awt.Color.WHITE);

        javax.swing.JPanel pnlLeft = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 5));
        pnlLeft.setOpaque(false);

        javax.swing.JLabel lblMovieName = new javax.swing.JLabel(selectedMovie != null ? selectedMovie.getTitle().toUpperCase() : "CHƯA CHỌN PHIM");
        lblMovieName.setFont(new java.awt.Font("Segoe UI", 1, 15));

        lblSummaryInfo = new javax.swing.JLabel("Ghế: Chưa chọn | Tổng tiền: 0 VNĐ");
        lblSummaryInfo.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblSummaryInfo.setForeground(java.awt.Color.GRAY);

        pnlLeft.add(lblMovieName);
        pnlLeft.add(lblSummaryInfo);

        javax.swing.JButton btnConfirm = new javax.swing.JButton("Đặt vé");
        btnConfirm.setBackground(new java.awt.Color(192, 36, 36)); 
        btnConfirm.setForeground(java.awt.Color.WHITE);
        btnConfirm.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnConfirm.setPreferredSize(new java.awt.Dimension(150, 45));
        btnConfirm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

//        btnConfirm.addActionListener(e -> {
//            if (selectedSeatsList.isEmpty()) {
//                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 ghế!");
//            } else {
//                currentStep = 3;
//                updateNavigation();
//                ShowPanel("HoaDon");
//            }
//        });

        panel.add(pnlLeft, java.awt.BorderLayout.CENTER);
        panel.add(btnConfirm, java.awt.BorderLayout.EAST);
        panel.revalidate();
        panel.repaint();
    }
//    private void updateSummaryInfo() {
//        if (lblSummaryInfo == null) return;
//        long total = 0;
//        java.util.StringJoiner joiner = new java.util.StringJoiner(", ");
//        for (cinema.models.Seat s : selectedSeatsList) {
//            joiner.add(s.getSeatLabel());
//            if ("VIP".equals(s.getSeatType())) total += 30000;
//            else if ("COUPLE".equals(s.getSeatType())) total += 50000;
//            else total += 65000;
//        }
//
//        String listGhe = selectedSeatsList.isEmpty() ? "Chưa chọn" : joiner.toString();
//        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
//
//        lblSummaryInfo.setText("<html>Ghế: <b style='color:#3b82f6'>" + listGhe + 
//                              "</b> | Tổng tiền: <b style='color:#ef4444'>" + formatter.format(total) + " VNĐ</b></html>");
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        lblSummaryInfo = new javax.swing.JLabel();
        ChonPanel = new javax.swing.JPanel();
        LChonPhim = new javax.swing.JLabel();
        LChonSuatChieu = new javax.swing.JLabel();
        LChonGhe = new javax.swing.JLabel();
        LHoaDon = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ContentPanel = new javax.swing.JPanel();
        ChonSuatChieuPanel = new javax.swing.JPanel();
        SuatChieuPanel = new javax.swing.JPanel();
        ChonGhePanel = new javax.swing.JPanel();
        SoDoGhePanel = new javax.swing.JPanel();
        BookingSummaryPanel = new javax.swing.JPanel();
        HoaDonPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ChonPhimPanel = new javax.swing.JPanel();
        txtTimPhim = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        DSPhimPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(248, 250, 252));
        setPreferredSize(new java.awt.Dimension(759, 779));

        LChonPhim.setBackground(new java.awt.Color(248, 250, 252));
        LChonPhim.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        LChonPhim.setForeground(new java.awt.Color(51, 51, 51));
        LChonPhim.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LChonPhim.setText("Phim");
        LChonPhim.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        LChonPhim.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LChonPhim.setOpaque(true);

        LChonSuatChieu.setBackground(new java.awt.Color(232, 232, 255));
        LChonSuatChieu.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        LChonSuatChieu.setForeground(new java.awt.Color(51, 51, 51));
        LChonSuatChieu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LChonSuatChieu.setText("Suất chiếu");
        LChonSuatChieu.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        LChonSuatChieu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LChonSuatChieu.setOpaque(true);

        LChonGhe.setBackground(new java.awt.Color(232, 232, 255));
        LChonGhe.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        LChonGhe.setForeground(new java.awt.Color(51, 51, 51));
        LChonGhe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LChonGhe.setText("Ghế");
        LChonGhe.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        LChonGhe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LChonGhe.setOpaque(true);

        LHoaDon.setBackground(new java.awt.Color(232, 232, 255));
        LHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        LHoaDon.setForeground(new java.awt.Color(51, 51, 51));
        LHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LHoaDon.setText("Hóa đơn");
        LHoaDon.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        LHoaDon.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LHoaDon.setOpaque(true);

        javax.swing.GroupLayout ChonPanelLayout = new javax.swing.GroupLayout(ChonPanel);
        ChonPanel.setLayout(ChonPanelLayout);
        ChonPanelLayout.setHorizontalGroup(
            ChonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(LChonPhim, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(LChonSuatChieu, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(LChonGhe, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(LHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        ChonPanelLayout.setVerticalGroup(
            ChonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LChonPhim)
            .addComponent(LChonSuatChieu)
            .addComponent(LChonGhe)
            .addComponent(LHoaDon)
        );

        ContentPanel.setLayout(new java.awt.CardLayout());

        ChonSuatChieuPanel.setBackground(new java.awt.Color(248, 250, 252));

        SuatChieuPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout SuatChieuPanelLayout = new javax.swing.GroupLayout(SuatChieuPanel);
        SuatChieuPanel.setLayout(SuatChieuPanelLayout);
        SuatChieuPanelLayout.setHorizontalGroup(
            SuatChieuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        SuatChieuPanelLayout.setVerticalGroup(
            SuatChieuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ChonSuatChieuPanelLayout = new javax.swing.GroupLayout(ChonSuatChieuPanel);
        ChonSuatChieuPanel.setLayout(ChonSuatChieuPanelLayout);
        ChonSuatChieuPanelLayout.setHorizontalGroup(
            ChonSuatChieuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonSuatChieuPanelLayout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(SuatChieuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(128, 128, 128))
        );
        ChonSuatChieuPanelLayout.setVerticalGroup(
            ChonSuatChieuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonSuatChieuPanelLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(SuatChieuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(77, 77, 77))
        );

        ContentPanel.add(ChonSuatChieuPanel, "ChonSuatChieu");

        ChonGhePanel.setBackground(new java.awt.Color(248, 250, 252));

        SoDoGhePanel.setBackground(new java.awt.Color(248, 250, 252));

        javax.swing.GroupLayout SoDoGhePanelLayout = new javax.swing.GroupLayout(SoDoGhePanel);
        SoDoGhePanel.setLayout(SoDoGhePanelLayout);
        SoDoGhePanelLayout.setHorizontalGroup(
            SoDoGhePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        SoDoGhePanelLayout.setVerticalGroup(
            SoDoGhePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
        );

        BookingSummaryPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout BookingSummaryPanelLayout = new javax.swing.GroupLayout(BookingSummaryPanel);
        BookingSummaryPanel.setLayout(BookingSummaryPanelLayout);
        BookingSummaryPanelLayout.setHorizontalGroup(
            BookingSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        BookingSummaryPanelLayout.setVerticalGroup(
            BookingSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ChonGhePanelLayout = new javax.swing.GroupLayout(ChonGhePanel);
        ChonGhePanel.setLayout(ChonGhePanelLayout);
        ChonGhePanelLayout.setHorizontalGroup(
            ChonGhePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BookingSummaryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(SoDoGhePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ChonGhePanelLayout.setVerticalGroup(
            ChonGhePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonGhePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(SoDoGhePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(BookingSummaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        ContentPanel.add(ChonGhePanel, "ChonGhe");

        HoaDonPanel.setBackground(new java.awt.Color(248, 250, 252));

        javax.swing.GroupLayout HoaDonPanelLayout = new javax.swing.GroupLayout(HoaDonPanel);
        HoaDonPanel.setLayout(HoaDonPanelLayout);
        HoaDonPanelLayout.setHorizontalGroup(
            HoaDonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 951, Short.MAX_VALUE)
        );
        HoaDonPanelLayout.setVerticalGroup(
            HoaDonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        ContentPanel.add(HoaDonPanel, "HoaDon");

        ChonPhimPanel.setBackground(new java.awt.Color(248, 250, 252));

        txtTimPhim.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtTimPhim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimPhimKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Tìm phim");

        DSPhimPanel.setBackground(new java.awt.Color(248, 250, 252));

        javax.swing.GroupLayout DSPhimPanelLayout = new javax.swing.GroupLayout(DSPhimPanel);
        DSPhimPanel.setLayout(DSPhimPanelLayout);
        DSPhimPanelLayout.setHorizontalGroup(
            DSPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 889, Short.MAX_VALUE)
        );
        DSPhimPanelLayout.setVerticalGroup(
            DSPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ChonPhimPanelLayout = new javax.swing.GroupLayout(ChonPhimPanel);
        ChonPhimPanel.setLayout(ChonPhimPanelLayout);
        ChonPhimPanelLayout.setHorizontalGroup(
            ChonPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonPhimPanelLayout.createSequentialGroup()
                .addContainerGap(227, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtTimPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(227, Short.MAX_VALUE))
            .addGroup(ChonPhimPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(DSPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );
        ChonPhimPanelLayout.setVerticalGroup(
            ChonPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChonPhimPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ChonPhimPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(40, 40, 40)
                .addComponent(DSPhimPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        jScrollPane1.setViewportView(ChonPhimPanel);

        ContentPanel.add(jScrollPane1, "ChonPhim");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ChonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ChonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void txtTimPhimKeyReleased(java.awt.event.KeyEvent evt) {                                       
        String title = txtTimPhim.getText().trim().toLowerCase();
        if (searchTimer != null && searchTimer.isRunning()) {
            searchTimer.stop(); 
        }
        java.util.List ds = movieDao.searchMovies(title, 0,0,movieDao.getMaxDuration());
        searchTimer = new javax.swing.Timer(300, e -> {
            addMovies(ds);
        });
        searchTimer.setRepeats(false);
        searchTimer.start();
    }                                      


    // Variables declaration - do not modify                     
    private javax.swing.JPanel BookingSummaryPanel;
    private javax.swing.JPanel ChonGhePanel;
    private javax.swing.JPanel ChonPanel;
    private javax.swing.JPanel ChonPhimPanel;
    private javax.swing.JPanel ChonSuatChieuPanel;
    private javax.swing.JPanel ContentPanel;
    private javax.swing.JPanel DSPhimPanel;
    private javax.swing.JPanel HoaDonPanel;
    private javax.swing.JLabel LChonGhe;
    private javax.swing.JLabel LChonPhim;
    private javax.swing.JLabel LChonSuatChieu;
    private javax.swing.JLabel LHoaDon;
    private javax.swing.JPanel SoDoGhePanel;
    private javax.swing.JPanel SuatChieuPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSummaryInfo;
    private javax.swing.JTextField txtTimPhim;
    // End of variables declaration                   
}
