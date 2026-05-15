package cinema.form.panel;

import cinema.DBConnection;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongManagerPanel extends JPanel {
    private static final Color BG = new Color(248, 250, 252);
    private static final Color BLUE = new Color(0, 146, 255);
    private static final Color BORDER = new Color(204, 204, 204);
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 15);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    private PlaceholderTextField txtSearch;
    private JComboBox<String> cboType;
    private JComboBox<String> cboStatus;
    private JPanel cardContainer;

    private final RoomRepository roomRepository = new RoomRepository();

    public PhongManagerPanel() {
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        initUI();
        loadRooms();
    }

    private void initUI() {
        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BorderLayout(0, 50));
        add(main, BorderLayout.CENTER);

        main.add(createFilterPanel(), BorderLayout.NORTH);
        main.add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new LineBorder(BORDER, 1));
        filterPanel.setPreferredSize(new Dimension(10, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 16, 12, 16);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtSearch = new PlaceholderTextField("Nhập mã phòng hoặc tên phòng...");
        txtSearch.setFont(INPUT_FONT);
        txtSearch.setPreferredSize(new Dimension(300, 36));
        txtSearch.addActionListener(e -> loadRooms());

        cboType = new JComboBox<>(new String[]{"Tất cả loại phòng", "2D", "3D", "VIP", "IMAX", "COUPLE"});
        cboType.setFont(INPUT_FONT);
        cboType.setPreferredSize(new Dimension(180, 36));
        cboType.addActionListener(e -> loadRooms());

        cboStatus = new JComboBox<>(new String[]{"Đang hoạt động", "Tất cả trạng thái", "Ngừng hoạt động"});
        cboStatus.setFont(INPUT_FONT);
        cboStatus.setPreferredSize(new Dimension(180, 36));
        cboStatus.addActionListener(e -> loadRooms());

        JButton btnSearch = createBlueButton("Tìm kiếm", null);
        btnSearch.setPreferredSize(new Dimension(150, 40));
        btnSearch.addActionListener(e -> loadRooms());

        addFilterItem(filterPanel, gbc, 0, "Tìm kiếm", txtSearch);
        addFilterItem(filterPanel, gbc, 1, "Loại phòng", cboType);
        addFilterItem(filterPanel, gbc, 2, "Trạng thái", cboStatus);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        filterPanel.add(btnSearch, gbc);

        return filterPanel;
    }

    private void addFilterItem(JPanel panel, GridBagConstraints gbc, int x, String label, JComponent input) {
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        panel.add(lbl, gbc);

        gbc.gridy = 1;
        panel.add(input, gbc);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);

        JLabel title = new JLabel("Danh sách phòng chiếu");
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT);
        titleBar.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnAdd = createBlueButton("Thêm phòng", "/cinema/images/add.png");
        btnAdd.addActionListener(e -> openRoomDialog(null));
        buttonPanel.add(btnAdd);

        titleBar.add(buttonPanel, BorderLayout.EAST);
        content.add(titleBar, BorderLayout.NORTH);

        cardContainer = new JPanel(new GridBagLayout());
        cardContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    private JButton createBlueButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(210, 40));

        if (iconPath != null) {
            java.net.URL url = getClass().getResource(iconPath);
            if (url != null) button.setIcon(new ImageIcon(url));
        }
        return button;
    }

    private JButton createWhiteButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(BLUE);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(BLUE, 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadRooms() {
        cardContainer.removeAll();

        try {
            String keyword = txtSearch == null ? "" : txtSearch.getRealText().trim();
            String type = cboType == null ? "Tất cả loại phòng" : cboType.getSelectedItem().toString();
            String status = cboStatus == null ? "Đang hoạt động" : cboStatus.getSelectedItem().toString();

            List<RoomView> rooms = roomRepository.findRooms(keyword, type, status);

            if (rooms.isEmpty()) {
                JLabel empty = new JLabel("Không có phòng chiếu phù hợp");
                empty.setFont(new Font("Arial", Font.PLAIN, 16));
                empty.setForeground(new Color(100, 116, 139));
                cardContainer.add(empty);
            } else {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(0, 0, 30, 30);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1;

                int colCount = 3;
                for (int i = 0; i < rooms.size(); i++) {
                    gbc.gridx = i % colCount;
                    gbc.gridy = i / colCount;
                    cardContainer.add(createRoomCard(rooms.get(i)), gbc);
                }

                gbc.gridx = 0;
                gbc.gridy = (rooms.size() + colCount - 1) / colCount;
                gbc.weighty = 1;
                cardContainer.add(Box.createVerticalGlue(), gbc);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel createRoomCard(RoomView room) {
        JPanel card = new JPanel(new BorderLayout(0, 14));
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BORDER, 1));
        card.setPreferredSize(new Dimension(330, 255));

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel name = new JLabel(room.name + "  -  " + room.roomId);
        name.setFont(new Font("Arial", Font.BOLD, 20));
        name.setForeground(TEXT);
        body.add(name, gbc);

        gbc.gridy = 1;
        body.add(createInfoLine("Loại phòng", room.type), gbc);
        gbc.gridy = 2;
        body.add(createInfoLine("Sức chứa", room.capacity + " ghế"), gbc);
        gbc.gridy = 3;
        body.add(createInfoLine("Sơ đồ", room.numberOfRows + " hàng x " + room.seatsPerRow + " ghế"), gbc);
        gbc.gridy = 4;
        body.add(createInfoLine("Hàng VIP", formatVip(room)), gbc);
        gbc.gridy = 5;
        body.add(createInfoLine("Hàng couple", room.coupleRow >= 0 ? String.valueOf((char) ('A' + room.coupleRow)) : "Không có"), gbc);
        gbc.gridy = 6;
        JLabel status = new JLabel(room.active ? "Đang hoạt động" : "Ngừng hoạt động");
        status.setFont(new Font("Arial", Font.BOLD, 14));
        status.setForeground(room.active ? new Color(22, 163, 74) : new Color(220, 38, 38));
        body.add(status, gbc);

        card.add(body, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        actions.setBackground(Color.WHITE);
        JButton btnEdit = createWhiteButton("Sửa");
        btnEdit.setPreferredSize(new Dimension(90, 34));
        btnEdit.addActionListener(e -> openRoomDialog(room));

        JButton btnDelete = createWhiteButton("Xóa");
        btnDelete.setPreferredSize(new Dimension(90, 34));
        btnDelete.setForeground(new Color(220, 38, 38));
        btnDelete.setBorder(new LineBorder(new Color(220, 38, 38), 1));
        btnDelete.addActionListener(e -> deleteRoom(room));

        actions.add(btnEdit);
        actions.add(btnDelete);
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createInfoLine(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(new Color(100, 116, 139));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.BOLD, 14));
        val.setForeground(TEXT);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private String formatVip(RoomView room) {
        if (room.vipStartRow < 0 || room.vipEndRow < 0) return "Không có";
        char start = (char) ('A' + room.vipStartRow);
        char end = (char) ('A' + room.vipEndRow);
        return start == end ? String.valueOf(start) : start + " - " + end;
    }

    private void openRoomDialog(RoomView room) {
        RoomDialog dialog = new RoomDialog(SwingUtilities.getWindowAncestor(this), room);
        dialog.setVisible(true);
        if (dialog.isSaved()) loadRooms();
    }

    private void deleteRoom(RoomView room) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa phòng " + room.name + " không?\nPhòng sẽ chuyển sang trạng thái ngừng hoạt động để không lỗi khóa ngoại.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                roomRepository.softDelete(room.roomId);
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!");
                loadRooms();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa phòng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RoomDialog extends JDialog {
        private JTextField txtRoomId;
        private JTextField txtName;
        private JTextField txtCapacity;
        private JComboBox<String> cboRoomType;
        private JComboBox<String> cboActive;
        private JTextField txtRows;
        private JTextField txtSeatsPerRow;
        private JTextField txtVipStart;
        private JTextField txtVipEnd;
        private JTextField txtCoupleRow;
        private boolean saved = false;
        private final RoomView editingRoom;

        RoomDialog(Window owner, RoomView room) {
            super(owner, room == null ? "Thêm phòng" : "Sửa phòng", ModalityType.APPLICATION_MODAL);
            this.editingRoom = room;
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setResizable(false);
            setSize(620, 600);
            setLocationRelativeTo(owner);
            initDialogUI();
            if (room != null) fillData(room);
        }

        boolean isSaved() {
            return saved;
        }

        private void initDialogUI() {
            JPanel root = new JPanel(new BorderLayout(0, 24));
            root.setBackground(Color.WHITE);
            root.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            setContentPane(root);

            JLabel title = new JLabel(editingRoom == null ? "Thêm phòng chiếu" : "Sửa phòng chiếu");
            title.setFont(TITLE_FONT);
            title.setForeground(TEXT);
            root.add(title, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Color.WHITE);
            root.add(form, BorderLayout.CENTER);

            txtRoomId = new JTextField();
            txtName = new JTextField();
            txtCapacity = new JTextField();
            cboRoomType = new JComboBox<>(new String[]{"2D", "3D", "VIP", "IMAX", "COUPLE"});
            cboActive = new JComboBox<>(new String[]{"Đang hoạt động", "Ngừng hoạt động"});
            txtRows = new JTextField();
            txtSeatsPerRow = new JTextField();
            txtVipStart = new JTextField();
            txtVipEnd = new JTextField();
            txtCoupleRow = new JTextField();

            addField(form, 0, "Mã phòng", txtRoomId);
            addField(form, 1, "Tên phòng", txtName);
            addField(form, 2, "Loại phòng", cboRoomType);
            addField(form, 3, "Sức chứa", txtCapacity);
            addField(form, 4, "Trạng thái", cboActive);
            addField(form, 5, "Số hàng ghế", txtRows);
            addField(form, 6, "Số ghế mỗi hàng", txtSeatsPerRow);
            addField(form, 7, "Hàng VIP bắt đầu", txtVipStart);
            addField(form, 8, "Hàng VIP kết thúc", txtVipEnd);
            addField(form, 9, "Hàng couple", txtCoupleRow);

            JLabel hint = new JLabel("Gợi ý: hàng ghế nhập số 0,1,2... tương ứng A,B,C... Nếu không có thì nhập -1.");
            hint.setFont(new Font("Arial", Font.ITALIC, 13));
            hint.setForeground(new Color(100, 116, 139));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 10;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(4, 0, 0, 0);
            form.add(hint, gbc);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
            buttons.setBackground(Color.WHITE);
            JButton btnCancel = createWhiteButton("Hủy");
            btnCancel.setPreferredSize(new Dimension(120, 40));
            btnCancel.addActionListener(e -> dispose());

            JButton btnSave = createBlueButton("Lưu thông tin", null);
            btnSave.setPreferredSize(new Dimension(160, 40));
            btnSave.addActionListener(e -> saveRoom());

            buttons.add(btnCancel);
            buttons.add(btnSave);
            root.add(buttons, BorderLayout.SOUTH);
        }

        private void addField(JPanel form, int row, String label, JComponent input) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 12, 16);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = 0;
            gbc.gridy = row;
            JLabel lbl = new JLabel(label);
            lbl.setFont(LABEL_FONT);
            lbl.setForeground(TEXT);
            form.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 12, 0);
            input.setFont(INPUT_FONT);
            input.setPreferredSize(new Dimension(360, 36));
            form.add(input, gbc);
        }

        private void fillData(RoomView room) {
            txtRoomId.setText(room.roomId);
            txtRoomId.setEditable(false);
            txtName.setText(room.name);
            txtCapacity.setText(String.valueOf(room.capacity));
            cboRoomType.setSelectedItem(room.type);
            cboActive.setSelectedIndex(room.active ? 0 : 1);
            txtRows.setText(String.valueOf(room.numberOfRows));
            txtSeatsPerRow.setText(String.valueOf(room.seatsPerRow));
            txtVipStart.setText(String.valueOf(room.vipStartRow));
            txtVipEnd.setText(String.valueOf(room.vipEndRow));
            txtCoupleRow.setText(String.valueOf(room.coupleRow));
        }

        private void saveRoom() {
            try {
                RoomView room = getFormData();
                validateRoom(room);

                if (editingRoom == null) {
                    roomRepository.insert(room);
                    JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                } else {
                    roomRepository.update(room);
                    JOptionPane.showMessageDialog(this, "Sửa phòng thành công!");
                }

                saved = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private RoomView getFormData() {
            RoomView room = new RoomView();
            room.roomId = txtRoomId.getText().trim();
            room.name = txtName.getText().trim();
            room.type = cboRoomType.getSelectedItem().toString();
            room.capacity = parseInt(txtCapacity.getText().trim(), "Sức chứa");
            room.active = cboActive.getSelectedIndex() == 0;
            room.numberOfRows = parseInt(txtRows.getText().trim(), "Số hàng ghế");
            room.seatsPerRow = parseInt(txtSeatsPerRow.getText().trim(), "Số ghế mỗi hàng");
            room.vipStartRow = parseInt(txtVipStart.getText().trim(), "Hàng VIP bắt đầu");
            room.vipEndRow = parseInt(txtVipEnd.getText().trim(), "Hàng VIP kết thúc");
            room.coupleRow = parseInt(txtCoupleRow.getText().trim(), "Hàng couple");
            return room;
        }

        private int parseInt(String value, String fieldName) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(fieldName + " phải là số nguyên.");
            }
        }

        private void validateRoom(RoomView room) throws SQLException {
            if (room.roomId.isEmpty()) throw new IllegalArgumentException("Mã phòng không được để trống.");
            if (room.name.isEmpty()) throw new IllegalArgumentException("Tên phòng không được để trống.");
            if (room.capacity <= 0) throw new IllegalArgumentException("Sức chứa phải lớn hơn 0.");
            if (room.numberOfRows <= 0) throw new IllegalArgumentException("Số hàng ghế phải lớn hơn 0.");
            if (room.seatsPerRow <= 0) throw new IllegalArgumentException("Số ghế mỗi hàng phải lớn hơn 0.");
            if (room.capacity != room.numberOfRows * room.seatsPerRow) {
                throw new IllegalArgumentException("Sức chứa phải bằng Số hàng ghế x Số ghế mỗi hàng.");
            }
            if (room.vipStartRow >= 0 || room.vipEndRow >= 0) {
                if (room.vipStartRow < 0 || room.vipEndRow < 0 || room.vipStartRow > room.vipEndRow || room.vipEndRow >= room.numberOfRows) {
                    throw new IllegalArgumentException("Hàng VIP không hợp lệ.");
                }
            }
            if (room.coupleRow >= room.numberOfRows) {
                throw new IllegalArgumentException("Hàng couple không được lớn hơn số hàng ghế.");
            }
            if (editingRoom == null && roomRepository.exists(room.roomId)) {
                throw new IllegalArgumentException("Mã phòng đã tồn tại.");
            }
        }
    }

    private static class RoomView {
        String roomId;
        String name;
        int capacity;
        String type;
        boolean active;
        int layoutId;
        int numberOfRows;
        int seatsPerRow;
        int vipStartRow;
        int vipEndRow;
        int coupleRow;
    }

    private static class RoomRepository {
        private Connection getConnection() throws SQLException {
            return DBConnection.getConnection();
        }

        List<RoomView> findRooms(String keyword, String typeFilter, String statusFilter) throws SQLException {
            List<RoomView> rooms = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT r.roomId, r.name, r.capacity, r.type, r.active, ");
            sql.append("COALESCE(sl.layoutId, 0) AS layoutId, ");
            sql.append("COALESCE(sl.numberOfRows, 0) AS numberOfRows, ");
            sql.append("COALESCE(sl.seatsPerRow, 0) AS seatsPerRow, ");
            sql.append("COALESCE(sl.vipStartRow, -1) AS vipStartRow, ");
            sql.append("COALESCE(sl.vipEndRow, -1) AS vipEndRow, ");
            sql.append("COALESCE(sl.coupleRow, -1) AS coupleRow ");
            sql.append("FROM Room r LEFT JOIN SeatLayout sl ON r.roomId = sl.roomId WHERE 1 = 1 ");

            List<Object> params = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                sql.append("AND (r.roomId LIKE ? OR r.name LIKE ?) ");
                params.add("%" + keyword + "%");
                params.add("%" + keyword + "%");
            }
            if (typeFilter != null && !typeFilter.equals("Tất cả loại phòng")) {
                sql.append("AND r.type = ? ");
                params.add(typeFilter);
            }
            if (statusFilter != null && statusFilter.equals("Đang hoạt động")) {
                sql.append("AND r.active = 1 ");
            } else if (statusFilter != null && statusFilter.equals("Ngừng hoạt động")) {
                sql.append("AND r.active = 0 ");
            }
            sql.append("ORDER BY r.roomId");

            try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) rooms.add(mapRoom(rs));
                }
            }
            return rooms;
        }

        boolean exists(String roomId) throws SQLException {
            String sql = "SELECT roomId FROM Room WHERE roomId = ?";
            try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, roomId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        void insert(RoomView room) throws SQLException {
            String insertRoom = "INSERT INTO Room(roomId, name, capacity, type, active) VALUES (?, ?, ?, ?, ?)";
            String insertLayout = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);
                try (PreparedStatement psRoom = conn.prepareStatement(insertRoom);
                     PreparedStatement psLayout = conn.prepareStatement(insertLayout)) {

                    psRoom.setString(1, room.roomId);
                    psRoom.setString(2, room.name);
                    psRoom.setInt(3, room.capacity);
                    psRoom.setString(4, room.type);
                    psRoom.setBoolean(5, room.active);
                    psRoom.executeUpdate();

                    psLayout.setString(1, room.roomId);
                    psLayout.setInt(2, room.numberOfRows);
                    psLayout.setInt(3, room.seatsPerRow);
                    psLayout.setInt(4, room.vipStartRow);
                    psLayout.setInt(5, room.vipEndRow);
                    psLayout.setInt(6, room.coupleRow);
                    psLayout.executeUpdate();

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }

        void update(RoomView room) throws SQLException {
            String updateRoom = "UPDATE Room SET name = ?, capacity = ?, type = ?, active = ? WHERE roomId = ?";
            String upsertLayout = "INSERT INTO SeatLayout(roomId, numberOfRows, seatsPerRow, vipStartRow, vipEndRow, coupleRow) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE numberOfRows = VALUES(numberOfRows), seatsPerRow = VALUES(seatsPerRow), " +
                    "vipStartRow = VALUES(vipStartRow), vipEndRow = VALUES(vipEndRow), coupleRow = VALUES(coupleRow)";

            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);
                try (PreparedStatement psRoom = conn.prepareStatement(updateRoom);
                     PreparedStatement psLayout = conn.prepareStatement(upsertLayout)) {

                    psRoom.setString(1, room.name);
                    psRoom.setInt(2, room.capacity);
                    psRoom.setString(3, room.type);
                    psRoom.setBoolean(4, room.active);
                    psRoom.setString(5, room.roomId);
                    psRoom.executeUpdate();

                    psLayout.setString(1, room.roomId);
                    psLayout.setInt(2, room.numberOfRows);
                    psLayout.setInt(3, room.seatsPerRow);
                    psLayout.setInt(4, room.vipStartRow);
                    psLayout.setInt(5, room.vipEndRow);
                    psLayout.setInt(6, room.coupleRow);
                    psLayout.executeUpdate();

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        }

        void softDelete(String roomId) throws SQLException {
            String sql = "UPDATE Room SET active = 0 WHERE roomId = ?";
            try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, roomId);
                ps.executeUpdate();
            }
        }

        private RoomView mapRoom(ResultSet rs) throws SQLException {
            RoomView room = new RoomView();
            room.roomId = rs.getString("roomId");
            room.name = rs.getString("name");
            room.capacity = rs.getInt("capacity");
            room.type = rs.getString("type");
            room.active = rs.getBoolean("active");
            room.layoutId = rs.getInt("layoutId");
            room.numberOfRows = rs.getInt("numberOfRows");
            room.seatsPerRow = rs.getInt("seatsPerRow");
            room.vipStartRow = rs.getInt("vipStartRow");
            room.vipEndRow = rs.getInt("vipEndRow");
            room.coupleRow = rs.getInt("coupleRow");
            return room;
        }
    }

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;
        private boolean showingPlaceholder = true;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setForeground(Color.GRAY);
            setBorder(new LineBorder(BORDER, 1));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (showingPlaceholder) {
                        setText("");
                        setForeground(TEXT);
                        showingPlaceholder = false;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().trim().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                        showingPlaceholder = true;
                    }
                }
            });
        }

        String getRealText() {
            return showingPlaceholder ? "" : getText();
        }
    }
}
