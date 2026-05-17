package cinema.form.panel;

import cinema.dao.RoomDAO;
import cinema.dao.SeatDao;
import cinema.enums.SeatType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PhongManagerPanel extends javax.swing.JPanel {

    private final Color BACKGROUND = new Color(248, 250, 252);
    private final Color BLUE = new Color(0, 146, 255);
    private final Color ORANGE = new Color(245, 157, 35);
    private final Color BORDER = new Color(204, 204, 204);
    private final Color HEADER = new Color(235, 235, 235);

    private final Color SEAT_REGULAR = new Color(46, 132, 235);
    private final Color SEAT_VIP = new Color(245, 181, 32);
    private final Color SEAT_COUPLE = new Color(235, 93, 160);
    private final Color SEAT_EMPTY = Color.WHITE;
    private final Color SEAT_BOOKED = new Color(220, 220, 220);
    private final Color SEAT_SELECTED = new Color(34, 197, 94);
    private boolean[][] selectedSeats;

    private RoomDAO roomDAO = new RoomDAO();
    private SeatDao seatDao = new SeatDao();

    private JPanel pMain;
    private JTable tblRoom;
    private DefaultTableModel roomModel;

    private JTextField txtSearch;
    private JComboBox<String> cboSearchStatus;

    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnCancel;

    private JTextField txtRoomId;
    private JTextField txtRoomName;
    private JTextField txtCapacity;
    private JTextField txtRows;
    private JTextField txtCols;
    private JComboBox<String> cboStatus;
    private JTextArea txtNote;

    private JPanel seatMapPanel;
    private JButton btnEmptySeat;
    private JButton btnRegularSeat;
    private JButton btnVipSeat;
    private JButton btnCoupleSeat;
    private JButton btnBookedSeat;
    private JButton btnSelectedSeat;

    private boolean addMode = false;
    private boolean editMode = false;
    private boolean loadingForm = false;

    private String selectedSeatType = "REGULAR";
    private String[][] draftSeatTypes;

    public PhongManagerPanel() {
        initComponents();
        buildCustomUI();
        loadRoomData();
    }

    private void buildCustomUI() {
        removeAll();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        pMain = new JPanel();
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBackground(Color.WHITE);
        pMain.setBorder(new LineBorder(BORDER, 1));

        pMain.add(createTopPanel());
        pMain.add(createTablePanel());
        pMain.add(Box.createVerticalStrut(12));
        pMain.add(createBottomPanel());

        add(pMain, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 12, 18));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        panel.setPreferredSize(new Dimension(1000, 130));

        JLabel title = new JLabel("DANH SÁCH PHÒNG CHIẾU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Color.BLACK);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(Color.WHITE);

        btnAdd = createButton("+  Thêm phòng", BLUE, 150);
        btnEdit = createButton("Sửa phòng", ORANGE, 150);

        btnAdd.addActionListener(e -> prepareAdd());
        btnEdit.addActionListener(e -> prepareEdit());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        cboSearchStatus = new JComboBox<>(new String[]{
                "Tất cả trạng thái",
                "Đang hoạt động",
                "Ngừng hoạt động"
        });
        cboSearchStatus.setPreferredSize(new Dimension(170, 38));
        cboSearchStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboSearchStatus.setBackground(Color.WHITE);
        cboSearchStatus.addActionListener(e -> searchRoom());

        txtSearch = new JTextField("Tìm kiếm phòng...");
        txtSearch.setPreferredSize(new Dimension(260, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        addPlaceholder(txtSearch, "Tìm kiếm phòng...");

        JButton btnSearch = new JButton("⌕");
        btnSearch.setPreferredSize(new Dimension(46, 38));
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSearch.addActionListener(e -> searchRoom());
        txtSearch.addActionListener(e -> searchRoom());

        searchPanel.add(cboSearchStatus);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel row2 = new JPanel(new BorderLayout());
        row2.setBackground(Color.WHITE);
        row2.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
        row2.add(actionPanel, BorderLayout.WEST);
        row2.add(searchPanel, BorderLayout.EAST);

        panel.add(title, BorderLayout.NORTH);
        panel.add(row2, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.setPreferredSize(new Dimension(1000, 250));

        String[] columns = {
                "Mã phòng",
                "Tên phòng",
                "Sức chứa",
                "Số hàng",
                "Số cột",
                "Trạng thái",
                "Ghi chú"
        };

        roomModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblRoom = new JTable(roomModel);
        tblRoom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblRoom.setRowHeight(42);
        tblRoom.setSelectionBackground(new Color(220, 237, 255));
        tblRoom.setSelectionForeground(Color.BLACK);
        tblRoom.setShowVerticalLines(false);
        tblRoom.setShowHorizontalLines(true);
        tblRoom.setGridColor(HEADER);
        tblRoom.setBackground(Color.WHITE);
        tblRoom.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tblRoom.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tblRoom.getTableHeader().setBackground(HEADER);
        tblRoom.getTableHeader().setForeground(Color.BLACK);
        tblRoom.getTableHeader().setPreferredSize(new Dimension(100, 42));

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
                label.setBorder(new LineBorder(HEADER, 1));

                return label;
            }
        };

        for (int i = 0; i < tblRoom.getColumnModel().getColumnCount(); i++) {
            tblRoom.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBackground(Color.WHITE);

        for (int i = 0; i < tblRoom.getColumnCount(); i++) {
            tblRoom.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tblRoom.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblRoom);
        scrollPane.setBorder(new LineBorder(BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(1000, 240));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 430));
        panel.setPreferredSize(new Dimension(1000, 430));

        panel.add(createInfoPanel());
        panel.add(createSeatMapBox());

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1));

        JLabel title = new JLabel("THÔNG TIN PHÒNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 18, 10, 18));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(0, 18, 10, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRoomId = createTextField();
        txtRoomName = createTextField();
        txtCapacity = createTextField();
        txtRows = createTextField();
        txtCols = createTextField();
        
        txtRoomId.setEditable(false);
        txtCapacity.setEditable(false);
        txtCapacity.setBackground(new Color(245, 245, 245));

        addLayoutListener(txtRows);
        addLayoutListener(txtCols);

        cboStatus = new JComboBox<>(new String[]{"Đang hoạt động", "Ngừng hoạt động"});
        cboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboStatus.setPreferredSize(new Dimension(260, 34));

        txtNote = new JTextArea();
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNote.setLineWrap(true);
        txtNote.setWrapStyleWord(true);
        txtNote.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        txtNote.setPreferredSize(new Dimension(260, 70));

        addFormRow(form, gbc, 0, "Mã phòng:", txtRoomId);
        addFormRow(form, gbc, 1, "Tên phòng:", txtRoomName);
        addFormRow(form, gbc, 2, "Sức chứa:", txtCapacity);
        addFormRow(form, gbc, 3, "Số hàng:", txtRows);
        addFormRow(form, gbc, 4, "Số cột:", txtCols);
        addFormRow(form, gbc, 5, "Trạng thái:", cboStatus);

        JLabel lblNote = new JLabel("Ghi chú:");
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblNote, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.75;
        form.add(txtNote, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));

        btnSave = createButton("Lưu thông tin", BLUE, 150);
        btnCancel = createGrayButton("Hủy", 110);

        btnSave.addActionListener(e -> saveRoom());
        btnCancel.addActionListener(e -> cancelEdit());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setFormEnabled(false);

        return panel;
    }

    public JPanel createSeatMapBox() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(BORDER, 1));

        JLabel title = new JLabel("SƠ ĐỒ PHÒNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel screen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
        screen.setOpaque(true);
        screen.setBackground(new Color(225, 225, 225));
        screen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        screen.setPreferredSize(new Dimension(100, 32));

        seatMapPanel = new JPanel(new BorderLayout());
        seatMapPanel.setBackground(Color.WHITE);

        content.add(screen, BorderLayout.NORTH);
        content.add(seatMapPanel, BorderLayout.CENTER);
        content.add(createSeatToolPanel(), BorderLayout.SOUTH);

        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    public JPanel createSeatToolPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        panel.setBackground(Color.WHITE);
        
        btnEmptySeat = createSeatTypeButton("Ghế trống/hỏng", SEAT_EMPTY, "EMPTY");
        btnRegularSeat = createSeatTypeButton("Ghế thường", SEAT_REGULAR, "REGULAR");
        btnVipSeat = createSeatTypeButton("Ghế VIP", SEAT_VIP, "VIP");
        btnCoupleSeat = createSeatTypeButton("Ghế đôi", SEAT_COUPLE, "COUPLE");
        panel.add(btnEmptySeat);
        panel.add(btnRegularSeat);
        panel.add(btnVipSeat);
        panel.add(btnCoupleSeat);
        if(selectedSeats != null ){
            btnBookedSeat = createSeatTypeButton("Ghế đã đặt", SEAT_BOOKED, "BOOKED");
            panel.add(btnBookedSeat);
            btnSelectedSeat = createSeatTypeButton("Ghế đang chọn", SEAT_SELECTED, "SELECTED");
            panel.add(btnSelectedSeat);
        }
        highlightSelectedSeatType();

        return panel;
    }

    public JButton createSeatTypeButton(String text, Color color, String type) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 32));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(type.equals("EMPTY") ? Color.BLACK : Color.WHITE);
        if (selectedSeats != null) {
            btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            btn.setFocusable(false);
        } else {
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                selectedSeatType = type;
                highlightSelectedSeatType();
            });
            
        }
        btn.setBorder(new LineBorder(BORDER, 1));
        return btn;
    }

    public void highlightSelectedSeatType() {
        resetSeatButtonBorder();
        if (selectedSeats == null) {
            if ("EMPTY".equals(selectedSeatType)) {
                btnEmptySeat.setBorder(new LineBorder(Color.BLACK, 3));
            } else if ("REGULAR".equals(selectedSeatType)) {
                btnRegularSeat.setBorder(new LineBorder(Color.BLACK, 3));
            } else if ("VIP".equals(selectedSeatType)) {
                btnVipSeat.setBorder(new LineBorder(Color.BLACK, 3));
            } else if ("COUPLE".equals(selectedSeatType)) {
                btnCoupleSeat.setBorder(new LineBorder(Color.BLACK, 3));
            }
        }
    }

    public void resetSeatButtonBorder() {
        if (btnEmptySeat != null) {
            btnEmptySeat.setBorder(new LineBorder(BORDER, 1));
        }

        if (btnRegularSeat != null) {
            btnRegularSeat.setBorder(new LineBorder(BORDER, 1));
        }

        if (btnVipSeat != null) {
            btnVipSeat.setBorder(new LineBorder(BORDER, 1));
        }

        if (btnCoupleSeat != null) {
            btnCoupleSeat.setBorder(new LineBorder(BORDER, 1));
        }
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(260, 34));
        txt.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        return txt;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent input) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.75;
        panel.add(input, gbc);
    }

    private JButton createButton(String text, Color color, int width) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, 38));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createGrayButton(String text, int width) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, 38));
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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

    private void addLayoutListener(JTextField txt) {
        txt.addActionListener(e -> updateCapacityAndRebuildSeatMap());

        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateCapacityAndRebuildSeatMap();
            }
        });
    }
    private void updateCapacityAndRebuildSeatMap() {
        if (loadingForm) {
            return;
        }

        if (!addMode && !editMode) {
            return;
        }

        try {
            int rows = Integer.parseInt(txtRows.getText().trim());
            int cols = Integer.parseInt(txtCols.getText().trim());

            if (rows <= 0 || cols <= 0) {
                return;
            }

            txtCapacity.setText(String.valueOf(rows * cols));

            createRegularDraftFromForm();

        } catch (NumberFormatException e) {
            txtCapacity.setText("");
        }
    }

    private void loadRoomData() {
        roomModel.setRowCount(0);

        try {
            List<RoomDAO.RoomTableRow> rooms = roomDAO.getAllForTable();

            for (RoomDAO.RoomTableRow room : rooms) {
                roomModel.addRow(new Object[]{
                        room.getRoomId(),
                        room.getName(),
                        room.getCapacity(),
                        room.getNumberOfRows() == 0 ? "" : room.getNumberOfRows(),
                        room.getSeatsPerRow() == 0 ? "" : room.getSeatsPerRow(),
                        room.getStatusText(),
                        room.getNoteText()
                });
            }

            if (roomModel.getRowCount() > 0) {
                tblRoom.setRowSelectionInterval(0, 0);
                fillFormFromSelectedRow();
            } else {
                clearForm();
                clearSeatMap();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải danh sách phòng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRoom() {
        String keyword = txtSearch.getText().trim();

        if (keyword.equals("Tìm kiếm phòng...")) {
            keyword = "";
        }

        String selectedStatus = cboSearchStatus.getSelectedItem().toString();

        roomModel.setRowCount(0);

        try {
            List<RoomDAO.RoomTableRow> rooms = roomDAO.searchForTable(keyword, selectedStatus);

            for (RoomDAO.RoomTableRow room : rooms) {
                roomModel.addRow(new Object[]{
                        room.getRoomId(),
                        room.getName(),
                        room.getCapacity(),
                        room.getNumberOfRows() == 0 ? "" : room.getNumberOfRows(),
                        room.getSeatsPerRow() == 0 ? "" : room.getSeatsPerRow(),
                        room.getStatusText(),
                        room.getNoteText()
                });
            }

            if (roomModel.getRowCount() > 0) {
                tblRoom.setRowSelectionInterval(0, 0);
                fillFormFromSelectedRow();
            } else {
                clearForm();
                clearSeatMap();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm kiếm phòng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = tblRoom.getSelectedRow();

        if (row < 0) {
            return;
        }

        loadingForm = true;

        String roomId = tblRoom.getValueAt(row, 0).toString();

        txtRoomId.setText(tblRoom.getValueAt(row, 0).toString());
        txtRoomName.setText(tblRoom.getValueAt(row, 1).toString());
        txtCapacity.setText(tblRoom.getValueAt(row, 2).toString());
        txtRows.setText(tblRoom.getValueAt(row, 3).toString());
        txtCols.setText(tblRoom.getValueAt(row, 4).toString());
        cboStatus.setSelectedItem(tblRoom.getValueAt(row, 5).toString());
        txtNote.setText(tblRoom.getValueAt(row, 6).toString());

        setFormEnabled(false);
        addMode = false;
        editMode = false;

        loadingForm = false;

        loadSeatMap(roomId);
    }

    public void loadSeatMap(String roomId) {
        clearSeatMap();
        selectedSeats = null;
        try {
            List<SeatDao.SeatView> seats = seatDao.getSeatViewsByRoom(roomId);

            int maxRow = -1;
            int maxCol = -1;

            for (SeatDao.SeatView s : seats) {
                maxRow = Math.max(maxRow, s.getRowIndex());
                maxCol = Math.max(maxCol, s.getColIndex());
            }

            if (seats.isEmpty()) {
                createWhiteDraftFromForm();
                return;
            }

            draftSeatTypes = new String[maxRow + 1][maxCol + 1];

            for (int i = 0; i <= maxRow; i++) {
                for (int j = 0; j <= maxCol; j++) {
                    draftSeatTypes[i][j] = "EMPTY";
                }
            }

            for (SeatDao.SeatView s : seats) {
                if (s.getActive() == 0) {
                    draftSeatTypes[s.getRowIndex()][s.getColIndex()] = "EMPTY";
                } else {
                    draftSeatTypes[s.getRowIndex()][s.getColIndex()] = s.getSeatType();
                }
            }

            renderDraftSeatMap();

        } catch (Exception e) {
            JLabel error = new JLabel("Lỗi tải sơ đồ ghế: " + e.getMessage(), SwingConstants.CENTER);
            error.setForeground(Color.RED);
            seatMapPanel.add(error, BorderLayout.CENTER);
            seatMapPanel.revalidate();
            seatMapPanel.repaint();
        }
    }
    public void loadSeatMapForSelling(String roomId, List<cinema.models.Seat> bookedSeats) {
        clearSeatMap();
        try {
            List<SeatDao.SeatView> seats = seatDao.getSeatViewsByRoom(roomId);
            int maxRow = -1, maxCol = -1;
            for (SeatDao.SeatView s : seats) {
                maxRow = Math.max(maxRow, s.getRowIndex());
                maxCol = Math.max(maxCol, s.getColIndex());
            }
            if (seats.isEmpty()) return;

            draftSeatTypes = new String[maxRow + 1][maxCol + 1];
            selectedSeats = new boolean[maxRow + 1][maxCol + 1];

            for (int i = 0; i <= maxRow; i++)
                for (int j = 0; j <= maxCol; j++)
                    draftSeatTypes[i][j] = "EMPTY";

            for (SeatDao.SeatView s : seats) {
                if (s.getActive() == 0) {
                    draftSeatTypes[s.getRowIndex()][s.getColIndex()] = "EMPTY";
                } else {
                    draftSeatTypes[s.getRowIndex()][s.getColIndex()] = s.getSeatType();
                }
            }
            if (bookedSeats != null) {
                for (cinema.models.Seat s : bookedSeats) {
                    draftSeatTypes[s.getRowIndex()][s.getColIndex()] = "BOOKED";
                }
            }
            renderDraftSeatMap();
        } catch (Exception e) {
            JLabel error = new JLabel("Lỗi tải sơ đồ ghế: " + e.getMessage(), SwingConstants.CENTER);
            error.setForeground(Color.RED);
            seatMapPanel.add(error, BorderLayout.CENTER);
            seatMapPanel.revalidate();
            seatMapPanel.repaint();
        }
    }
    public void setSelectMode(boolean isSelect) {
    if (isSelect) {
        selectedSeats = new boolean[1][1];
    } else {
        selectedSeats = null;
    }
}
    private void rebuildWhiteSeatMapIfEditing() {
        if (loadingForm) {
            return;
        }

        if (!addMode && !editMode) {
            return;
        }

        createWhiteDraftFromForm();
    }

    private void createWhiteDraftFromForm() {
        createRegularDraftFromForm();
    }

    private void createRegularDraftFromForm() {
        try {
            int rows = Integer.parseInt(txtRows.getText().trim());
            int cols = Integer.parseInt(txtCols.getText().trim());

            if (rows <= 0 || cols <= 0) {
                return;
            }

            draftSeatTypes = new String[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    draftSeatTypes[i][j] = "REGULAR";
                }
            }

            renderDraftSeatMap();

        } catch (Exception ignored) {
        }
    }

    private void renderDraftSeatMap() {
        clearSeatMap();

        if (draftSeatTypes == null || draftSeatTypes.length == 0) {
            JLabel empty = new JLabel("Nhập số hàng và số cột để tạo sơ đồ ghế", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(Color.GRAY);
            seatMapPanel.add(empty, BorderLayout.CENTER);
            seatMapPanel.revalidate();
            seatMapPanel.repaint();
            return;
        }

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);

        int rows = draftSeatTypes.length;
        int cols = draftSeatTypes[0].length;

        JLabel corner = new JLabel("");
        corner.setPreferredSize(new Dimension(24, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        grid.add(corner, gbc);

        for (int col = 0; col < cols; col++) {
            JLabel colLabel = new JLabel(String.valueOf(col + 1), SwingConstants.CENTER);
            colLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            colLabel.setPreferredSize(new Dimension(24, 18));

            gbc.gridx = col + 1;
            gbc.gridy = 0;
            grid.add(colLabel, gbc);
        }

        for (int row = 0; row < rows; row++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rowLabel.setPreferredSize(new Dimension(24, 24));

            gbc.gridx = 0;
            gbc.gridy = row + 1;
            grid.add(rowLabel, gbc);

            for (int col = 0; col < cols; col++) {
                gbc.gridx = col + 1;
                gbc.gridy = row + 1;
                grid.add(createDraftSeatCell(row, col), gbc);
            }
        }

        JScrollPane mapScroll = new JScrollPane(grid);
        mapScroll.setBorder(null);
        mapScroll.getViewport().setBackground(Color.WHITE);
        mapScroll.getVerticalScrollBar().setUnitIncrement(16);
        mapScroll.getHorizontalScrollBar().setUnitIncrement(16);

        seatMapPanel.add(mapScroll, BorderLayout.CENTER);
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }

    private JLabel createDraftSeatCell(int row, int col) {
        JLabel label = new JLabel();

        label.setPreferredSize(new Dimension(24, 24));
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(new LineBorder(new Color(120, 120, 120), 1));
        label.setBackground(getSeatColor(draftSeatTypes[row][col]));
        label.setToolTipText((char) ('A' + row) + String.valueOf(col + 1) + " - " + draftSeatTypes[row][col]);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (selectedSeats != null && selectedSeats[row][col]) {
            label.setBackground(SEAT_SELECTED);
        } else {
            label.setBackground(getSeatColor(draftSeatTypes[row][col]));
        }
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedSeats != null) {
                    String type = draftSeatTypes[row][col];
                    if ("EMPTY".equals(type) || "BOOKED".equals(type)) return;
                    selectedSeats[row][col] = !selectedSeats[row][col];
                    renderDraftSeatMap();
                } else {
                    if (!addMode && !editMode) return;
                    draftSeatTypes[row][col] = selectedSeatType;
                    renderDraftSeatMap();
                }
            }
        });

        return label;
    }
    // Lấy danh sách các đối tượng Seat đang được tích chọn (để lưu DB)
    public java.util.List<cinema.models.Seat> getSelectedSeatsList() {
        java.util.List<cinema.models.Seat> list = new java.util.ArrayList<>();
        if (selectedSeats == null) return list;

        for (int i = 0; i < selectedSeats.length; i++) {
            for (int j = 0; j < selectedSeats[i].length; j++) {
                if (selectedSeats[i][j]) {
                    cinema.models.Seat seat = new cinema.models.Seat();
                    seat.setRowIndex(i);
                    seat.setColIndex(j);
                    seat.setSeatType(SeatType.valueOf(draftSeatTypes[i][j])); 
                    list.add(seat);
                }
            }
        }
        return list;
    }

    public java.util.List<String> getSelectedSeatNames() {
        java.util.List<String> names = new java.util.ArrayList<>();
        if (selectedSeats == null) return names;

        for (int i = 0; i < selectedSeats.length; i++) {
            for (int j = 0; j < selectedSeats[i].length; j++) {
                if (selectedSeats[i][j]) {
                    char rowChar = (char) ('A' + i);
                    names.add("" + rowChar + (j + 1));
                }
            }
        }
        return names;
    }

    private Color getSeatColor(String type) {
        if ("VIP".equalsIgnoreCase(type)) {
            return SEAT_VIP;
        }

        if ("COUPLE".equalsIgnoreCase(type)) {
            return SEAT_COUPLE;
        }

        if ("REGULAR".equalsIgnoreCase(type)) {
            return SEAT_REGULAR;
        }
        if ("BOOKED".equalsIgnoreCase(type)) {
            return SEAT_BOOKED;
        }
        if ("SELECTED".equalsIgnoreCase(type)) {
            return SEAT_SELECTED;
        }
        return SEAT_EMPTY;
    }

    private void prepareAdd() {
        addMode = true;
        editMode = false;

        clearForm();
        setFormEnabled(true);

        try {
            txtRoomId.setText(roomDAO.getNextRoomId());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tự sinh mã phòng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            txtRoomId.setText("");
        }

        txtRoomId.setEditable(false);
        txtCapacity.setEditable(false);
        txtCapacity.setBackground(new Color(245, 245, 245));

        txtRoomName.requestFocus();

        selectedSeatType = "REGULAR";
        highlightSelectedSeatType();

        clearSeatMap();

        JLabel empty = new JLabel("Nhập số hàng và số cột để tạo sơ đồ ghế", SwingConstants.CENTER);
        empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        empty.setForeground(Color.GRAY);

        seatMapPanel.add(empty, BorderLayout.CENTER);
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }

    private void prepareEdit() {
        int row = tblRoom.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Bạn cần chọn phòng muốn sửa!");
            return;
        }

        addMode = false;
        editMode = true;

        setFormEnabled(true);

        txtRoomId.setEditable(false);
        txtCapacity.setEditable(false);
        txtCapacity.setBackground(new Color(245, 245, 245));

        txtRoomName.requestFocus();
    }

    private void clearForm() {
        txtRoomId.setText("");
        txtRoomName.setText("");
        txtCapacity.setText("");
        txtRows.setText("");
        txtCols.setText("");
        cboStatus.setSelectedIndex(0);
        txtNote.setText("");
        draftSeatTypes = null;
    }

    private void clearSeatMap() {
        seatMapPanel.removeAll();
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }

    private void setFormEnabled(boolean enabled) {
        txtRoomId.setEnabled(enabled);
        txtRoomName.setEnabled(enabled);
        txtCapacity.setEnabled(enabled);
        txtRows.setEnabled(enabled);
        txtCols.setEnabled(enabled);
        cboStatus.setEnabled(enabled);
        txtNote.setEnabled(enabled);

        txtRoomId.setEditable(false);
        txtCapacity.setEditable(false);

        txtCapacity.setBackground(new Color(245, 245, 245));

        btnSave.setEnabled(enabled);
        btnCancel.setEnabled(enabled);
    }

    private void cancelEdit() {
        addMode = false;
        editMode = false;
        setFormEnabled(false);
        fillFormFromSelectedRow();
    }

    private boolean validateForm() {
        if (txtRoomId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!");
            return false;
        }

        if (txtRoomName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phòng không được để trống!");
            return false;
        }

        try {
            int rows = Integer.parseInt(txtRows.getText().trim());
            int cols = Integer.parseInt(txtCols.getText().trim());

            if (rows <= 0 || cols <= 0) {
                JOptionPane.showMessageDialog(this, "Số hàng, số cột phải lớn hơn 0!");
                return false;
            }

            int realCapacity = rows * cols;
            txtCapacity.setText(String.valueOf(realCapacity));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số hàng, số cột phải là số!");
            return false;
        }

        if (draftSeatTypes == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa tạo sơ đồ ghế!");
            return false;
        }

        return true;
    }

    private void saveRoom() {
        if (!validateForm()) {
            return;
        }

        if (!addMode && !editMode) {
            JOptionPane.showMessageDialog(this, "Bạn cần bấm Thêm phòng hoặc Sửa phòng trước!");
            return;
        }

        String roomId = txtRoomId.getText().trim();
        String name = txtRoomName.getText().trim();
        int rows = Integer.parseInt(txtRows.getText().trim());
        int cols = Integer.parseInt(txtCols.getText().trim());
        int capacity = rows * cols;
        txtCapacity.setText(String.valueOf(capacity));
        int active = cboStatus.getSelectedIndex() == 0 ? 1 : 0;
        String type = getTypeFromNote(txtNote.getText().trim());

        try {
            if (addMode) {
                roomDAO.insertRoomWithSeats(roomId, name, capacity, type, active, rows, cols, draftSeatTypes);
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            } else {
                roomDAO.updateRoomWithSeats(roomId, name, capacity, type, active, rows, cols, draftSeatTypes);
                JOptionPane.showMessageDialog(this, "Sửa phòng thành công!");
            }

            addMode = false;
            editMode = false;
            setFormEnabled(false);
            loadRoomData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi lưu phòng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTypeFromNote(String note) {
        String noteUpper = note.toUpperCase();

        if (noteUpper.contains("IMAX")) {
            return "IMAX";
        }

        if (noteUpper.contains("VIP")) {
            return "VIP";
        }

        if (noteUpper.contains("ĐÔI") || noteUpper.contains("COUPLE")) {
            return "COUPLE";
        }

        if (noteUpper.contains("3D")) {
            return "3D";
        }

        return "2D";
    }

    public void openAddRoomMode() {
        prepareAdd();
    }

    public void refreshRoomData() {
        loadRoomData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(759, 779));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jLabel1.setText("Quản lý phòng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(317, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(311, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private javax.swing.JLabel jLabel1;
}