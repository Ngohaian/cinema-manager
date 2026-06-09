package cinema.dao;

import cinema.DBConnection;
import cinema.models.Room;
import cinema.models.SeatLayout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private SeatLayoutDAO seatLayoutDAO = new SeatLayoutDAO();
    private SeatDao seatDao = new SeatDao();

    public static class RoomTableRow {
        private String roomId;
        private String name;
        private int capacity;
        private String type;
        private int active;
        private int numberOfRows;
        private int seatsPerRow;

        public RoomTableRow(String roomId, String name, int capacity, String type,
                            int active, int numberOfRows, int seatsPerRow) {
            this.roomId = roomId;
            this.name = name;
            this.capacity = capacity;
            this.type = type;
            this.active = active;
            this.numberOfRows = numberOfRows;
            this.seatsPerRow = seatsPerRow;
        }

        public String getRoomId() {
            return roomId;
        }

        public String getName() {
            return name;
        }

        public int getCapacity() {
            return capacity;
        }

        public String getType() {
            return type;
        }

        public int getActive() {
            return active;
        }

        public int getNumberOfRows() {
            return numberOfRows;
        }

        public int getSeatsPerRow() {
            return seatsPerRow;
        }

        public String getStatusText() {
            return active == 1 ? "Đang hoạt động" : "Ngừng hoạt động";
        }

        public String getNoteText() {
            if (active == 0) {
                return "Bảo trì";
            }

            if (type == null) {
                return "Phòng tiêu chuẩn";
            }

            switch (type.toUpperCase()) {
                case "VIP":
                    return "Phòng VIP";
                case "IMAX":
                    return "Phòng IMAX";
                case "COUPLE":
                    return "Phòng ghế đôi";
                case "3D":
                    return "Phòng tiêu chuẩn 3D";
                case "2D":
                default:
                    return "Phòng tiêu chuẩn";
            }
        }
    }

    private Connection getConn() throws Exception {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            throw new Exception("Không kết nối được MySQL. Kiểm tra DBConnection, user, password, IP và MySQL Connector.");
        }

        return conn;
    }

    public List<Room> getAll() throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE active = 1 ORDER BY roomId";

        try (Connection conn = getConn();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String roomId = rs.getString("roomId");
                SeatLayout layout = seatLayoutDAO.getByRoomId(roomId);

                Room r = new Room(
                        roomId,
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getString("type"),
                        layout
                );

                r.setActive(rs.getBoolean("active"));
                list.add(r);
            }
        }

        return list;
    }

    public List<Room> search(String keyword) throws Exception {
        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM Room " +
                "WHERE active = 1 AND (roomId LIKE ? OR name LIKE ? OR type LIKE ?) " +
                "ORDER BY roomId";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String roomId = rs.getString("roomId");
                    SeatLayout layout = seatLayoutDAO.getByRoomId(roomId);

                    Room r = new Room(
                            roomId,
                            rs.getString("name"),
                            rs.getInt("capacity"),
                            rs.getString("type"),
                            layout
                    );

                    r.setActive(rs.getBoolean("active"));
                    list.add(r);
                }
            }
        }

        return list;
    }

    public List<RoomTableRow> getAllForTable() throws Exception {
        return searchForTable("", "Tất cả trạng thái");
    }

    public List<RoomTableRow> searchForTable(String keyword, String status) throws Exception {
        List<RoomTableRow> list = new ArrayList<>();

        if (keyword == null) {
            keyword = "";
        }

        if (status == null) {
            status = "Tất cả trạng thái";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.roomId, r.name, r.capacity, r.type, r.active, ");
        sql.append("       sl.numberOfRows, sl.seatsPerRow ");
        sql.append("FROM Room r ");
        sql.append("LEFT JOIN SeatLayout sl ON r.roomId = sl.roomId ");
        sql.append("WHERE (r.roomId LIKE ? OR r.name LIKE ? OR r.type LIKE ?) ");

        if (status.equals("Đang hoạt động")) {
            sql.append("AND r.active = 1 ");
        } else if (status.equals("Ngừng hoạt động")) {
            sql.append("AND r.active = 0 ");
        }

        sql.append("ORDER BY r.roomId");

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rows = rs.getObject("numberOfRows") == null ? 0 : rs.getInt("numberOfRows");
                    int cols = rs.getObject("seatsPerRow") == null ? 0 : rs.getInt("seatsPerRow");

                    RoomTableRow row = new RoomTableRow(
                            rs.getString("roomId"),
                            rs.getString("name"),
                            rs.getInt("capacity"),
                            rs.getString("type"),
                            rs.getInt("active"),
                            rows,
                            cols
                    );

                    list.add(row);
                }
            }
        }

        return list;
    }

    public Room getById(String id) throws Exception {
        String sql = "SELECT * FROM Room WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SeatLayout layout = seatLayoutDAO.getByRoomId(id);

                    Room r = new Room(
                            rs.getString("roomId"),
                            rs.getString("name"),
                            rs.getInt("capacity"),
                            rs.getString("type"),
                            layout
                    );

                    r.setActive(rs.getBoolean("active"));
                    return r;
                }
            }
        }

        return null;
    }

    public void insert(Room r) throws Exception {
        String sql = "INSERT INTO Room(roomId, name, capacity, type, active) VALUES (?, ?, ?, ?, 1)";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getRoomId());
            ps.setString(2, r.getName());
            ps.setInt(3, r.getCapacity());
            ps.setString(4, r.getType());

            ps.executeUpdate();
        }
    }

    public void update(Room r) throws Exception {
        String sql = "UPDATE Room SET name = ?, capacity = ?, type = ? WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getName());
            ps.setInt(2, r.getCapacity());
            ps.setString(3, r.getType());
            ps.setString(4, r.getRoomId());

            ps.executeUpdate();
        }
    }

    public void delete(String roomId) throws Exception {
        String sql = "UPDATE Room SET active = 0 WHERE roomId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.executeUpdate();
        }
    }

    public void insertRoomWithSeats(String roomId, String name, int capacity, String type,
                                    int active, int rows, int cols, String[][] draftSeatTypes) throws Exception {

        String sqlRoom = "INSERT INTO Room(roomId, name, capacity, type, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
                    ps.setString(1, roomId);
                    ps.setString(2, name);
                    ps.setInt(3, capacity);
                    ps.setString(4, type);
                    ps.setInt(5, active);
                    ps.executeUpdate();
                }

                int[] layoutInfo = calculateLayoutInfo(draftSeatTypes);

                seatLayoutDAO.insertOrUpdate(
                        conn,
                        roomId,
                        rows,
                        cols,
                        layoutInfo[0],
                        layoutInfo[1],
                        layoutInfo[2]
                );

                seatDao.insertOrUpdateSeats(conn, roomId, draftSeatTypes);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void updateRoomWithSeats(String roomId, String name, int capacity, String type,
                                    int active, int rows, int cols, String[][] draftSeatTypes) throws Exception {

        String sqlRoom = "UPDATE Room SET name = ?, capacity = ?, type = ?, active = ? WHERE roomId = ?";

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlRoom)) {
                    ps.setString(1, name);
                    ps.setInt(2, capacity);
                    ps.setString(3, type);
                    ps.setInt(4, active);
                    ps.setString(5, roomId);
                    ps.executeUpdate();
                }

                int[] layoutInfo = calculateLayoutInfo(draftSeatTypes);

                seatLayoutDAO.insertOrUpdate(
                        conn,
                        roomId,
                        rows,
                        cols,
                        layoutInfo[0],
                        layoutInfo[1],
                        layoutInfo[2]
                );

                seatDao.insertOrUpdateSeats(conn, roomId, draftSeatTypes);
                seatDao.deactivateOutOfLayoutSeats(conn, roomId, rows, cols);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private int[] calculateLayoutInfo(String[][] draftSeatTypes) {
        int vipStart = -1;
        int vipEnd = -1;
        int coupleRow = -1;

        if (draftSeatTypes == null) {
            return new int[]{-1, -1, -1};
        }

        for (int i = 0; i < draftSeatTypes.length; i++) {
            boolean hasVip = false;
            boolean hasCouple = false;

            for (int j = 0; j < draftSeatTypes[i].length; j++) {
                if ("VIP".equalsIgnoreCase(draftSeatTypes[i][j])) {
                    hasVip = true;
                }

                if ("COUPLE".equalsIgnoreCase(draftSeatTypes[i][j])) {
                    hasCouple = true;
                }
            }

            if (hasVip) {
                if (vipStart == -1) {
                    vipStart = i;
                }
                vipEnd = i;
            }

            if (hasCouple && coupleRow == -1) {
                coupleRow = i;
            }
        }

        return new int[]{vipStart, vipEnd, coupleRow};
    }
    public String getNextRoomId() throws Exception {
        String sql = "SELECT roomId FROM Room ORDER BY CAST(SUBSTRING(roomId, 2) AS UNSIGNED) DESC LIMIT 1";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("roomId"); 
                int number = Integer.parseInt(lastId.substring(1)); 
                number++;

                return String.format("R%03d", number);
            }
        }

        return "R001";
    }
}