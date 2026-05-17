package cinema.form.panel;

import cinema.dao.MovieDAO;
import cinema.dao.RoomDAO;
import cinema.dao.ShowTimeDAO;
import cinema.models.Movie;
import cinema.models.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SuatChieuManagerPanel extends javax.swing.JPanel {

    private final Color BACKGROUND = new Color(248, 250, 252);
    private final Color BLUE = new Color(0, 146, 255);
    private final Color ORANGE = new Color(245, 157, 35);
    private final Color BORDER = new Color(204, 204, 204);
    private final Color HEADER = new Color(235, 235, 235);

    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###");

    private ShowTimeDAO showTimeDAO = new ShowTimeDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private RoomDAO roomDAO = new RoomDAO();

    private JPanel pMain;

    private JTextField txtSearch;
    private JSpinner spnDate;
    private JCheckBox chkAllDate;
    private JComboBox<ComboItem> cboRoom;
    private JComboBox<String> cboStatus;

    private JButton btnAdd;
    private JButton btnEdit;

    private JTable tblShowtime;
    private DefaultTableModel showtimeModel;
    private JScrollPane tableScrollPane;

    public SuatChieuManagerPanel() {
        initComponents();
        buildCustomUI();
        loadRoomCombobox();
        loadShowtimeData();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setBackground(BACKGROUND);
        setPreferredSize(new java.awt.Dimension(759, 779));
        setLayout(new BorderLayout());
    }

    private void buildCustomUI() {
        removeAll();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        pMain = new JPanel();
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBackground(BACKGROUND);

        pMain.add(createFilterPanel());
        pMain.add(Box.createVerticalStrut(50));
        pMain.add(createTitleActionPanel());
        pMain.add(Box.createVerticalStrut(20));
        pMain.add(createTablePanel());

        add(pMain, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createFilterPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(Color.WHITE);
        outer.setBorder(new LineBorder(BORDER, 1));
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        outer.setPreferredSize(new Dimension(1000, 110));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        txtSearch = createTextField("Tìm theo mã suất chiếu / tên phim");

        cboRoom = new JComboBox<>();
        cboRoom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboRoom.setBackground(Color.WHITE);

        spnDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnDate, "yyyy-MM-dd");
        spnDate.setEditor(dateEditor);
        spnDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnDate.setBorder(new LineBorder(new Color(220, 220, 220), 1));

        chkAllDate = new JCheckBox("Tất cả");
        chkAllDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkAllDate.setBackground(Color.WHITE);
        chkAllDate.setSelected(true);

        JPanel dateBox = new JPanel(new BorderLayout(8, 0));
        dateBox.setBackground(Color.WHITE);
        dateBox.add(spnDate, BorderLayout.CENTER);
        dateBox.add(chkAllDate, BorderLayout.EAST);

        cboStatus = new JComboBox<>(new String[]{
                "Tất cả",
                "Đang mở",
                "Đã kết thúc",
                "Đã hủy",
                "Ngừng bán"
        });
        cboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboStatus.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0.35;
        form.add(createFilterItem("Tìm kiếm", txtSearch), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.20;
        form.add(createFilterItem("Phòng", cboRoom), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.22;
        form.add(createFilterItem("Ngày chiếu", dateBox), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.18;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(createFilterItem("Trạng thái", cboStatus), gbc);

        txtSearch.addActionListener(e -> searchShowtime());
        cboRoom.addActionListener(e -> searchShowtime());
        cboStatus.addActionListener(e -> searchShowtime());

        spnDate.addChangeListener(e -> {
            if (!chkAllDate.isSelected()) {
                searchShowtime();
            }
        });

        chkAllDate.addActionListener(e -> searchShowtime());

        outer.add(form, BorderLayout.CENTER);
        return outer;
    }

    private JPanel createFilterItem(String label, JComponent input) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lbl.setForeground(Color.BLACK);

        input.setPreferredSize(new Dimension(180, 36));

        if (!(input instanceof JPanel)) {
            input.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        }

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTitleActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel title = new JLabel("Danh sách suất chiếu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.BLACK);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        actionPanel.setBackground(BACKGROUND);

        btnAdd = createButton("+  Thêm suất chiếu", BLUE, 210);
        btnEdit = createButton("Sửa", ORANGE, 120);

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);

        panel.add(title, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        panel.setPreferredSize(new Dimension(1000, 500));

        String[] columns = {
                "Mã SC",
                "Phim",
                "Phòng",
                "Ngày chiếu",
                "Giờ bắt đầu",
                "Giờ kết thúc",
                "Giá cơ bản",
                "Trạng thái"
        };

        showtimeModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblShowtime = new JTable(showtimeModel);
        tblShowtime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblShowtime.setRowHeight(40);
        tblShowtime.setBackground(Color.WHITE);
        tblShowtime.setSelectionBackground(new Color(220, 237, 255));
        tblShowtime.setSelectionForeground(Color.BLACK);

        tblShowtime.setShowGrid(true);
        tblShowtime.setShowHorizontalLines(true);
        tblShowtime.setShowVerticalLines(true);
        tblShowtime.setGridColor(new Color(225, 225, 225));
        tblShowtime.setIntercellSpacing(new Dimension(1, 1));
        tblShowtime.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblShowtime.setFillsViewportHeight(false);

        tblShowtime.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblShowtime.getTableHeader().setBackground(HEADER);
        tblShowtime.getTableHeader().setForeground(Color.BLACK);
        tblShowtime.getTableHeader().setPreferredSize(new Dimension(100, 40));
        tblShowtime.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setBackground(HEADER);
                label.setForeground(Color.BLACK);
                label.setOpaque(true);
                label.setBorder(new LineBorder(new Color(200, 200, 200), 1));

                return label;
            }
        };

        for (int i = 0; i < tblShowtime.getColumnModel().getColumnCount(); i++) {
            tblShowtime.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tblShowtime.getColumnCount(); i++) {
            tblShowtime.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tblShowtime.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

        tblShowtime.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblShowtime.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblShowtime.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblShowtime.getColumnModel().getColumn(3).setPreferredWidth(110);
        tblShowtime.getColumnModel().getColumn(4).setPreferredWidth(110);
        tblShowtime.getColumnModel().getColumn(5).setPreferredWidth(110);
        tblShowtime.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblShowtime.getColumnModel().getColumn(7).setPreferredWidth(100);

        tableScrollPane = new JScrollPane(tblShowtime);
        tableScrollPane.setBorder(new LineBorder(BORDER, 1));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(tableScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new PaddingTextField(placeholder);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(Color.GRAY);
        txt.setBackground(Color.WHITE);
        txt.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        addPlaceholder(txt, placeholder);
        return txt;
    }

    private JButton createButton(String text, Color color, int width) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().trim().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void loadRoomCombobox() {
        cboRoom.removeAllItems();
        cboRoom.addItem(new ComboItem("", "Tất cả phòng"));

        try {
            List<Room> rooms = roomDAO.getAll();

            for (Room r : rooms) {
                cboRoom.addItem(new ComboItem(r.getRoomId(), r.getName()));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải phòng: " + e.getMessage()
                            + "\nKiểm tra RoomDAO: rs.getString(\"'name\") phải sửa thành rs.getString(\"name\").",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadShowtimeData() {
        showtimeModel.setRowCount(0);

        try {
            List<ShowTimeDAO.ShowtimeView> list = showTimeDAO.getAllView();

            for (ShowTimeDAO.ShowtimeView s : list) {
                addShowtimeRow(s);
            }

            resizeTableHeightByData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải danh sách suất chiếu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchShowtime() {
        String keyword = txtSearch.getText().trim();

        if (keyword.equals("Tìm theo mã suất chiếu / tên phim")) {
            keyword = "";
        }

        ComboItem selectedRoom = (ComboItem) cboRoom.getSelectedItem();
        String roomId = selectedRoom == null ? "" : selectedRoom.getId();

        String date = "";
        if (!chkAllDate.isSelected()) {
            Date selectedDate = (Date) spnDate.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(selectedDate);
        }

        String selectedStatus = cboStatus.getSelectedItem() == null
                ? "Tất cả"
                : cboStatus.getSelectedItem().toString();

        showtimeModel.setRowCount(0);

        try {
            List<ShowTimeDAO.ShowtimeView> list =
                    showTimeDAO.searchView(keyword, roomId, date, "Tất cả");

            for (ShowTimeDAO.ShowtimeView s : list) {
                String displayStatus = getDisplayStatus(s);

                if (!selectedStatus.equals("Tất cả")
                        && !selectedStatus.equals(displayStatus)) {
                    continue;
                }

                addShowtimeRow(s);
            }

            resizeTableHeightByData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm kiếm suất chiếu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addShowtimeRow(ShowTimeDAO.ShowtimeView s) {
        showtimeModel.addRow(new Object[]{
                s.getShowtimeId(),
                s.getMovieTitle(),
                s.getRoomName(),
                s.getStartTime().format(DATE_FORMAT),
                s.getStartTime().format(TIME_FORMAT),
                s.getEndTime().format(TIME_FORMAT),
                MONEY_FORMAT.format(s.getBasePrice()),
                getDisplayStatus(s)
        });
    }

    private String getDisplayStatus(ShowTimeDAO.ShowtimeView s) {
        if (!s.isActive()) {
            return "Ngừng bán";
        }

        LocalDateTime now = LocalDateTime.now();

        if (s.getEndTime().isBefore(now)) {
            return "Đã kết thúc";
        }

        return "Đang mở";
    }

    private void resizeTableHeightByData() {
        int rowCount = showtimeModel.getRowCount();

        int headerHeight = 40;
        int rowHeight = 40;

        int visibleRows = Math.max(rowCount, 1);
        int height = headerHeight + visibleRows * rowHeight + 2;

        height = Math.min(height, 500);

        tblShowtime.setPreferredScrollableViewportSize(new Dimension(1000, height));
        tblShowtime.revalidate();
        tblShowtime.repaint();
    }

    private void openAddDialog() {
        AutoCreateShowtimeDialog dialog = new AutoCreateShowtimeDialog(
                SwingUtilities.getWindowAncestor(this)
        );

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadShowtimeData();
        }
    }

    private void openEditDialog() {
        int row = tblShowtime.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn suất chiếu muốn sửa!");
            return;
        }

        String showtimeId = tblShowtime.getValueAt(row, 0).toString();

        ShowtimeFormDialog dialog = new ShowtimeFormDialog(
                SwingUtilities.getWindowAncestor(this),
                "Sửa suất chiếu",
                showtimeId
        );

        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadShowtimeData();
        }
    }

    private class AutoCreateShowtimeDialog extends JDialog {

        private JSpinner spnFromDate;
        private JSpinner spnToDate;
        private JSpinner spnStartTime;
        private JSpinner spnEndTime;
        private JSpinner spnGoldenHour;

        private JLabel lblRangeInfo;
        private JLabel lblMovieTitle;

        private JTextField txtSearchMovie;
        private JCheckBox chkSelectAll;
        private JPanel movieListPanel;
        private JPanel scheduleGridPanel;

        private JButton btnCreate;
        private JButton btnSave;

        private List<MovieCheckBoxItem> movieCheckBoxItems = new ArrayList<>();
        private List<ShowTimeDAO.GeneratedShowtime> draftList = new ArrayList<>();

        private boolean saved = false;

        public AutoCreateShowtimeDialog(Window owner) {
            super(owner, "Tạo suất chiếu", ModalityType.APPLICATION_MODAL);

            buildUI();
            loadMoviesToList();

            setSize(1080, 700);
            setMinimumSize(new Dimension(1080, 700));
            setLocationRelativeTo(owner);
            setResizable(false);
        }

        public boolean isSaved() {
            return saved;
        }

        private void buildUI() {
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(Color.WHITE);
            root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            root.add(createHeaderPanel(), BorderLayout.NORTH);
            root.add(createBodyPanel(), BorderLayout.CENTER);
            root.add(createLegendPanel(), BorderLayout.SOUTH);

            setContentPane(root);
        }

        private JPanel createHeaderPanel() {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.WHITE);
            wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

            lblRangeInfo = new JLabel();
            lblRangeInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblRangeInfo.setForeground(Color.BLACK);

            JLabel note = new JLabel("Khung thời gian sẽ thay đổi theo Từ ngày - Đến ngày. Mã SC tự tăng khi bấm Tạo và Lưu.");
            note.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            note.setForeground(Color.GRAY);

            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(Color.WHITE);
            infoPanel.add(lblRangeInfo, BorderLayout.NORTH);
            infoPanel.add(note, BorderLayout.SOUTH);

            JPanel groupPanel = new JPanel(new BorderLayout());
            groupPanel.setBackground(Color.WHITE);
            groupPanel.setBorder(BorderFactory.createTitledBorder(
                    new LineBorder(BORDER, 1),
                    "Thiết lập thời gian",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP,
                    new Font("Segoe UI", Font.PLAIN, 14),
                    Color.BLACK
            ));

            JPanel leftControlPanel = new JPanel(new GridBagLayout());
            leftControlPanel.setBackground(Color.WHITE);
            leftControlPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

            spnFromDate = createDateSpinner();
            spnToDate = createDateSpinner();
            spnStartTime = createTimeSpinner("08:00");
            spnEndTime = createTimeSpinner("22:00");
            spnGoldenHour = createTimeSpinner("18:00");

            addDateTimeListeners();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 12);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            leftControlPanel.add(createTopInput("Từ ngày", spnFromDate), gbc);

            gbc.gridx = 1;
            leftControlPanel.add(createTopInput("Đến ngày", spnToDate), gbc);

            gbc.gridx = 2;
            leftControlPanel.add(createTopInput("Giờ bắt đầu", spnStartTime), gbc);

            gbc.gridx = 3;
            leftControlPanel.add(createTopInput("Giờ kết thúc", spnEndTime), gbc);

            gbc.gridx = 4;
            leftControlPanel.add(createTopInput("Giờ vàng", spnGoldenHour), gbc);

            JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 23));
            rightButtonPanel.setBackground(Color.WHITE);

            btnCreate = createMiniButton("Tạo", new Color(245, 157, 35), 80);
            btnSave = createMiniButton("Lưu", new Color(0, 150, 90), 80);

            btnCreate.addActionListener(e -> createDraftSchedule());
            btnSave.addActionListener(e -> saveDraftSchedule());

            rightButtonPanel.add(btnCreate);
            rightButtonPanel.add(btnSave);

            groupPanel.add(leftControlPanel, BorderLayout.WEST);
            groupPanel.add(rightButtonPanel, BorderLayout.EAST);

            wrapper.add(infoPanel, BorderLayout.NORTH);
            wrapper.add(groupPanel, BorderLayout.CENTER);

            updateRangeInfo();

            return wrapper;
        }

        private void addDateTimeListeners() {
            javax.swing.event.ChangeListener listener = e -> updateRangeInfo();

            spnFromDate.addChangeListener(listener);
            spnToDate.addChangeListener(listener);
            spnStartTime.addChangeListener(listener);
            spnEndTime.addChangeListener(listener);
        }

        private void updateRangeInfo() {
            LocalDate from = spinnerToLocalDate(spnFromDate);
            LocalDate to = spinnerToLocalDate(spnToDate);

            long days = ChronoUnit.DAYS.between(from, to) + 1;
            if (days < 1) {
                days = 0;
            }

            String fromText = from.format(DateTimeFormatter.ofPattern("dd/MM"));
            String toText = to.format(DateTimeFormatter.ofPattern("dd/MM"));

            LocalTime start = spinnerToLocalTime(spnStartTime);
            LocalTime end = spinnerToLocalTime(spnEndTime);

            lblRangeInfo.setText(
                    fromText + " - " + toText + " (" + days + " ngày)    "
                            + "Khung thời gian: "
                            + start.format(DateTimeFormatter.ofPattern("HH:mm"))
                            + " - "
                            + end.format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        }

        private JPanel createBodyPanel() {
            JPanel body = new JPanel(new BorderLayout(14, 0));
            body.setBackground(Color.WHITE);

            body.add(createMoviePanel(), BorderLayout.WEST);
            body.add(createSchedulePanel(), BorderLayout.CENTER);

            return body;
        }

        private JPanel createMoviePanel() {
            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBackground(Color.WHITE);
            panel.setPreferredSize(new Dimension(270, 500));

            txtSearchMovie = new PaddingTextField("Tìm kiếm phim theo tên");
            txtSearchMovie.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtSearchMovie.setForeground(Color.GRAY);
            txtSearchMovie.setPreferredSize(new Dimension(250, 36));
            txtSearchMovie.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            addPlaceholder(txtSearchMovie, "Tìm kiếm phim theo tên");
            txtSearchMovie.addActionListener(e -> filterMovies());

            lblMovieTitle = new JLabel("Danh sách phim (0)");
            lblMovieTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

            chkSelectAll = new JCheckBox("Chọn tất cả");
            chkSelectAll.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            chkSelectAll.setBackground(Color.WHITE);
            chkSelectAll.addActionListener(e -> selectAllMovies());

            JPanel titleRow = new JPanel(new BorderLayout());
            titleRow.setBackground(Color.WHITE);
            titleRow.add(lblMovieTitle, BorderLayout.WEST);
            titleRow.add(chkSelectAll, BorderLayout.EAST);

            movieListPanel = new JPanel();
            movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
            movieListPanel.setBackground(Color.WHITE);

            JScrollPane scrollPane = new JScrollPane(movieListPanel);
            scrollPane.setBorder(new LineBorder(BORDER, 1));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            JPanel top = new JPanel(new BorderLayout(0, 8));
            top.setBackground(Color.WHITE);
            top.add(txtSearchMovie, BorderLayout.NORTH);
            top.add(titleRow, BorderLayout.SOUTH);

            JLabel note = new JLabel("<html>Chọn phim cần tạo suất chiếu, sau đó bấm Tạo để sinh lịch nháp.</html>");
            note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            note.setForeground(Color.GRAY);
            note.setBorder(new LineBorder(BORDER, 1));
            note.setPreferredSize(new Dimension(250, 55));

            panel.add(top, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(note, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createSchedulePanel() {
            scheduleGridPanel = new JPanel(new BorderLayout());
            scheduleGridPanel.setBackground(Color.WHITE);
            scheduleGridPanel.setBorder(new LineBorder(BORDER, 1));

            JLabel empty = new JLabel("Chọn phim và bấm Tạo để xem lịch suất chiếu dự kiến", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setForeground(Color.GRAY);

            scheduleGridPanel.add(empty, BorderLayout.CENTER);

            return scheduleGridPanel;
        }

        private JPanel createLegendPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 8));
            panel.setBackground(Color.WHITE);

            panel.add(createLegendItem(new Color(232, 245, 238), "Phòng 1 / suất thường"));
            panel.add(createLegendItem(new Color(244, 232, 255), "Phòng khác"));
            panel.add(createLegendItem(new Color(255, 246, 214), "Giờ vàng"));

            return panel;
        }

        private JPanel createLegendItem(Color color, String text) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            panel.setBackground(Color.WHITE);

            JLabel box = new JLabel();
            box.setOpaque(true);
            box.setBackground(color);
            box.setPreferredSize(new Dimension(18, 18));
            box.setBorder(new LineBorder(BORDER, 1));

            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            panel.add(box);
            panel.add(label);

            return panel;
        }

        private JPanel createTopInput(String label, JComponent input) {
            JPanel panel = new JPanel(new BorderLayout(0, 5));
            panel.setBackground(Color.WHITE);

            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            input.setPreferredSize(new Dimension(120, 34));

            panel.add(lbl, BorderLayout.NORTH);
            panel.add(input, BorderLayout.CENTER);

            return panel;
        }

        private JSpinner createDateSpinner() {
            JSpinner spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
            spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            spinner.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            return spinner;
        }

        private JSpinner createTimeSpinner(String value) {
            Calendar cal = Calendar.getInstance();
            String[] parts = value.split(":");

            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
            cal.set(Calendar.SECOND, 0);

            JSpinner spinner = new JSpinner(new SpinnerDateModel(
                    cal.getTime(),
                    null,
                    null,
                    Calendar.MINUTE
            ));

            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
            spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            spinner.setBorder(new LineBorder(new Color(220, 220, 220), 1));

            return spinner;
        }

        private JButton createMiniButton(String text, Color color, int width) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(width, 36));
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        private void loadMoviesToList() {
            movieCheckBoxItems.clear();
            movieListPanel.removeAll();

            try {
                List<Movie> movies = movieDAO.getAll();

                for (Movie movie : movies) {
                    MovieCheckBoxItem item = new MovieCheckBoxItem(movie);
                    movieCheckBoxItems.add(item);
                    movieListPanel.add(item);
                    movieListPanel.add(Box.createVerticalStrut(8));
                }

                lblMovieTitle.setText("Danh sách phim (" + movies.size() + ")");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi tải danh sách phim: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

            movieListPanel.revalidate();
            movieListPanel.repaint();
        }

        private void filterMovies() {
            String keyword = txtSearchMovie.getText().trim().toLowerCase();

            if (keyword.equals("tìm kiếm phim theo tên")) {
                keyword = "";
            }

            movieListPanel.removeAll();

            int visibleCount = 0;

            for (MovieCheckBoxItem item : movieCheckBoxItems) {
                if (item.getMovieTitle().toLowerCase().contains(keyword)) {
                    movieListPanel.add(item);
                    movieListPanel.add(Box.createVerticalStrut(8));
                    visibleCount++;
                }
            }

            lblMovieTitle.setText("Danh sách phim (" + visibleCount + ")");

            movieListPanel.revalidate();
            movieListPanel.repaint();
        }

        private void selectAllMovies() {
            boolean selected = chkSelectAll.isSelected();

            for (MovieCheckBoxItem item : movieCheckBoxItems) {
                item.setSelectedMovie(selected);
            }
        }

        private void createDraftSchedule() {
            try {
                List<String> selectedMovieIds = new ArrayList<>();

                for (MovieCheckBoxItem item : movieCheckBoxItems) {
                    if (item.isSelectedMovie()) {
                        selectedMovieIds.add(item.getMovieId());
                    }
                }

                if (selectedMovieIds.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bạn cần chọn ít nhất 1 phim.");
                    return;
                }

                LocalDate fromDate = spinnerToLocalDate(spnFromDate);
                LocalDate toDate = spinnerToLocalDate(spnToDate);
                LocalTime startTime = spinnerToLocalTime(spnStartTime);
                LocalTime endTime = spinnerToLocalTime(spnEndTime);
                LocalTime goldenHour = spinnerToLocalTime(spnGoldenHour);

                if (toDate.isBefore(fromDate)) {
                    JOptionPane.showMessageDialog(this, "Đến ngày phải lớn hơn hoặc bằng từ ngày.");
                    return;
                }

                if (!endTime.isAfter(startTime)) {
                    JOptionPane.showMessageDialog(this, "Giờ kết thúc phải sau giờ bắt đầu.");
                    return;
                }

                draftList = showTimeDAO.buildAutoSchedule(
                        fromDate,
                        toDate,
                        startTime,
                        endTime,
                        goldenHour,
                        selectedMovieIds,
                        90000,
                        30000,
                        50000
                );

                renderDraftCalendar();

                JOptionPane.showMessageDialog(this,
                        "Đã tạo " + draftList.size() + " suất chiếu dự kiến.\nMã SC đã được sinh tự động.");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi tạo lịch suất chiếu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveDraftSchedule() {
            try {
                if (draftList == null || draftList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bạn chưa tạo lịch suất chiếu.");
                    return;
                }

                int count = showTimeDAO.insertGeneratedShowtimes(draftList);

                saved = true;

                JOptionPane.showMessageDialog(this,
                        "Lưu thành công " + count + " suất chiếu.\nDanh sách suất chiếu sẽ tự tải lại.");

                dispose();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi lưu suất chiếu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void renderDraftCalendar() {
            scheduleGridPanel.removeAll();

            CalendarBoardPanel board = new CalendarBoardPanel(
                    draftList,
                    spinnerToLocalDate(spnFromDate),
                    spinnerToLocalDate(spnToDate),
                    spinnerToLocalTime(spnStartTime),
                    spinnerToLocalTime(spnEndTime),
                    spinnerToLocalTime(spnGoldenHour)
            );

            JScrollPane scrollPane = new JScrollPane(board);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(Color.WHITE);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

            scheduleGridPanel.add(scrollPane, BorderLayout.CENTER);
            scheduleGridPanel.revalidate();
            scheduleGridPanel.repaint();
        }

        private LocalDate spinnerToLocalDate(JSpinner spinner) {
            Date date = (Date) spinner.getValue();

            return date.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
        }

        private LocalTime spinnerToLocalTime(JSpinner spinner) {
            Date date = (Date) spinner.getValue();

            return date.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalTime()
                    .withSecond(0)
                    .withNano(0);
        }
    }

    private class CalendarBoardPanel extends JPanel {

        private List<ShowTimeDAO.GeneratedShowtime> showtimes;
        private LocalDate fromDate;
        private LocalDate toDate;
        private LocalTime openTime;
        private LocalTime closeTime;
        private LocalTime goldenHour;

        private final int leftWidth = 55;
        private final int topHeight = 50;
        private final int bottomSpace = 20;
        private final int dayWidth = 190;
        private final int hourHeight = 70;

        public CalendarBoardPanel(List<ShowTimeDAO.GeneratedShowtime> showtimes,
                                  LocalDate fromDate,
                                  LocalDate toDate,
                                  LocalTime openTime,
                                  LocalTime closeTime,
                                  LocalTime goldenHour) {
            this.showtimes = showtimes;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.openTime = openTime;
            this.closeTime = closeTime;
            this.goldenHour = goldenHour;

            int days = (int) ChronoUnit.DAYS.between(fromDate, toDate) + 1;
            int hours = Math.max(1, closeTime.getHour() - openTime.getHour());

            setPreferredSize(new Dimension(leftWidth + days * dayWidth + 40,
                    topHeight + hours * hourHeight + bottomSpace));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(g2);
            drawGoldenHour(g2);
            drawShowtimeBlocks(g2);

            g2.dispose();
        }

        private void drawGrid(Graphics2D g2) {
            int days = (int) ChronoUnit.DAYS.between(fromDate, toDate) + 1;
            int startHour = openTime.getHour();
            int endHour = closeTime.getHour();

            g2.setColor(new Color(245, 245, 245));
            g2.fillRect(leftWidth, topHeight, days * dayWidth, (endHour - startHour) * hourHeight);

            g2.setColor(new Color(225, 225, 225));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            for (int d = 0; d < days; d++) {
                int x = leftWidth + d * dayWidth;

                g2.setColor(new Color(235, 235, 235));
                g2.fillRect(x, 0, dayWidth, topHeight);

                g2.setColor(new Color(210, 210, 210));
                g2.drawRect(x, 0, dayWidth, getHeight() - bottomSpace);

                LocalDate date = fromDate.plusDays(d);

                String dayName = getVietnameseDayName(date);
                String dateText = date.format(DateTimeFormatter.ofPattern("dd/MM"));

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                drawCenteredText(g2, dayName, x, 18, dayWidth);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                drawCenteredText(g2, dateText, x, 36, dayWidth);
            }

            for (int h = startHour; h <= endHour; h++) {
                int y = topHeight + (h - startHour) * hourHeight;

                g2.setColor(new Color(225, 225, 225));
                g2.drawLine(leftWidth, y, leftWidth + days * dayWidth, y);

                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.drawString(String.format("%02d:00", h), 8, y + 5);
            }
        }

        private void drawGoldenHour(Graphics2D g2) {
            if (goldenHour == null) {
                return;
            }

            int days = (int) ChronoUnit.DAYS.between(fromDate, toDate) + 1;
            int startHour = openTime.getHour();

            int y = topHeight + (int) ChronoUnit.MINUTES.between(
                    LocalTime.of(startHour, 0),
                    goldenHour
            ) * hourHeight / 60;

            int h = 2 * hourHeight;

            g2.setColor(new Color(255, 246, 214));
            g2.fillRect(leftWidth, y, days * dayWidth, h);

            g2.setColor(new Color(230, 200, 120));
            g2.drawLine(leftWidth, y, leftWidth + days * dayWidth, y);
        }

        private void drawShowtimeBlocks(Graphics2D g2) {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

            for (ShowTimeDAO.GeneratedShowtime s : showtimes) {
                int dayIndex = (int) ChronoUnit.DAYS.between(fromDate, s.getStartTime().toLocalDate());

                if (dayIndex < 0) {
                    continue;
                }

                LocalTime baseTime = LocalTime.of(openTime.getHour(), 0);

                int x = leftWidth + dayIndex * dayWidth + 8;
                int y = topHeight + (int) ChronoUnit.MINUTES.between(baseTime, s.getStartTime().toLocalTime()) * hourHeight / 60;
                int h = Math.max(40, (int) ChronoUnit.MINUTES.between(s.getStartTime(), s.getEndTime()) * hourHeight / 60);
                int w = dayWidth - 16;

                Color fill;
                Color border;

                if (s.isGoldenHour()) {
                    fill = new Color(255, 246, 214);
                    border = new Color(235, 190, 90);
                } else if (s.getRoomName().contains("1")) {
                    fill = new Color(232, 245, 238);
                    border = new Color(100, 190, 145);
                } else {
                    fill = new Color(244, 232, 255);
                    border = new Color(175, 120, 220);
                }

                g2.setColor(fill);
                g2.fillRoundRect(x, y, w, h, 8, 8);

                g2.setColor(border);
                g2.drawRoundRect(x, y, w, h, 8, 8);

                g2.setColor(new Color(70, 45, 95));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));

                String title = shortenText(s.getMovieTitle(), 24);
                g2.drawString(title, x + 8, y + 18);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String roomTime = s.getRoomName() + "   "
                        + s.getStartTime().format(timeFormat)
                        + " - "
                        + s.getEndTime().format(timeFormat);

                g2.drawString(roomTime, x + 8, y + 36);

                if (s.isGoldenHour()) {
                    g2.setColor(new Color(170, 120, 30));
                    g2.drawString("Giờ vàng", x + 8, y + 54);
                }
            }
        }

        private String getVietnameseDayName(LocalDate date) {
            switch (date.getDayOfWeek()) {
                case MONDAY:
                    return "Th 2";
                case TUESDAY:
                    return "Th 3";
                case WEDNESDAY:
                    return "Th 4";
                case THURSDAY:
                    return "Th 5";
                case FRIDAY:
                    return "Th 6";
                case SATURDAY:
                    return "Th 7";
                case SUNDAY:
                    return "CN";
                default:
                    return "";
            }
        }

        private void drawCenteredText(Graphics2D g2, String text, int x, int y, int width) {
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (width - fm.stringWidth(text)) / 2;
            g2.drawString(text, textX, y);
        }

        private String shortenText(String text, int max) {
            if (text == null) {
                return "";
            }

            if (text.length() <= max) {
                return text;
            }

            return text.substring(0, max - 3) + "...";
        }
    }

    private class MovieCheckBoxItem extends JPanel {

        private Movie movie;
        private JCheckBox checkBox;

        public MovieCheckBoxItem(Movie movie) {
            this.movie = movie;

            setLayout(new BorderLayout());
            setBackground(new Color(248, 246, 255));
            setBorder(new LineBorder(new Color(205, 190, 255), 1));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
            setPreferredSize(new Dimension(240, 72));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.setOpaque(false);
            textPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));

            JLabel lblTitle = new JLabel(shortenTitle(movie.getTitle()));
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblTitle.setForeground(Color.BLACK);
            lblTitle.setToolTipText(movie.getTitle());

            JLabel lblDuration = new JLabel("Thời lượng: " + movie.getDuration() + " phút");
            lblDuration.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblDuration.setForeground(Color.GRAY);

            textPanel.add(lblTitle);
            textPanel.add(lblDuration);

            checkBox = new JCheckBox();
            checkBox.setBackground(new Color(248, 246, 255));
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);

            add(textPanel, BorderLayout.CENTER);
            add(checkBox, BorderLayout.EAST);

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    checkBox.setSelected(!checkBox.isSelected());
                }
            });
        }

        public boolean isSelectedMovie() {
            return checkBox.isSelected();
        }

        public void setSelectedMovie(boolean selected) {
            checkBox.setSelected(selected);
        }

        public String getMovieId() {
            return movie.getId();
        }

        public String getMovieTitle() {
            return movie.getTitle();
        }

        private String shortenTitle(String title) {
            if (title == null) {
                return "";
            }

            if (title.length() <= 32) {
                return title;
            }

            return title.substring(0, 29) + "...";
        }
    }

    private class ShowtimeFormDialog extends JDialog {

        private JTextField txtShowtimeId;
        private JComboBox<ComboItem> cboMovie;
        private JComboBox<ComboItem> cboRoomForm;
        private JTextField txtStartTime;
        private JTextField txtBasePrice;
        private JTextField txtVipExtra;
        private JTextField txtCoupleExtra;
        private JComboBox<String> cboActive;

        private boolean saved = false;
        private String editingId;

        public ShowtimeFormDialog(Window owner, String title, String editingId) {
            super(owner, title, ModalityType.APPLICATION_MODAL);
            this.editingId = editingId;

            buildFormUI();
            loadMovieCombobox();
            loadRoomFormCombobox();

            if (editingId != null) {
                loadEditData(editingId);
            } else {
                txtShowtimeId.setText(generateShowtimeId());
                txtVipExtra.setText("30000");
                txtCoupleExtra.setText("50000");
                cboActive.setSelectedItem("Đang mở");
            }

            setSize(680, 610);
            setMinimumSize(new Dimension(680, 610));
            setLocationRelativeTo(owner);
            setResizable(false);
        }

        public boolean isSaved() {
            return saved;
        }

        private void buildFormUI() {
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(Color.WHITE);
            root.setBorder(BorderFactory.createEmptyBorder(26, 30, 26, 30));

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(Color.WHITE);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Color.WHITE);
            form.setAlignmentX(Component.LEFT_ALIGNMENT);

            txtShowtimeId = createDialogTextField();
            cboMovie = createDialogComboBox();
            cboRoomForm = createDialogComboBox();
            txtStartTime = createDialogTextField();
            txtBasePrice = createDialogTextField();
            txtVipExtra = createDialogTextField();
            txtCoupleExtra = createDialogTextField();
            cboActive = createStatusComboBox();

            txtStartTime.setToolTipText("Nhập dạng yyyy-mm-dd hh:mm. Ví dụ: 2025-12-25 08:45");

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;

            addFormRow(form, gbc, 0, "Mã suất chiếu:", txtShowtimeId);
            addFormRow(form, gbc, 1, "Phim:", cboMovie);
            addFormRow(form, gbc, 2, "Phòng:", cboRoomForm);
            addFormRow(form, gbc, 3, "Giờ bắt đầu:", txtStartTime);
            addFormRow(form, gbc, 4, "Giá cơ bản:", txtBasePrice);
            addFormRow(form, gbc, 5, "Phụ thu VIP:", txtVipExtra);
            addFormRow(form, gbc, 6, "Phụ thu ghế đôi:", txtCoupleExtra);
            addFormRow(form, gbc, 7, "Trạng thái:", cboActive);

            JLabel note = new JLabel("Giờ kết thúc sẽ tự tính theo thời lượng phim.");
            note.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            note.setForeground(Color.GRAY);
            note.setAlignmentX(Component.LEFT_ALIGNMENT);
            note.setBorder(BorderFactory.createEmptyBorder(8, 155, 0, 0));

            content.add(form);
            content.add(Box.createVerticalStrut(8));
            content.add(note);
            content.add(Box.createVerticalStrut(20));

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            buttons.setBackground(Color.WHITE);

            JButton btnCancel = createSmallButton("Hủy", new Color(160, 160, 160));
            JButton btnSave = createSmallButton("Lưu", BLUE);

            btnCancel.addActionListener(e -> dispose());
            btnSave.addActionListener(e -> saveShowtime());

            buttons.add(btnCancel);
            buttons.add(btnSave);

            root.add(content, BorderLayout.CENTER);
            root.add(buttons, BorderLayout.SOUTH);

            setContentPane(root);
        }

        private JTextField createDialogTextField() {
            JTextField txt = new PaddingTextField("");
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txt.setPreferredSize(new Dimension(360, 36));
            txt.setMinimumSize(new Dimension(360, 36));
            txt.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            txt.setBackground(Color.WHITE);
            return txt;
        }

        private JComboBox<ComboItem> createDialogComboBox() {
            JComboBox<ComboItem> comboBox = new JComboBox<>();
            comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            comboBox.setPreferredSize(new Dimension(360, 36));
            comboBox.setMinimumSize(new Dimension(360, 36));
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(new LineBorder(new Color(220, 220, 220), 1));
            return comboBox;
        }

        private JComboBox<String> createStatusComboBox() {
            JComboBox<String> comboBox = new JComboBox<>();

            comboBox.addItem("Đang mở");
            comboBox.addItem("Ngừng bán");
            comboBox.addItem("Đã hủy");

            comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            comboBox.setPreferredSize(new Dimension(360, 36));
            comboBox.setMinimumSize(new Dimension(360, 36));
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(new LineBorder(new Color(220, 220, 220), 1));

            return comboBox;
        }

        private JButton createSmallButton(String text, Color color) {
            JButton btn = new JButton(text);
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return btn;
        }

        private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent input) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lbl.setForeground(Color.BLACK);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.25;
            gbc.insets = new Insets(7, 0, 7, 18);
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 0.75;
            gbc.insets = new Insets(7, 0, 7, 0);
            panel.add(input, gbc);
        }

        private void loadMovieCombobox() {
            cboMovie.removeAllItems();

            try {
                List<Movie> movies = movieDAO.getAll();

                for (Movie m : movies) {
                    cboMovie.addItem(new ComboItem(m.getId(), m.getTitle()));
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi tải phim: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadRoomFormCombobox() {
            cboRoomForm.removeAllItems();

            try {
                List<Room> rooms = roomDAO.getAll();

                for (Room r : rooms) {
                    cboRoomForm.addItem(new ComboItem(r.getRoomId(), r.getName()));
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi tải phòng: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadEditData(String showtimeId) {
            try {
                ShowTimeDAO.ShowtimeView s = showTimeDAO.getViewById(showtimeId);

                if (s == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu!");
                    dispose();
                    return;
                }

                txtShowtimeId.setText(s.getShowtimeId());
                txtShowtimeId.setEditable(false);

                selectComboItem(cboMovie, s.getMovieId());
                selectComboItem(cboRoomForm, s.getRoomId());

                txtStartTime.setText(s.getStartTime().format(DATETIME_FORMAT));
                txtBasePrice.setText(String.valueOf((long) s.getBasePrice()));
                txtVipExtra.setText(String.valueOf((long) s.getVipExtra()));
                txtCoupleExtra.setText(String.valueOf((long) s.getCoupleExtra()));

                cboActive.setSelectedItem(s.isActive() ? "Đang mở" : "Ngừng bán");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi tải chi tiết suất chiếu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void selectComboItem(JComboBox<ComboItem> comboBox, String id) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                ComboItem item = comboBox.getItemAt(i);

                if (item.getId().equals(id)) {
                    comboBox.setSelectedIndex(i);
                    return;
                }
            }
        }

        private String generateShowtimeId() {
            return "SC" + System.currentTimeMillis() % 100000;
        }

        private boolean validateForm() {
            if (txtShowtimeId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã suất chiếu không được để trống!");
                return false;
            }

            if (cboMovie.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn phim!");
                return false;
            }

            if (cboRoomForm.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Bạn chưa chọn phòng!");
                return false;
            }

            try {
                LocalDateTime.parse(txtStartTime.getText().trim(), DATETIME_FORMAT);

                double basePrice = Double.parseDouble(txtBasePrice.getText().trim());
                double vipExtra = Double.parseDouble(txtVipExtra.getText().trim());
                double coupleExtra = Double.parseDouble(txtCoupleExtra.getText().trim());

                if (basePrice < 0 || vipExtra < 0 || coupleExtra < 0) {
                    JOptionPane.showMessageDialog(this, "Giá và phụ thu phải lớn hơn hoặc bằng 0!");
                    return false;
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Dữ liệu chưa đúng.\nGiờ nhập dạng: yyyy-mm-dd hh:mm\nVí dụ: 2025-12-25 08:45",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        private void saveShowtime() {
            if (!validateForm()) {
                return;
            }

            try {
                String showtimeId = txtShowtimeId.getText().trim();
                ComboItem movie = (ComboItem) cboMovie.getSelectedItem();
                ComboItem room = (ComboItem) cboRoomForm.getSelectedItem();

                LocalDateTime startTime = LocalDateTime.parse(
                        txtStartTime.getText().trim(),
                        DATETIME_FORMAT
                );

                double basePrice = Double.parseDouble(txtBasePrice.getText().trim());
                double vipExtra = Double.parseDouble(txtVipExtra.getText().trim());
                double coupleExtra = Double.parseDouble(txtCoupleExtra.getText().trim());

                String status = cboActive.getSelectedItem().toString();
                boolean active = "Đang mở".equals(status);

                showTimeDAO.update(
                        showtimeId,
                        movie.getId(),
                        room.getId(),
                        startTime,
                        basePrice,
                        vipExtra,
                        coupleExtra,
                        active
                );

                JOptionPane.showMessageDialog(this, "Sửa suất chiếu thành công!");
                saved = true;
                dispose();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi lưu suất chiếu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class ComboItem {
        private String id;
        private String name;

        public ComboItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class PaddingTextField extends JTextField {
        private final Insets insets = new Insets(0, 12, 0, 12);

        public PaddingTextField(String text) {
            super(text);
            setBorder(new EmptyBorder(insets));
        }

        @Override
        public Insets getInsets() {
            return insets;
        }

        @Override
        public Insets getInsets(Insets insets) {
            insets.left = this.insets.left;
            insets.right = this.insets.right;
            insets.top = this.insets.top;
            insets.bottom = this.insets.bottom;
            return insets;
        }
    }
}
