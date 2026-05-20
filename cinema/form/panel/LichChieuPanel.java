package cinema.form.panel;

import cinema.dao.ShowTimeDAO;
import cinema.dao.ShowTimeDAO.LichChieuItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LichChieuPanel extends javax.swing.JPanel {

    private final Color BG = new Color(248, 250, 252);
    private final Color BLUE = new Color(0, 146, 255);
    private final Color BORDER = new Color(204, 204, 204);
    private final Color TEXT = new Color(20, 30, 45);
    private final Color MUTED = new Color(100, 116, 139);
    private final Color ORANGE = new Color(255, 112, 24);

    private ShowTimeDAO showTimeDAO = new ShowTimeDAO();

    private JPanel pnlRoot;
    private JPanel pnlDateBox;
    private JPanel pnlMovieList;
    private LocalDate selectedDate;
    private List<LocalDate> availableDates = new ArrayList<>();

    public LichChieuPanel() {
        initComponents();

        // Không dùng label trống do NetBeans tạo nữa
        jLabel1.setVisible(false);

        buildUI();
        loadDates();
    }

    private void buildUI() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(BG);

        pnlRoot = new JPanel();
        pnlRoot.setBackground(BG);
        pnlRoot.setLayout(new BoxLayout(pnlRoot, BoxLayout.Y_AXIS));
        pnlRoot.setBorder(new EmptyBorder(40, 30, 40, 30));

        JLabel lblTitle = new JLabel("Lịch chiếu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(TEXT);
        lblTitle.setAlignmentX(LEFT_ALIGNMENT);

        pnlDateBox = new JPanel(new BorderLayout());
        pnlDateBox.setBackground(Color.WHITE);
        pnlDateBox.setBorder(new LineBorder(BORDER, 1));
        pnlDateBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        pnlDateBox.setAlignmentX(LEFT_ALIGNMENT);

        JPanel dateWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        dateWrapper.setBackground(Color.WHITE);
        pnlDateBox.add(dateWrapper, BorderLayout.CENTER);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(BG);
        titleRow.setAlignmentX(LEFT_ALIGNMENT);
        titleRow.add(lblTitle, BorderLayout.WEST);

        JLabel lblListTitle = new JLabel("Danh sách lịch chiếu");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblListTitle.setForeground(TEXT);
        lblListTitle.setAlignmentX(LEFT_ALIGNMENT);

        pnlMovieList = new JPanel();
        pnlMovieList.setBackground(BG);
        pnlMovieList.setLayout(new BoxLayout(pnlMovieList, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(pnlMovieList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        pnlRoot.add(titleRow);
        pnlRoot.add(Box.createVerticalStrut(20));
        pnlRoot.add(pnlDateBox);
        pnlRoot.add(Box.createVerticalStrut(35));
        pnlRoot.add(lblListTitle);
        pnlRoot.add(Box.createVerticalStrut(20));
        pnlRoot.add(scrollPane);

        add(pnlRoot, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void loadDates() {
        availableDates = showTimeDAO.getAvailableScheduleDates();

        if (availableDates.isEmpty()) {
            selectedDate = LocalDate.now();
            renderDateButtons();
            showEmptyMessage("Hiện chưa có lịch chiếu nào đang mở.");
            return;
        }

        selectedDate = availableDates.get(0);
        renderDateButtons();
        loadScheduleByDate();
    }

    private void renderDateButtons() {
        pnlDateBox.removeAll();

        JPanel dateWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        dateWrapper.setBackground(Color.WHITE);

        JButton btnPrev = createArrowButton("<");
        btnPrev.setEnabled(false);
        dateWrapper.add(btnPrev);

        if (availableDates.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có ngày chiếu");
            lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblEmpty.setForeground(MUTED);
            dateWrapper.add(lblEmpty);
        } else {
            for (LocalDate date : availableDates) {
                JButton btnDate = createDateButton(date);
                dateWrapper.add(btnDate);
            }
        }

        JButton btnNext = createArrowButton(">");
        btnNext.setEnabled(false);
        dateWrapper.add(btnNext);

        pnlDateBox.add(dateWrapper, BorderLayout.CENTER);
        pnlDateBox.revalidate();
        pnlDateBox.repaint();
    }

    private JButton createArrowButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(44, 44));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXT);
        btn.setBorder(new LineBorder(BORDER, 1));
        return btn;
    }

    private JButton createDateButton(LocalDate date) {
        String thu = getVietnameseDay(date);
        String ngay = date.format(DateTimeFormatter.ofPattern("dd/MM"));

        JButton btn = new JButton("<html><center>" + thu + "<br>" + ngay + "</center></html>");
        btn.setPreferredSize(new Dimension(120, 68));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        if (date.equals(selectedDate)) {
            btn.setBackground(BLUE);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new LineBorder(BLUE, 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(TEXT);
            btn.setBorder(new LineBorder(BORDER, 1));
        }

        btn.addActionListener(e -> {
            selectedDate = date;
            renderDateButtons();
            loadScheduleByDate();
        });

        return btn;
    }

    private String getVietnameseDay(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY: return "Thứ 2";
            case TUESDAY: return "Thứ 3";
            case WEDNESDAY: return "Thứ 4";
            case THURSDAY: return "Thứ 5";
            case FRIDAY: return "Thứ 6";
            case SATURDAY: return "Thứ 7";
            case SUNDAY: return "CN";
            default: return "";
        }
    }

    private void loadScheduleByDate() {
        List<LichChieuItem> data = showTimeDAO.getLichChieuByDate(selectedDate);

        pnlMovieList.removeAll();

        if (data.isEmpty()) {
            showEmptyMessage("Không có lịch chiếu trong ngày này.");
            return;
        }

        Map<String, List<LichChieuItem>> movieMap = new LinkedHashMap<>();

        for (LichChieuItem item : data) {
            String key = item.getMovieId();
            if (!movieMap.containsKey(key)) {
                movieMap.put(key, new ArrayList<>());
            }
            movieMap.get(key).add(item);
        }

        for (List<LichChieuItem> movieItems : movieMap.values()) {
            JPanel card = createMovieScheduleCard(movieItems);
            pnlMovieList.add(card);
            pnlMovieList.add(Box.createVerticalStrut(30));
        }

        pnlMovieList.add(createLegendPanel());

        pnlMovieList.revalidate();
        pnlMovieList.repaint();
    }

    private JPanel createMovieScheduleCard(List<LichChieuItem> items) {
        LichChieuItem first = items.get(0);

        JPanel card = new JPanel(new BorderLayout(24, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BORDER, 1));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        card.setPreferredSize(new Dimension(1000, 260));

        JPanel posterPanel = new JPanel(new GridBagLayout());
        posterPanel.setBackground(Color.WHITE);
        posterPanel.setBorder(new EmptyBorder(18, 18, 18, 0));
        posterPanel.setPreferredSize(new Dimension(170, 230));

        JLabel lblPoster = new JLabel();
        lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
        lblPoster.setIcon(loadPosterIcon(first.getPoster(), 125, 180));
        posterPanel.add(lblPoster);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(18, 0, 18, 18));

        JLabel lblTitle = new JLabel(first.getMovieTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT);
        lblTitle.setAlignmentX(LEFT_ALIGNMENT);

        String genre = first.getGenreName() == null ? "Chưa có thể loại" : first.getGenreName();

        JLabel lblMeta = new JLabel(genre + "  •  " + first.getDuration() + " phút");
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMeta.setForeground(MUTED);
        lblMeta.setAlignmentX(LEFT_ALIGNMENT);

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(5));
        content.add(lblMeta);
        content.add(Box.createVerticalStrut(18));

        Map<String, List<LichChieuItem>> formatMap = new LinkedHashMap<>();

        for (LichChieuItem item : items) {
            String key = item.getFormatText();
            if (!formatMap.containsKey(key)) {
                formatMap.put(key, new ArrayList<>());
            }
            formatMap.get(key).add(item);
        }

        for (String format : formatMap.keySet()) {
            JPanel row = createShowtimeRow(format, formatMap.get(format));
            content.add(row);
            content.add(Box.createVerticalStrut(12));
        }

        card.add(posterPanel, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private JPanel createShowtimeRow(String format, List<LichChieuItem> showtimes) {
        JPanel row = new JPanel(new BorderLayout(18, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblFormat = new JLabel(format);
        lblFormat.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormat.setForeground(TEXT);
        lblFormat.setPreferredSize(new Dimension(190, 48));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        timePanel.setBackground(Color.WHITE);

        for (LichChieuItem item : showtimes) {
            timePanel.add(createTimeButton(item));
        }

        row.add(lblFormat, BorderLayout.WEST);
        row.add(timePanel, BorderLayout.CENTER);

        return row;
    }

    private JButton createTimeButton(LichChieuItem item) {
        String start = item.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String end = item.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        JButton btn = new JButton("<html><center><b>" + start + "</b><br><span style='font-size:9px'>~" + end + "</span></center></html>");
        btn.setPreferredSize(new Dimension(120, 48));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (item.isSoldOut()) {
            btn.setText("<html><center><b>" + start + "</b><br><span style='font-size:9px'>Hết vé</span></center></html>");
            btn.setEnabled(false);
            btn.setBackground(new Color(245, 245, 245));
            btn.setForeground(new Color(140, 140, 140));
            btn.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        } else if (item.isFastSelling()) {
            btn.setText("<html><center><b>" + start + "  🔥</b><br><span style='font-size:9px'>~" + end + "</span></center></html>");
            btn.setBackground(Color.WHITE);
            btn.setForeground(ORANGE);
            btn.setBorder(new LineBorder(ORANGE, 1));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(0, 105, 220));
            btn.setBorder(new LineBorder(new Color(70, 150, 255), 1));
        }

        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "Bạn đã chọn suất chiếu: " + item.getShowtimeId()
                    + "\nPhim: " + item.getMovieTitle()
                    + "\nPhòng: " + item.getRoomName()
                    + "\nGiờ: " + start,
                "Thông tin suất chiếu",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Sau này khi nối màn chọn ghế thì gọi ở đây:
            // openSeatMap(item.getShowtimeId());
        });

        return btn;
    }

    private JPanel createLegendPanel() {
        JPanel legend = new JPanel(new BorderLayout());
        legend.setBackground(Color.WHITE);
        legend.setBorder(new LineBorder(BORDER, 1));
        legend.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        legend.setPreferredSize(new Dimension(1000, 90));

        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 18));
        content.setBackground(Color.WHITE);

        JLabel title = new JLabel("Chú giải trạng thái suất chiếu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT);

        content.add(title);
        content.add(createLegendItem(new Color(70, 150, 255), "Sẵn sàng"));
        content.add(createLegendItem(ORANGE, "Sắp đầy"));
        content.add(createLegendItem(new Color(200, 200, 200), "Hết vé"));

        JLabel note = new JLabel("Lưu ý: Suất chiếu đã qua thời gian hiện tại sẽ không hiển thị.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        note.setForeground(MUTED);

        legend.add(content, BorderLayout.CENTER);
        legend.add(note, BorderLayout.SOUTH);

        return legend;
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        item.setBackground(Color.WHITE);

        JLabel square = new JLabel();
        square.setPreferredSize(new Dimension(28, 22));
        square.setOpaque(true);
        square.setBackground(Color.WHITE);
        square.setBorder(new LineBorder(color, 1));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT);

        item.add(square);
        item.add(lbl);

        return item;
    }

    private void showEmptyMessage(String message) {
        pnlMovieList.removeAll();

        JPanel empty = new JPanel(new GridBagLayout());
        empty.setBackground(Color.WHITE);
        empty.setBorder(new LineBorder(BORDER, 1));
        empty.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        empty.setPreferredSize(new Dimension(1000, 160));

        JLabel lbl = new JLabel(message);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(MUTED);

        empty.add(lbl);
        pnlMovieList.add(empty);

        pnlMovieList.revalidate();
        pnlMovieList.repaint();
    }

    private ImageIcon loadPosterIcon(String posterPath, int width, int height) {
        try {
            if (posterPath == null || posterPath.trim().isEmpty()) {
                return createPlaceholderIcon(width, height);
            }

            java.net.URL url;

            if (posterPath.startsWith("http://") || posterPath.startsWith("https://")) {
                url = java.net.URI.create(posterPath).toURL();
            } else {
                url = getClass().getResource(posterPath);
            }

            if (url == null) {
                System.out.println("Không tìm thấy ảnh poster: " + posterPath);
                return createPlaceholderIcon(width, height);
            }

            Image img = new ImageIcon(url).getImage();
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            e.printStackTrace();
            return createPlaceholderIcon(width, height);
        }
    }

    private ImageIcon createPlaceholderIcon(int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
            width,
            height,
            java.awt.image.BufferedImage.TYPE_INT_RGB
        );

        java.awt.Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(235, 235, 235));
        g2.fillRect(0, 0, width, height);
        g2.setColor(new Color(120, 120, 120));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString("No Poster", 25, height / 2);
        g2.dispose();

        return new ImageIcon(img);
    }

    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Lịch chiếu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(314, 314, 314)
                .addComponent(jLabel1)
                .addContainerGap(234, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel1)
                .addContainerGap(312, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}