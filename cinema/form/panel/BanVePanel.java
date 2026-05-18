
package cinema.form.panel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import cinema.dao.CustomerDAO;
import cinema.dao.InvoiceDAO;
import cinema.dao.MovieDAO;
import cinema.dao.ShowTimeDAO;
import cinema.dao.CustomerDAO;
import static cinema.enums.GenreType.getNameGenreType;

import cinema.models.Customer;
import cinema.models.Employee;
import cinema.models.Invoice;
import cinema.models.Movie;
import cinema.models.Seat;
import cinema.models.ShowTime;
import cinema.models.Ticket;

import java.util.ArrayList;
import java.util.List;
public class BanVePanel extends javax.swing.JPanel {
    MovieDAO movieDao ;
    ShowTimeDAO showtimeDao;
    InvoiceDAO invoiceDao ;
    CustomerDAO customerDao;
    List<Movie> movies ;
    List<ShowTime> showtimes ;
    private PhongManagerPanel seatMap = new PhongManagerPanel();
    private java.util.Map<Movie, JPanel> movieCardCache = new java.util.HashMap<>();
    private javax.swing.Timer searchTimer;
    private int currentStep = 0;
    private JLabel[] stepLabels;
    private Employee currentEmployee;
    public BanVePanel() {
        try{
            movieDao= new MovieDAO();
            showtimeDao = new ShowTimeDAO();
            invoiceDao = new InvoiceDAO();
            customerDao = new CustomerDAO();
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
        
        SoDoGhePanel.setLayout(new java.awt.BorderLayout());
    }   
    public void setCurrentEmployee(Employee e){
        this.currentEmployee = e;
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
        List<String> maSuatChieuList = new java.util.ArrayList<>();
        List<String> maPhongList = new java.util.ArrayList<>();
        String[] times = new String[ST.size()];
        for(int i=0; i<ST.size();i++){
            java.time.LocalDateTime start = ST.get(i).getStartTime();
            times[i] = String.format("%02d:%02d", start.getHour(), start.getMinute());
        }
        for(int i=0;i<ST.size();i++){
            maPhongList.add(ST.get(i).getRoom().getRoomId());
            maSuatChieuList.add(ST.get(i).getShowtimeId());
        }
        renderSuatChieu(m, times, maPhongList, maSuatChieuList);
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
                stepLabels[i].setBackground(java.awt.Color.white);
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
        java.awt.CardLayout cl = (java.awt.CardLayout )(ContentPanel.getLayout());
        cl.show(ContentPanel, name);
        jScrollPane1.getVerticalScrollBar().setValue(0);
    }
    public void renderSuatChieu(Movie m, String[] times, List<String> maPhong, List<String> maSuatChieu){ {
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
        for(int i=0;i<times.length;i++){
            pnlGridTime.add(createTimeButton(times[i], maPhong.get(i), maSuatChieu.get(i),m));
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
    private javax.swing.JButton createTimeButton(String time, String maPhong, String maSuatChieu, Movie m) {
        javax.swing.JButton btn = new javax.swing.JButton(time);
        btn.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btn.setBackground(new java.awt.Color(248, 250, 252));
        btn.setForeground(new java.awt.Color(51, 65, 85));
        btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(241, 245, 249), 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn.putClientProperty("Component.arc", 15);

        btn.addActionListener(e -> {
        SoDoGhePanel.removeAll();
        SoDoGhePanel.setLayout(new java.awt.BorderLayout());
        
        seatMap.setSelectMode(true);
        JPanel seatBox = seatMap.createSeatMapBox();
        SoDoGhePanel.add(seatBox, java.awt.BorderLayout.CENTER);
        SoDoGhePanel.revalidate();
        SoDoGhePanel.repaint();

        List<cinema.models.Seat> bookedSeats = showtimeDao.getSeatStatusByShowtimeId(maSuatChieu);
        seatMap.loadSeatMapForSelling(maPhong, bookedSeats);

        currentStep = 2;
        updateNavigation();
        ShowPanel("ChonGhe");
        seatMap.setOnSeatClickAction(() -> updateSummaryInfo(showtimeDao.getShowtimeById(maSuatChieu)));
        setupSummaryPanel(BookingSummaryPanel, m, showtimeDao.getShowtimeById(maSuatChieu));
    });
        return btn;
    }

    private void setupSummaryPanel(javax.swing.JPanel panel, Movie selectedMovie, ShowTime selectedShowTime) {
        panel.removeAll();
        panel.setLayout(new java.awt.BorderLayout(20, 0));
        panel.setBackground(java.awt.Color.WHITE);
        panel.setPreferredSize(new java.awt.Dimension(0, 100));
        panel.setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
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
        btnConfirm.addActionListener(e -> {
            if (seatMap.getSelectedSeatsList().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 ghế!");
            } else {
                currentStep = 3;
                updateNavigation();
                ShowPanel("HoaDon");
                String maHD = invoiceDao.getNextInvoiceID();
                List<Ticket> dsticket = new ArrayList<>();
                String nextTicketIdFromDB = invoiceDao.getNextTicketIdByShowtimeId(selectedShowTime.getShowtimeId());
                int lastI = nextTicketIdFromDB.lastIndexOf("-");
                String ticketIdst = nextTicketIdFromDB.substring(0, lastI + 1); 
                int ticketNumber = Integer.parseInt(nextTicketIdFromDB.substring(lastI + 1)); 
                if (ticketNumber == 0) ticketNumber = 1;
                for (cinema.models.Seat s : seatMap.getSelectedSeatsList()) {
                    Ticket t = new Ticket();
                    String ticketId = String.format("%s-%03d", ticketIdst, ticketNumber);
                    t.setTicketId(ticketId);
                    t.setSeat(s);
                    t.setShowtime(selectedShowTime);
                    if (s.getSeatType() != null) {
                        String loaiGheStr = s.getSeatType().name();
                        if ("VIP".equals(loaiGheStr)) {
                            t.setPrice(selectedShowTime.getBasePrice() + selectedShowTime.getVipExtra());
                        } else if ("COUPLE".equals(loaiGheStr)) {
                            t.setPrice(selectedShowTime.getBasePrice() + selectedShowTime.getCoupleExtra());
                        } else {
                            t.setPrice(selectedShowTime.getBasePrice());
                        }
                    } else {
                        t.setPrice(selectedShowTime.getBasePrice());
                    }
                    t.setStatus(cinema.enums.TicketStatus.Sold);
                    t.setInvoiceId(maHD);
                    ticketNumber++;
                    dsticket.add(t);
                }
                renderHoaDon(maHD, currentEmployee.getId(), currentEmployee.getName(), selectedMovie.getTitle(), LocalDateTime.now(), dsticket);
            }
        });

        panel.add(pnlLeft, java.awt.BorderLayout.CENTER);
        panel.add(btnConfirm, java.awt.BorderLayout.EAST);
        panel.revalidate();
        panel.repaint();
    }
    private void updateSummaryInfo(ShowTime st) {
        if (lblSummaryInfo == null) return;
        long total = 0;
        java.util.StringJoiner joiner = new java.util.StringJoiner(", ");
        
        for (cinema.models.Seat s : seatMap.getSelectedSeatsList()) {
            String tenGhe = (char) ('A' + s.getRowIndex()) + String.valueOf(s.getColIndex() + 1);
            joiner.add(tenGhe);
            
            if (s.getSeatType() != null) {
                String loaiGheStr = s.getSeatType().name();
                
                if ("VIP".equals(loaiGheStr)) {
                    total += (st.getVipExtra() + st.getBasePrice());
                } else if ("COUPLE".equals(loaiGheStr)) {
                    total += (st.getCoupleExtra() + st.getBasePrice());
                } else {
                    total += st.getBasePrice();
                }
            }
        }

        String listGhe = seatMap.getSelectedSeatsList().isEmpty() ? "Chưa chọn" : joiner.toString();
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(java.util.Locale.forLanguageTag("vi-VN"));

        lblSummaryInfo.setText("<html>Ghế: <b style='color:#3b82f6'>" + listGhe + 
                                "</b> | Tổng tiền: <b style='color:#ef4444'>" + formatter.format(total) + " VNĐ</b></html>");
    }

    public void renderHoaDon(String maHD, String maNV, String tenNV, String tenPhim, LocalDateTime ngayLap, List<Ticket> danhSachVe) {
        final List<Ticket> dsVeFinal = new ArrayList<>(danhSachVe); 
        TTHoaDonPanel.removeAll();
        TTHoaDonPanel.setLayout(new java.awt.BorderLayout(15, 15));
        TTHoaDonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1, true), 
        BorderFactory.createEmptyBorder(20, 30, 20, 30)));
        double totalInvoiceAmount = 0;
        for(Ticket t : dsVeFinal){
            totalInvoiceAmount += t.getPrice();
        }
        final double totalInvoiceAmountFinal = totalInvoiceAmount;
        final double[] discountRef = {0};
        final double[] totalAfterRef = {totalInvoiceAmount};
        final JLabel lblSaleVal = new JLabel("0 VNĐ");
        final JLabel lblTotalVal = new JLabel(String.format("%,.0f VNĐ", totalInvoiceAmount));

        //1.Title hóa đơn
        JLabel lblTitle = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new java.awt.Color(44, 62, 80));
        TTHoaDonPanel.add(lblTitle, java.awt.BorderLayout.NORTH);

        //2.Body hóa đơn
        JPanel pnlBody = new JPanel();
        pnlBody.setBackground(java.awt.Color.WHITE);
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
        
        //2.1. Thông tin chung
        JPanel pnlInfo = new JPanel(new java.awt.GridLayout(2, 2, 15, 10)); 
        pnlInfo.setBackground(java.awt.Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY, 1), "Thông tin chung"
        ));
        pnlInfo.add(new JLabel("<html><b>Mã hóa đơn:</b> " + maHD + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Ngày lập:</b> " + ngayLap + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Nhân viên:</b> " + maNV + " - " + tenNV + "</html>"));
        pnlInfo.add(new JLabel("<html><b>Tên phim:</b> " + tenPhim + "</html>"));
        
        JPanel pnlTenKH = new JPanel(new java.awt.BorderLayout(5, 0));
        pnlTenKH.setBackground(java.awt.Color.WHITE);
        javax.swing.JTextField txtTenKH = new javax.swing.JTextField("Guest");
        txtTenKH.setEditable(false);
        txtTenKH.setBackground(new java.awt.Color(240, 240, 240));
        javax.swing.JTextField txtSdtKH = new javax.swing.JTextField("");
        txtSdtKH.addActionListener(ev -> {
            String sdt = txtSdtKH.getText().trim();
            if (sdt.isEmpty()) return;
                Customer found = customerDao.getCustomerBySDT(sdt);
                if (found != null) {
                    txtTenKH.setText(found.getName());
                    txtTenKH.setEditable(false);
                    txtTenKH.setBackground(new java.awt.Color(240, 240, 240));
                    discountRef[0] = found.calculateDiscount(totalInvoiceAmountFinal);
                    totalAfterRef[0] = totalInvoiceAmountFinal - discountRef[0];
                    lblSaleVal.setText(String.format("%,.0f VNĐ", discountRef[0]));
                    lblTotalVal.setText(String.format("%,.0f VNĐ", totalAfterRef[0]));
                } else {
                    txtTenKH.setText("");
                    txtTenKH.setEditable(true);
                    txtTenKH.setBackground(java.awt.Color.WHITE);
                    txtTenKH.requestFocus();
                    discountRef[0] = 0;
                    totalAfterRef[0] = totalInvoiceAmountFinal;
                    lblSaleVal.setText("0 VNĐ");
                    lblTotalVal.setText(String.format("%,.0f VNĐ", totalInvoiceAmountFinal));
                }
            });
        txtTenKH.addActionListener(ev -> {
            String tenKH = txtTenKH.getText().trim();
            String sdt = txtSdtKH.getText().trim();
            if (tenKH.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(HoaDonPanel, "Vui lòng nhập đầy đủ tên và số điện thoại!");
                return;
            }
            Customer newCustomer = new Customer(tenKH, sdt, ""); 
            newCustomer.setId(customerDao.getNextCustomerId());
            boolean success = customerDao.addCustomer(newCustomer);
            if (success) {
                JOptionPane.showMessageDialog(HoaDonPanel, "Đã thêm khách hàng mới: " + tenKH);
                txtTenKH.setEditable(false);
                txtTenKH.setBackground(new java.awt.Color(240, 240, 240));
            } else {
                JOptionPane.showMessageDialog(HoaDonPanel, "Lỗi khi thêm khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        pnlTenKH.add(new JLabel("<html><b>Tên khách hàng:</b></html>"), java.awt.BorderLayout.WEST);
        pnlTenKH.add(txtTenKH, java.awt.BorderLayout.CENTER);
        pnlInfo.add(pnlTenKH);
        
        JPanel pnlSdtKH = new JPanel(new java.awt.BorderLayout(5, 0));
        pnlSdtKH.setBackground(java.awt.Color.WHITE);
        pnlSdtKH.add(new JLabel("<html><b>Số điện thoại:</b></html>"), java.awt.BorderLayout.WEST);
        pnlSdtKH.add(txtSdtKH, java.awt.BorderLayout.CENTER);
        pnlInfo.add(pnlSdtKH);
        pnlInfo.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, pnlInfo.getPreferredSize().height +50 ));
        pnlBody.add(pnlInfo);
        pnlBody.add(Box.createVerticalStrut(15));

        //2.2. Bảng chi tiết vé
        String[] columnHeaders = {"Loại Ghế", "Tên Ghế", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0);
        JTable tblDetails = new JTable(tableModel);
        tblDetails.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblDetails.setRowHeight(25);
        tblDetails.getTableHeader().setBackground(java.awt.Color.WHITE);
        List<String> seatType = new ArrayList<>();
        for (Ticket ticket : dsVeFinal) {
            String loaiGhe = ticket.getSeat().getSeatType().toString();
            if (!seatType.contains(loaiGhe)) {
                seatType.add(loaiGhe);
            }
        }

        for (String loaiGhe : seatType) {
            StringBuilder sbTenGhe = new StringBuilder();
            double donGia = 0;
            int soLuong = 0;
            boolean laVeDauTien = true;
            
            for (Ticket ticket : dsVeFinal) {
                if (ticket.getSeat().getSeatType().toString().equals(loaiGhe)) {
                    if (laVeDauTien) {
                        donGia = ticket.getPrice();
                        laVeDauTien = false;
                    } else {
                        sbTenGhe.append(", ");
                    }
                    Seat ghe = ticket.getSeat();
                    String tenGhe = (char) ('A' + ghe.getRowIndex()) + String.valueOf(ghe.getColIndex() + 1);
                    sbTenGhe.append(tenGhe);
                    soLuong++;
                }
            }
            double thanhTien = donGia * soLuong;

            tableModel.addRow(new Object[]{loaiGhe, sbTenGhe.toString(), 
                String.format("%,.0f VNĐ", donGia), 
                soLuong, 
                String.format("%,.0f VNĐ", thanhTien)
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(tblDetails);
        scrollPane.setBackground(java.awt.Color.WHITE);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 150));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết vị trí ghế đặt"));
        pnlBody.add(scrollPane);
        pnlBody.add(Box.createVerticalStrut(10));
        
        //3. Tổng tiền
        JPanel pnlTotalWrapper = new JPanel(new java.awt.BorderLayout());
        pnlTotalWrapper.setBackground(java.awt.Color.WHITE);
        pnlTotalWrapper.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 100));

        JPanel pnlTotal = new JPanel();
        pnlTotal.setLayout(new BoxLayout(pnlTotal, BoxLayout.Y_AXIS));
        pnlTotal.setBackground(java.awt.Color.WHITE);

        // 3.1. Tổng tiền
        JPanel row1 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        row1.setBackground(java.awt.Color.WHITE);
        row1.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        JLabel lblTotalInvoiceText = new JLabel("Tổng tiền: ");
        lblTotalInvoiceText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        JLabel lblTotalInvoiceVal = new JLabel(String.format("%,.0f VNĐ", totalInvoiceAmount));
        lblTotalInvoiceVal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        row1.add(lblTotalInvoiceText);
        row1.add(lblTotalInvoiceVal);

        // 3.2. Giảm giá
        JPanel row2 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        row2.setBackground(java.awt.Color.WHITE);
        row2.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        JLabel lblSaleOff = new JLabel("Giảm giá: ");
        lblSaleOff.setForeground(java.awt.Color.BLUE);
        lblSaleOff.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblSaleVal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblSaleVal.setForeground(java.awt.Color.BLUE);
        row2.add(lblSaleOff);
        row2.add(lblSaleVal);

        // 3.3. Tổng thanh toán
        JPanel row3 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        row3.setBackground(java.awt.Color.WHITE);
        row3.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        JLabel lblTotal = new JLabel("Tổng thanh toán: ");
        lblTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblTotalVal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTotalVal.setForeground(java.awt.Color.RED);
        row3.add(lblTotal);
        row3.add(lblTotalVal);

        pnlTotal.add(row1);
        pnlTotal.add(row2);
        pnlTotal.add(row3);
        pnlTotalWrapper.add(pnlTotal, java.awt.BorderLayout.EAST);
        pnlBody.add(pnlTotalWrapper);
        
        TTHoaDonPanel.add(pnlBody, java.awt.BorderLayout.CENTER);

        //4. Phương thức thanh toán
        JPanel pnlButtons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 5));
        pnlButtons.setBackground(java.awt.Color.WHITE);
        JButton btnTienMat = new JButton("Thanh toán Tiền Mặt");
        JButton btnChuyenKhoan = new JButton("Thanh toán Chuyển Khoản");
        
        java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13);
        btnTienMat.setFont(btnFont); btnChuyenKhoan.setFont(btnFont);
        btnTienMat.setBackground(new java.awt.Color(46, 204, 113)); btnTienMat.setForeground(java.awt.Color.WHITE);
        btnChuyenKhoan.setBackground(new java.awt.Color(52, 152, 219)); btnChuyenKhoan.setForeground(java.awt.Color.WHITE);
        
        java.awt.event.ActionListener paymentListener = new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String phuongThuc = (e.getSource() == btnTienMat) ? "Tiền mặt" : "Chuyển khoản";
            String ten = txtTenKH.getText().trim();
            String sdt = txtSdtKH.getText().trim();
            String customerId = "CUS006";
            if (!sdt.isEmpty()) {
                Customer found = customerDao.getCustomerBySDT(sdt);
                if (found != null) {
                    customerId = found.getId();
                }
            }
            JOptionPane.showMessageDialog(HoaDonPanel,
                "Thanh toán thành công qua hình thức: " + phuongThuc, "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
            String ngayLapStr = ngayLap.toLocalDate().toString();
            Invoice newInvoice = new Invoice(maHD, customerId, maNV, ngayLap, totalAfterRef[0]); 
            if (invoiceDao.insert(newInvoice)) {
                for (Ticket t : dsVeFinal) {
                    invoiceDao.insertTicket(t);
                }
                xuatVeRaFilePDF(maHD, tenPhim, ngayLapStr, ten, sdt, tableModel, totalInvoiceAmountFinal, discountRef[0], totalAfterRef[0] );
                ShowPanel("ChonPhim");
            }
        }
        };
        
        btnTienMat.addActionListener(paymentListener);
        btnChuyenKhoan.addActionListener(paymentListener);
        
        pnlButtons.add(btnTienMat);
        pnlButtons.add(btnChuyenKhoan);
        TTHoaDonPanel.add(pnlButtons, java.awt.BorderLayout.SOUTH);
        TTHoaDonPanel.revalidate();
        TTHoaDonPanel.repaint();
    }


    private void xuatVeRaFilePDF(String maHD, String tenPhim, String ngayLap, String tenKH, String sdtKH, DefaultTableModel model, double tongTien, double giamGia, double tongThanhToan) {
        Document document = new Document(org.openpdf.text.PageSize.A5);
        String filePath = "VeXemPhim_" + maHD + ".pdf";
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph title = new Paragraph("VE XEM PHIM - BETA CINEMA");
            title.setAlignment(org.openpdf.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            org.openpdf.text.pdf.draw.LineSeparator line = new org.openpdf.text.pdf.draw.LineSeparator();
            document.add(new org.openpdf.text.Chunk(line));
            document.add(org.openpdf.text.Chunk.NEWLINE);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1f, 1f});
            infoTable.setSpacingAfter(10);

            for (org.openpdf.text.pdf.PdfPCell c : new org.openpdf.text.pdf.PdfPCell[]{
                cell("Ma HD: " + maHD),
                cell("Ngay: " + ngayLap),
                cell("Ten phim: " + tenPhim),
                cell(""),
                cell("Khach hang: " + tenKH),
                cell("SDT: " + sdtKH),
            }) { infoTable.addCell(c); }
            document.add(infoTable);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 3f, 1f, 2.5f});
            table.setSpacingBefore(5);
            table.setSpacingAfter(10);

            for (String h : new String[]{"Loai Ghe", "Ghe", "SL", "Thanh Tien"}) {
                org.openpdf.text.pdf.PdfPCell c = cell(h);
                c.setHorizontalAlignment(org.openpdf.text.Element.ALIGN_CENTER);
                table.addCell(c);
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int col : new int[]{0, 1, 3, 4}) {
                    org.openpdf.text.pdf.PdfPCell c = cell(model.getValueAt(i, col).toString());
                    c.setHorizontalAlignment(org.openpdf.text.Element.ALIGN_CENTER);
                    table.addCell(c);
                }
            }
            document.add(table);
            document.add(new org.openpdf.text.Chunk(line));
            document.add(org.openpdf.text.Chunk.NEWLINE);

            Paragraph total = new Paragraph("TONG TIEN: " + String.format("%,.0f VND", tongTien));
            total.setAlignment(org.openpdf.text.Element.ALIGN_RIGHT);
            Paragraph discount = new Paragraph("GIAM GIA: " + String.format("%,.0f VND", giamGia));
            discount.setAlignment(org.openpdf.text.Element.ALIGN_RIGHT);
            Paragraph finalTotal = new Paragraph("THANH TOAN: " + String.format("%,.0f VND", tongThanhToan));
            finalTotal.setAlignment(org.openpdf.text.Element.ALIGN_RIGHT);
            document.add(total);
            document.add(discount);
            document.add(finalTotal);

            document.add(org.openpdf.text.Chunk.NEWLINE);
            Paragraph footer = new Paragraph("Cam on quy khach! Chuc xem phim vui ve!");
            footer.setAlignment(org.openpdf.text.Element.ALIGN_CENTER);
            document.add(footer);

            JOptionPane.showMessageDialog(HoaDonPanel, "Da xuat ve ra file: " + filePath);

        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(HoaDonPanel, "Loi: " + ex.getMessage(), "Loi", JOptionPane.ERROR_MESSAGE);
        } finally {
            document.close();
        }
    }

    private org.openpdf.text.pdf.PdfPCell cell(String text) {
        org.openpdf.text.pdf.PdfPCell c = new org.openpdf.text.pdf.PdfPCell(new Paragraph(text));
        c.setBorder(org.openpdf.text.Rectangle.NO_BORDER);
        c.setPadding(4);
        return c;
    }
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
        HoaDonPanel.setLayout(new java.awt.BorderLayout());
        HoaDonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 150, 40, 150));
        TTHoaDonPanel = new javax.swing.JPanel();
        TTHoaDonPanel.setBackground(java.awt.Color.WHITE);
        TTHoaDonPanel.setPreferredSize(new java.awt.Dimension(400, 0));
        HoaDonPanel.add(TTHoaDonPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>                        

    private void txtTimPhimKeyReleased(java.awt.event.KeyEvent evt) {                                       
        String title = txtTimPhim.getText().trim().toLowerCase();
        if (searchTimer != null && searchTimer.isRunning()) {
            searchTimer.stop(); 
        }
        List<Movie> ds = movieDao.searchMovies(title, 0,0,movieDao.getMaxDuration());
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
    private javax.swing.JPanel TTHoaDonPanel;
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
