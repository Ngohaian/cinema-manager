package cinema.form.panel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuatChieuManagerPanel extends JPanel {

    private static final Color PAGE_BG = new Color(248, 250, 252);
    private static final Color BORDER_COLOR = new Color(204, 204, 204);
    private static final Color HEADER_BG = new Color(235, 235, 235);
    private static final Color PRIMARY_BLUE = new Color(0, 146, 255);
    private static final Color TEXT_DARK = new Color(33, 37, 41);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JLabel lblHeader = new JLabel("Quản lý suất chiếu phim");
    private final JButton btnAdd = new JButton("Thêm suất chiếu");

    private final JPanel filterPanel = new JPanel(null);
    private final HintTextField txtSearch = new HintTextField("Tìm kiếm theo phim hoặc phòng...");
    private final HintTextField txtFromDate = new HintTextField("dd/MM/yyyy");
    private final HintTextField txtToDate = new HintTextField("dd/MM/yyyy");
    private final JComboBox<String> cbPhong = new JComboBox<>();
    private final JComboBox<String> cbDinhDang = new JComboBox<>();
    private final JComboBox<String> cbTrangThai = new JComboBox<>();
    private final JButton btnClearFilter = new JButton("Xóa bộ lọc");

    private final JLabel lblTableTitle = new JLabel("Danh sách suất chiếu");
    private final JPanel tableCard = new JPanel(null);
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;

    private final List<ShowtimeRow> allRows = new ArrayList<>();
    private final Map<String, ImageIcon> imageCache = new HashMap<>();

    public SuatChieuManagerPanel() {
        setLayout(null);
        setBackground(PAGE_BG);
        setOpaque(true);
        setPreferredSize(new Dimension(1280, 860));

        initHeader();
        initFilterPanel();
        initTableSection();
        initData();
        initListeners();
        applyFilters();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutComponents();
            }
        });
    }

    private void initHeader() {
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(TEXT_DARK);
        add(lblHeader);

        stylePrimaryButton(btnAdd);
        btnAdd.addActionListener(e -> new ShowtimeFormFrame().setVisible(true));
        add(btnAdd);
    }

    private void initFilterPanel() {
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(BORDER_COLOR));
        add(filterPanel);

        styleInput(txtSearch);
        styleInput(txtFromDate);
        styleInput(txtToDate);

        styleComboBox(cbPhong, new String[]{"Tất cả phòng", "Phòng 1", "Phòng 2", "Phòng 3", "Phòng 4", "Phòng 5"});
        styleComboBox(cbDinhDang, new String[]{"Tất cả định dạng", "2D", "3D", "VIP", "IMAX", "COUPLE"});
        styleComboBox(cbTrangThai, new String[]{"Tất cả trạng thái", "Sắp chiếu", "Đang mở bán", "Đã chiếu"});

        styleSecondaryButton(btnClearFilter);

        filterPanel.add(txtSearch);
        filterPanel.add(btnClearFilter);
        filterPanel.add(createFilterLabel("Phòng"));
        filterPanel.add(createFilterLabel("Định dạng"));
        filterPanel.add(createFilterLabel("Trạng thái"));
        filterPanel.add(createFilterLabel("Từ ngày"));
        filterPanel.add(createFilterLabel("Đến ngày"));
        filterPanel.add(cbPhong);
        filterPanel.add(cbDinhDang);
        filterPanel.add(cbTrangThai);
        filterPanel.add(txtFromDate);
        filterPanel.add(txtToDate);
    }

    private void initTableSection() {
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTableTitle.setForeground(TEXT_DARK);
        add(lblTableTitle);

        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new LineBorder(BORDER_COLOR));
        add(tableCard);

        String[] columns = {"Phim", "Phòng chiếu", "Ngày & giờ", "Định dạng", "Tình trạng ghế"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(92);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(TEXT_DARK);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(Color.WHITE);
        table.setSelectionForeground(TEXT_DARK);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(HEADER_BG);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setPreferredSize(new Dimension(0, 44));
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT_DARK);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        table.getColumnModel().getColumn(0).setCellRenderer(new MovieInfoRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new RoomInfoRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new ScheduleInfoRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new FormatRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new SeatInfoRenderer());

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        tableCard.add(scrollPane);
    }

    private void initData() {
        allRows.clear();

        allRows.add(new ShowtimeRow(
                new MovieInfo("M001", "Biệt đội siêu anh hùng: Hồi kết", 181,
                        "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg"),
                new RoomInfo("Phòng 4", 200),
                new ScheduleInfo(LocalDate.of(2024, 6, 5), "14:30 - 17:31"),
                "IMAX",
                new SeatInfo(0, 200),
                "Sắp chiếu"
        ));

        allRows.add(new ShowtimeRow(
                new MovieInfo("M002", "Người Nhện: Không còn nhà", 148,
                        "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg"),
                new RoomInfo("Phòng 2", 120),
                new ScheduleInfo(LocalDate.of(2024, 6, 5), "16:00 - 18:28"),
                "3D",
                new SeatInfo(18, 120),
                "Đang mở bán"
        ));

        allRows.add(new ShowtimeRow(
                new MovieInfo("M003", "Doraemon: Nobita và vùng đất lý tưởng trên bầu trời", 107,
                        "https://media.themoviedb.org/t/p/w300_and_h450_face/f0cSFvuuEXpQEHXn9jFpCblHyMI.jpg"),
                new RoomInfo("Phòng 1", 100),
                new ScheduleInfo(LocalDate.of(2024, 6, 5), "08:30 - 10:17"),
                "2D",
                new SeatInfo(10, 100),
                "Đang mở bán"
        ));

        allRows.add(new ShowtimeRow(
                new MovieInfo("M004", "Quỷ nhập tràng 2", 126,
                        "https://media.themoviedb.org/t/p/w300_and_h450_face/6iycE00klNR0CTh0UANedZTxf7U.jpg"),
                new RoomInfo("Phòng 3", 50),
                new ScheduleInfo(LocalDate.of(2024, 6, 5), "21:00 - 23:06"),
                "VIP",
                new SeatInfo(6, 50),
                "Sắp chiếu"
        ));

        allRows.add(new ShowtimeRow(
                new MovieInfo("M009", "Minions: Sự trỗi dậy của Gru", 88,
                        "https://media.themoviedb.org/t/p/w300_and_h450_face/gvY5ZHjvrdPuqdVNYpOMGa2ZoWu.jpg"),
                new RoomInfo("Phòng 5", 40),
                new ScheduleInfo(LocalDate.of(2024, 6, 6), "09:00 - 10:28"),
                "COUPLE",
                new SeatInfo(4, 40),
                "Đang mở bán"
        ));
    }

    private void initListeners() {
        DocumentListener filterListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        };

        txtSearch.getDocument().addDocumentListener(filterListener);
        txtFromDate.getDocument().addDocumentListener(filterListener);
        txtToDate.getDocument().addDocumentListener(filterListener);

        cbPhong.addActionListener(e -> applyFilters());
        cbDinhDang.addActionListener(e -> applyFilters());
        cbTrangThai.addActionListener(e -> applyFilters());

        btnClearFilter.addActionListener(e -> {
            txtSearch.setText("");
            txtFromDate.setText("");
            txtToDate.setText("");
            cbPhong.setSelectedIndex(0);
            cbDinhDang.setSelectedIndex(0);
            cbTrangThai.setSelectedIndex(0);
            applyFilters();
        });
    }

    private void applyFilters() {
        tableModel.setRowCount(0);

        String keyword = txtSearch.getText().trim().toLowerCase();
        String phong = String.valueOf(cbPhong.getSelectedItem());
        String dinhDang = String.valueOf(cbDinhDang.getSelectedItem());
        String trangThai = String.valueOf(cbTrangThai.getSelectedItem());

        LocalDate fromDate = parseDate(txtFromDate.getText().trim());
        LocalDate toDate = parseDate(txtToDate.getText().trim());

        for (ShowtimeRow row : allRows) {
            boolean matchKeyword = keyword.isEmpty()
                    || row.movie.title.toLowerCase().contains(keyword)
                    || row.room.roomName.toLowerCase().contains(keyword);

            boolean matchPhong = phong == null || phong.equals("Tất cả phòng") || row.room.roomName.equals(phong);
            boolean matchDinhDang = dinhDang == null || dinhDang.equals("Tất cả định dạng") || row.format.equals(dinhDang);
            boolean matchTrangThai = trangThai == null || trangThai.equals("Tất cả trạng thái") || row.status.equals(trangThai);
            boolean matchFromDate = fromDate == null || !row.schedule.date.isBefore(fromDate);
            boolean matchToDate = toDate == null || !row.schedule.date.isAfter(toDate);

            if (matchKeyword && matchPhong && matchDinhDang && matchTrangThai && matchFromDate && matchToDate) {
                tableModel.addRow(new Object[]{row.movie, row.room, row.schedule, row.format, row.seatInfo});
            }
        }

        adjustTableCardHeight();
        repaint();
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private void adjustTableCardHeight() {
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        int rowHeight = table.getRowHeight() * Math.max(table.getRowCount(), 1);
        int contentHeight = headerHeight + rowHeight + 2;
        int maxHeight = Math.max(180, getHeight() - tableCard.getY() - 40);
        int targetHeight = Math.min(contentHeight, maxHeight);
        tableCard.setSize(tableCard.getWidth(), targetHeight);
        scrollPane.setBounds(1, 1, Math.max(0, tableCard.getWidth() - 2), Math.max(0, targetHeight - 2));
    }

    private void layoutComponents() {
        int panelWidth = Math.max(getWidth(), 1280);
        int marginX = 30;
        int top = 40;

        lblHeader.setBounds(marginX, top, 520, 40);
        btnAdd.setBounds(panelWidth - marginX - 210, top, 210, 40);

        int filterY = top + 70;
        int filterWidth = panelWidth - (marginX * 2);
        int filterHeight = 290;
        filterPanel.setBounds(marginX, filterY, filterWidth, filterHeight);
        layoutFilterPanel();

        int titleY = filterY + filterHeight + 50;
        lblTableTitle.setBounds(marginX, titleY, 420, 32);

        int tableY = titleY + 52;
        tableCard.setBounds(marginX, tableY, filterWidth, 200);
        adjustTableCardHeight();
    }

    private void layoutFilterPanel() {
        int pad = 20;
        int width = filterPanel.getWidth();

        int searchHeight = 48;
        int clearWidth = 170;
        int searchWidth = width - (pad * 3) - clearWidth;

        txtSearch.setBounds(pad, 20, searchWidth, searchHeight);
        btnClearFilter.setBounds(width - pad - clearWidth, 20, clearWidth, searchHeight);

        JSeparator separator = null;
        for (Component c : filterPanel.getComponents()) {
            if (c instanceof JSeparator) {
                separator = (JSeparator) c;
                break;
            }
        }
        if (separator == null) {
            separator = new JSeparator();
            separator.setForeground(HEADER_BG);
            separator.setBackground(HEADER_BG);
            filterPanel.add(separator);
        }
        separator.setBounds(pad, 87, width - (pad * 2), 1);

        Component[] components = filterPanel.getComponents();
        JLabel lblPhong = (JLabel) components[2];
        JLabel lblDinhDang = (JLabel) components[3];
        JLabel lblTrangThai = (JLabel) components[4];
        JLabel lblFrom = (JLabel) components[5];
        JLabel lblTo = (JLabel) components[6];

        int gap = 22;
        int colWidth = (width - (pad * 2) - (gap * 2)) / 3;
        int fieldHeight = 52;

        int row2LabelY = 108;
        int row2FieldY = 138;

        lblPhong.setBounds(pad, row2LabelY, colWidth, 22);
        lblDinhDang.setBounds(pad + colWidth + gap, row2LabelY, colWidth, 22);
        lblTrangThai.setBounds(pad + (colWidth + gap) * 2, row2LabelY, colWidth, 22);

        cbPhong.setBounds(pad, row2FieldY, colWidth, fieldHeight);
        cbDinhDang.setBounds(pad + colWidth + gap, row2FieldY, colWidth, fieldHeight);
        cbTrangThai.setBounds(pad + (colWidth + gap) * 2, row2FieldY, colWidth, fieldHeight);

        int row3LabelY = 206;
        int row3FieldY = 236;
        int dateWidth = (width - (pad * 2) - gap) / 2;

        lblFrom.setBounds(pad, row3LabelY, dateWidth, 22);
        lblTo.setBounds(pad + dateWidth + gap, row3LabelY, dateWidth, 22);

        txtFromDate.setBounds(pad, row3FieldY, dateWidth, fieldHeight);
        txtToDate.setBounds(pad + dateWidth + gap, row3FieldY, dateWidth, fieldHeight);
    }

    private JLabel createFilterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_DARK);
        field.setBackground(Color.WHITE);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(0, 16, 0, 16)));
    }

    private void styleComboBox(JComboBox<String> comboBox, String[] items) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
        comboBox.setModel(model);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_DARK);
        comboBox.setFocusable(false);
        comboBox.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(0, 12, 0, 12)));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel lb = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lb.setBorder(new EmptyBorder(0, 8, 0, 8));
                lb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return lb;
            }
        });
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(PRIMARY_BLUE));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_DARK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BORDER_COLOR));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private ImageIcon loadPoster(String url) {
        if (url == null || url.isBlank()) {
            return createPlaceholderPoster();
        }
        if (imageCache.containsKey(url)) {
            return imageCache.get(url);
        }

        try {
            BufferedImage image = ImageIO.read(new URL(url));
            Image scaled = image.getScaledInstance(52, 74, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);
            imageCache.put(url, icon);
            return icon;
        } catch (Exception e) {
            ImageIcon fallback = createPlaceholderPoster();
            imageCache.put(url, fallback);
            return fallback;
        }
    }

    private ImageIcon createPlaceholderPoster() {
        BufferedImage image = new BufferedImage(52, 74, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(228, 232, 236));
        g2.fillRect(0, 0, 52, 74);
        g2.setColor(new Color(150, 150, 150));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        String text = "Poster";
        int x = (52 - fm.stringWidth(text)) / 2;
        int y = (74 + fm.getAscent()) / 2 - 4;
        g2.drawString(text, x, y);
        g2.dispose();
        return new ImageIcon(image);
    }

    private abstract static class BasePanelRenderer implements javax.swing.table.TableCellRenderer {
        protected JPanel createPanel(boolean isSelected, JTable table) {
            JPanel panel = new JPanel();
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return panel;
        }
    }

    private class MovieInfoRenderer extends BasePanelRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            MovieInfo info = (MovieInfo) value;

            JPanel panel = createPanel(isSelected, table);
            panel.setLayout(new BorderLayout(14, 0));
            panel.setBorder(new EmptyBorder(8, 14, 8, 10));

            JLabel lblPoster = new JLabel(loadPoster(info.posterUrl));
            lblPoster.setPreferredSize(new Dimension(52, 74));
            panel.add(lblPoster, BorderLayout.WEST);

            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel lblTitle = new JLabel(info.title);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTitle.setForeground(TEXT_DARK);

            JLabel lblSub = new JLabel(info.code + " • " + info.durationMinutes + " phút");
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblSub.setForeground(TEXT_MUTED);

            textPanel.add(Box.createVerticalGlue());
            textPanel.add(lblTitle);
            textPanel.add(Box.createVerticalStrut(6));
            textPanel.add(lblSub);
            textPanel.add(Box.createVerticalGlue());

            panel.add(textPanel, BorderLayout.CENTER);
            return panel;
        }
    }

    private class RoomInfoRenderer extends BasePanelRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            RoomInfo info = (RoomInfo) value;

            JPanel panel = createPanel(isSelected, table);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(16, 14, 16, 10));

            JLabel lblRoom = new JLabel(info.roomName);
            lblRoom.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblRoom.setForeground(TEXT_DARK);
            lblRoom.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblSeats = new JLabel(info.capacity + " ghế");
            lblSeats.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblSeats.setForeground(TEXT_MUTED);
            lblSeats.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(lblRoom);
            panel.add(Box.createVerticalStrut(6));
            panel.add(lblSeats);
            return panel;
        }
    }

    private class ScheduleInfoRenderer extends BasePanelRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            ScheduleInfo info = (ScheduleInfo) value;

            JPanel panel = createPanel(isSelected, table);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(16, 14, 16, 10));

            JLabel lblDate = new JLabel(info.date.format(DATE_FORMAT));
            lblDate.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblDate.setForeground(TEXT_DARK);
            lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblTime = new JLabel(info.timeRange);
            lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblTime.setForeground(TEXT_MUTED);
            lblTime.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(lblDate);
            panel.add(Box.createVerticalStrut(6));
            panel.add(lblTime);
            return panel;
        }
    }

    private static class FormatRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(new EmptyBorder(0, 14, 0, 10));
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(TEXT_DARK);
            label.setBackground(Color.WHITE);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            return label;
        }
    }

    private static class SeatInfoRenderer extends BasePanelRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            SeatInfo info = (SeatInfo) value;

            JPanel panel = createPanel(isSelected, table);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(16, 14, 16, 18));

            JLabel lblText = new JLabel(info.sold + "/" + info.total);
            lblText.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblText.setForeground(TEXT_DARK);
            lblText.setAlignmentX(Component.LEFT_ALIGNMENT);

            JProgressBar progressBar = new JProgressBar(0, info.total);
            progressBar.setValue(info.sold);
            progressBar.setStringPainted(false);
            progressBar.setBorderPainted(false);
            progressBar.setPreferredSize(new Dimension(110, 6));
            progressBar.setMaximumSize(new Dimension(110, 6));
            progressBar.setMinimumSize(new Dimension(110, 6));
            progressBar.setForeground(PRIMARY_BLUE);
            progressBar.setBackground(new Color(229, 233, 237));
            progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(lblText);
            panel.add(Box.createVerticalStrut(10));
            panel.add(progressBar);
            return panel;
        }
    }

    private static class HintTextField extends JTextField {
        private final String hint;

        public HintTextField(String hint) {
            this.hint = hint;
            setOpaque(true);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(getFont());
                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(hint, insets.left, y);
                g2.dispose();
            }
        }
    }

    private static class MovieInfo {
        private final String code;
        private final String title;
        private final int durationMinutes;
        private final String posterUrl;

        public MovieInfo(String code, String title, int durationMinutes, String posterUrl) {
            this.code = code;
            this.title = title;
            this.durationMinutes = durationMinutes;
            this.posterUrl = posterUrl;
        }
    }

    private static class RoomInfo {
        private final String roomName;
        private final int capacity;

        public RoomInfo(String roomName, int capacity) {
            this.roomName = roomName;
            this.capacity = capacity;
        }
    }

    private static class ScheduleInfo {
        private final LocalDate date;
        private final String timeRange;

        public ScheduleInfo(LocalDate date, String timeRange) {
            this.date = date;
            this.timeRange = timeRange;
        }
    }

    private static class SeatInfo {
        private final int sold;
        private final int total;

        public SeatInfo(int sold, int total) {
            this.sold = sold;
            this.total = total;
        }
    }

    private static class ShowtimeRow {
        private final MovieInfo movie;
        private final RoomInfo room;
        private final ScheduleInfo schedule;
        private final String format;
        private final SeatInfo seatInfo;
        private final String status;

        public ShowtimeRow(MovieInfo movie, RoomInfo room, ScheduleInfo schedule,
                           String format, SeatInfo seatInfo, String status) {
            this.movie = movie;
            this.room = room;
            this.schedule = schedule;
            this.format = format;
            this.seatInfo = seatInfo;
            this.status = status;
        }
    }

    private static class ShowtimeFormFrame extends JFrame {
        public ShowtimeFormFrame() {
            setTitle("Thêm suất chiếu");
            setSize(620, 460);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(Color.WHITE);
            setLayout(null);

            JLabel lblTitle = new JLabel("Thêm suất chiếu");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblTitle.setBounds(30, 24, 300, 32);
            add(lblTitle);

            add(createLabel("Phim", 30, 86));
            add(createInput(new JComboBox<>(new String[]{
                    "Biệt đội siêu anh hùng: Hồi kết",
                    "Người Nhện: Không còn nhà",
                    "Doraemon: Nobita và vùng đất lý tưởng trên bầu trời",
                    "Quỷ nhập tràng 2",
                    "Minions: Sự trỗi dậy của Gru"
            }), 30, 116, 260, 44));

            add(createLabel("Phòng", 314, 86));
            add(createInput(new JComboBox<>(new String[]{"Phòng 1", "Phòng 2", "Phòng 3", "Phòng 4", "Phòng 5"}), 314, 116, 260, 44));

            add(createLabel("Ngày chiếu", 30, 184));
            add(createInput(new JTextField(), 30, 214, 260, 44));

            add(createLabel("Giờ chiếu", 314, 184));
            add(createInput(new JTextField(), 314, 214, 260, 44));

            add(createLabel("Định dạng", 30, 282));
            add(createInput(new JComboBox<>(new String[]{"2D", "3D", "VIP", "IMAX", "COUPLE"}), 30, 312, 260, 44));

            add(createLabel("Trạng thái", 314, 282));
            add(createInput(new JComboBox<>(new String[]{"Sắp chiếu", "Đang mở bán", "Đã chiếu"}), 314, 312, 260, 44));

            JButton btnCancel = new JButton("Đóng");
            btnCancel.setBounds(364, 382, 100, 40);
            btnCancel.setFocusPainted(false);
            btnCancel.setBackground(Color.WHITE);
            btnCancel.setBorder(new LineBorder(BORDER_COLOR));
            btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancel.addActionListener(e -> dispose());
            add(btnCancel);

            JButton btnSave = new JButton("Lưu");
            btnSave.setBounds(474, 382, 100, 40);
            btnSave.setFocusPainted(false);
            btnSave.setForeground(Color.WHITE);
            btnSave.setBackground(PRIMARY_BLUE);
            btnSave.setBorder(new LineBorder(PRIMARY_BLUE));
            btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSave.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đã lưu giao diện mẫu"));
            add(btnSave);
        }

        private JLabel createLabel(String text, int x, int y) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            label.setBounds(x, y, 120, 22);
            return label;
        }

        private JComponent createInput(JComponent component, int x, int y, int width, int height) {
            component.setBounds(x, y, width, height);
            component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            component.setBackground(Color.WHITE);
            component.setForeground(TEXT_DARK);
            component.setBorder(new CompoundBorder(new LineBorder(BORDER_COLOR), new EmptyBorder(0, 12, 0, 12)));
            return component;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Demo quản lý suất chiếu");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(1320, 900);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setLayout(new BorderLayout());

            JPanel sidebar = new JPanel();
            sidebar.setPreferredSize(new Dimension(210, 900));
            sidebar.setBackground(new Color(27, 121, 255));
            sidebar.setLayout(null);

            JLabel brand = new JLabel("Beta Cinema");
            brand.setForeground(Color.WHITE);
            brand.setFont(new Font("Segoe UI", Font.BOLD, 26));
            brand.setBounds(24, 34, 160, 34);
            sidebar.add(brand);

            JLabel menu = new JLabel("Suất chiếu");
            menu.setForeground(Color.WHITE);
            menu.setFont(new Font("Segoe UI", Font.BOLD, 18));
            menu.setOpaque(true);
            menu.setBackground(new Color(58, 144, 255));
            menu.setBounds(16, 180, 178, 46);
            menu.setBorder(new EmptyBorder(0, 18, 0, 0));
            sidebar.add(menu);

            frame.add(sidebar, BorderLayout.WEST);
            frame.add(new SuatChieuManagerPanel(), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
