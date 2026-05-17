package cinema.dao;

import cinema.DBConnection;
import cinema.enums.MovieStatus;
import cinema.models.Movie;
import cinema.models.Room;
import cinema.models.ShowTime;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShowTimeDAO {

    private MovieDAO movieDAO = new MovieDAO();
    private RoomDAO roomDAO = new RoomDAO();

    public List<ShowtimeView> getAllView() throws Exception {
        List<ShowtimeView> list = new ArrayList<>();

        String sql =
                "SELECT s.showtimeId, s.movieId, m.title, m.duration, " +
                "s.roomId, r.name AS roomName, " +
                "s.startTime, s.endTime, s.basePrice, s.vipExtra, s.coupleExtra, s.active " +
                "FROM Showtime s " +
                "JOIN Movie m ON s.movieId = m.movieId " +
                "JOIN Room r ON s.roomId = r.roomId " +
                "ORDER BY s.startTime DESC, s.showtimeId";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapView(rs));
            }
        }

        return list;
    }

    public List<ShowtimeView> searchView(String keyword, String roomId, String date, String status) throws Exception {
        List<ShowtimeView> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.showtimeId, s.movieId, m.title, m.duration, ");
        sql.append("s.roomId, r.name AS roomName, ");
        sql.append("s.startTime, s.endTime, s.basePrice, s.vipExtra, s.coupleExtra, s.active ");
        sql.append("FROM Showtime s ");
        sql.append("JOIN Movie m ON s.movieId = m.movieId ");
        sql.append("JOIN Room r ON s.roomId = r.roomId ");
        sql.append("WHERE (s.showtimeId LIKE ? OR m.title LIKE ?) ");

        if (roomId != null && !roomId.trim().isEmpty()) {
            sql.append("AND s.roomId = ? ");
        }

        if (date != null && !date.trim().isEmpty()) {
            sql.append("AND DATE(s.startTime) = ? ");
        }

        if ("Đang mở".equals(status)) {
            sql.append("AND s.active = 1 ");
        } else if ("Ngừng bán".equals(status)) {
            sql.append("AND s.active = 0 ");
        }

        sql.append("ORDER BY s.startTime DESC, s.showtimeId");

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            String key = "%" + (keyword == null ? "" : keyword.trim()) + "%";

            ps.setString(index++, key);
            ps.setString(index++, key);

            if (roomId != null && !roomId.trim().isEmpty()) {
                ps.setString(index++, roomId.trim());
            }

            if (date != null && !date.trim().isEmpty()) {
                ps.setString(index++, date.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapView(rs));
                }
            }
        }

        return list;
    }

    public ShowtimeView getViewById(String showtimeId) throws Exception {
        String sql =
                "SELECT s.showtimeId, s.movieId, m.title, m.duration, " +
                "s.roomId, r.name AS roomName, " +
                "s.startTime, s.endTime, s.basePrice, s.vipExtra, s.coupleExtra, s.active " +
                "FROM Showtime s " +
                "JOIN Movie m ON s.movieId = m.movieId " +
                "JOIN Room r ON s.roomId = r.roomId " +
                "WHERE s.showtimeId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, showtimeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapView(rs);
                }
            }
        }

        return null;
    }

    public List<ShowTime> getAll() throws Exception {
        List<ShowTime> list = new ArrayList<>();

        String sql = "SELECT * FROM Showtime WHERE active = 1 ORDER BY startTime DESC";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String movieId = rs.getString("movieId");
                String roomId = rs.getString("roomId");

                Movie movie = movieDAO.getById(movieId);
                Room room = roomDAO.getById(roomId);

                if (movie == null || room == null) {
                    continue;
                }

                LocalDateTime start = rs.getTimestamp("startTime").toLocalDateTime();

                ShowTime s = new ShowTime(
                        rs.getString("showtimeId"),
                        movie,
                        room,
                        start,
                        rs.getDouble("basePrice")
                );

                s.setActive(rs.getBoolean("active"));
                list.add(s);
            }
        }

        return list;
    }
    public List<ShowTime> getByMovieId(String movieId){
        List<ShowTime> list = new ArrayList<>();
        String sql = "SELECT * FROM Showtime WHERE movieId = ? AND active = 1 AND startTime >= now()";
        try (java.sql.Connection conn = DBConnection.getConnection();java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movieId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Movie movie = movieDAO.getById(movieId);
                Room room = roomDAO.getById(rs.getString("roomId"));

                ShowTime s = new ShowTime(
                    rs.getString("showtimeId"),
                    movie,
                    room,
                    rs.getTimestamp("startTime").toLocalDateTime(),
                    rs.getDouble("basePrice")
                );
                list.add(s);
            }
        }
        catch(Exception ex){
            System.out.print("Loi: "+ex);
        }
        return list;
    }
    public void insert(String showtimeId,
                       String movieId,
                       String roomId,
                       LocalDateTime startTime,
                       double basePrice,
                       double vipExtra,
                       double coupleExtra) throws Exception {

        Movie movie = movieDAO.getById(movieId);

        if (movie == null) {
            throw new Exception("Không tìm thấy phim có mã: " + movieId);
        }

        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());

        if (existsConflict(null, roomId, startTime, endTime)) {
            throw new Exception("Phòng đã có suất chiếu bị trùng thời gian.");
        }

        String finalShowtimeId = showtimeId;

        if (finalShowtimeId == null || finalShowtimeId.trim().isEmpty() || existsById(finalShowtimeId)) {
            finalShowtimeId = generateNextShowtimeId();
        }

        String sql =
                "INSERT INTO Showtime(showtimeId, movieId, roomId, startTime, endTime, basePrice, vipExtra, coupleExtra, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, finalShowtimeId);
            ps.setString(2, movieId);
            ps.setString(3, roomId);
            ps.setTimestamp(4, Timestamp.valueOf(startTime));
            ps.setTimestamp(5, Timestamp.valueOf(endTime));
            ps.setDouble(6, basePrice);
            ps.setDouble(7, vipExtra);
            ps.setDouble(8, coupleExtra);

            ps.executeUpdate();
        }
    }

    public void insert(ShowTime s) throws Exception {
        insert(
                s.getShowtimeId(),
                s.getMovie().getId(),
                s.getRoom().getRoomId(),
                s.getStartTime(),
                s.getBasePrice(),
                s.getVipExtra(),
                s.getCoupleExtra()
        );
    }

    public void update(String showtimeId,
                       String movieId,
                       String roomId,
                       LocalDateTime startTime,
                       double basePrice,
                       double vipExtra,
                       double coupleExtra,
                       boolean active) throws Exception {

        Movie movie = movieDAO.getById(movieId);

        if (movie == null) {
            throw new Exception("Không tìm thấy phim có mã: " + movieId);
        }

        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());

        if (existsConflict(showtimeId, roomId, startTime, endTime)) {
            throw new Exception("Phòng đã có suất chiếu bị trùng thời gian.");
        }

        String sql =
                "UPDATE Showtime SET movieId=?, roomId=?, startTime=?, endTime=?, " +
                "basePrice=?, vipExtra=?, coupleExtra=?, active=? " +
                "WHERE showtimeId=?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, movieId);
            ps.setString(2, roomId);
            ps.setTimestamp(3, Timestamp.valueOf(startTime));
            ps.setTimestamp(4, Timestamp.valueOf(endTime));
            ps.setDouble(5, basePrice);
            ps.setDouble(6, vipExtra);
            ps.setDouble(7, coupleExtra);
            ps.setBoolean(8, active);
            ps.setString(9, showtimeId);

            ps.executeUpdate();
        }
    }

    public void delete(String showtimeId) throws Exception {
        String sql = "UPDATE Showtime SET active = 0 WHERE showtimeId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, showtimeId);
            ps.executeUpdate();
        }
    }

    public boolean existsConflict(String currentShowtimeId,
                                  String roomId,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime) throws Exception {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM Showtime ");
        sql.append("WHERE roomId = ? ");
        sql.append("AND active = 1 ");
        sql.append("AND startTime < ? ");
        sql.append("AND endTime > ? ");

        if (currentShowtimeId != null && !currentShowtimeId.trim().isEmpty()) {
            sql.append("AND showtimeId <> ? ");
        }

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setString(index++, roomId);
            ps.setTimestamp(index++, Timestamp.valueOf(endTime));
            ps.setTimestamp(index++, Timestamp.valueOf(startTime));

            if (currentShowtimeId != null && !currentShowtimeId.trim().isEmpty()) {
                ps.setString(index++, currentShowtimeId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public List<GeneratedShowtime> buildAutoSchedule(
            LocalDate fromDate,
            LocalDate toDate,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime goldenHour,
            List<String> movieIds,
            double basePrice,
            double vipExtra,
            double coupleExtra
    ) throws Exception {

        List<GeneratedShowtime> result = new ArrayList<>();

        if (movieIds == null || movieIds.isEmpty()) {
            throw new Exception("Bạn chưa chọn phim để tạo suất chiếu.");
        }

        List<Movie> selectedMovies = new ArrayList<>();

        for (String movieId : movieIds) {
            Movie m = movieDAO.getById(movieId);

            if (m != null && m.getActive() == MovieStatus.ACTIVE ) {
                selectedMovies.add(m);
            }
        }

        if (selectedMovies.isEmpty()) {
            throw new Exception("Không tìm thấy phim hợp lệ.");
        }

        List<Room> rooms = roomDAO.getAll();

        if (rooms.isEmpty()) {
            throw new Exception("Không có phòng chiếu đang hoạt động.");
        }

        selectedMovies.sort(Comparator.comparing(Movie::getTitle));
        rooms.sort(Comparator.comparing(Room::getRoomId));

        int nextNumber = getNextShowtimeNumber();
        int movieIndex = 0;
        int bufferMinutes = 15;

        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {

            for (Room room : rooms) {
                LocalDateTime cursor = LocalDateTime.of(date, openTime);

                while (cursor.toLocalTime().isBefore(closeTime)) {
                    Movie movie = selectedMovies.get(movieIndex % selectedMovies.size());

                    LocalDateTime start = cursor;
                    LocalDateTime end = start.plusMinutes(movie.getDuration());

                    if (end.toLocalTime().isAfter(closeTime)) {
                        break;
                    }

                    if (!existsConflict(null, room.getRoomId(), start, end)
                            && !hasDraftConflict(result, room.getRoomId(), start, end)) {

                        boolean isGolden = isGoldenHour(start.toLocalTime(), goldenHour);
                        double finalBasePrice = isGolden ? basePrice + 10000 : basePrice;

                        String autoId = String.format("SC%03d", nextNumber++);

                        result.add(new GeneratedShowtime(
                                autoId,
                                movie.getId(),
                                movie.getTitle(),
                                room.getRoomId(),
                                room.getName(),
                                start,
                                end,
                                finalBasePrice,
                                vipExtra,
                                coupleExtra,
                                true,
                                isGolden
                        ));

                        cursor = end.plusMinutes(bufferMinutes);
                        movieIndex++;
                    } else {
                        cursor = cursor.plusMinutes(15);
                    }
                }
            }
        }

        return result;
    }

    public int insertGeneratedShowtimes(List<GeneratedShowtime> list) throws Exception {
        if (list == null || list.isEmpty()) {
            throw new Exception("Chưa có suất chiếu nào để lưu.");
        }

        int insertedCount = 0;

        String sql =
                "INSERT INTO Showtime(showtimeId, movieId, roomId, startTime, endTime, basePrice, vipExtra, coupleExtra, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (GeneratedShowtime s : list) {

                    if (existsConflict(null, s.getRoomId(), s.getStartTime(), s.getEndTime())) {
                        continue;
                    }

                    String showtimeId = s.getShowtimeId();

                    while (existsById(showtimeId)) {
                        showtimeId = generateNextShowtimeId();
                    }

                    ps.setString(1, showtimeId);
                    ps.setString(2, s.getMovieId());
                    ps.setString(3, s.getRoomId());
                    ps.setTimestamp(4, Timestamp.valueOf(s.getStartTime()));
                    ps.setTimestamp(5, Timestamp.valueOf(s.getEndTime()));
                    ps.setDouble(6, s.getBasePrice());
                    ps.setDouble(7, s.getVipExtra());
                    ps.setDouble(8, s.getCoupleExtra());
                    ps.setBoolean(9, s.isActive());

                    ps.addBatch();
                    insertedCount++;
                }

                ps.executeBatch();
                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }

        return insertedCount;
    }

    private boolean hasDraftConflict(List<GeneratedShowtime> list,
                                     String roomId,
                                     LocalDateTime start,
                                     LocalDateTime end) {

        for (GeneratedShowtime s : list) {
            if (!s.getRoomId().equals(roomId)) {
                continue;
            }

            boolean conflict = start.isBefore(s.getEndTime()) && end.isAfter(s.getStartTime());

            if (conflict) {
                return true;
            }
        }

        return false;
    }

    private boolean isGoldenHour(LocalTime startTime, LocalTime goldenHour) {
        if (goldenHour == null) {
            return false;
        }

        LocalTime goldenEnd = goldenHour.plusHours(2);

        return !startTime.isBefore(goldenHour) && startTime.isBefore(goldenEnd);
    }

    private boolean existsById(String showtimeId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Showtime WHERE showtimeId = ?";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, showtimeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private int getNextShowtimeNumber() throws Exception {
        String sql =
                "SELECT showtimeId FROM Showtime " +
                "WHERE showtimeId LIKE 'SC%' " +
                "ORDER BY CAST(SUBSTRING(showtimeId, 3) AS UNSIGNED) DESC " +
                "LIMIT 1";

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("showtimeId");
                String numberPart = lastId.substring(2);

                try {
                    return Integer.parseInt(numberPart) + 1;
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }

        return 1;
    }

    private String generateNextShowtimeId() throws Exception {
        return String.format("SC%03d", getNextShowtimeNumber());
    }

    private ShowtimeView mapView(ResultSet rs) throws SQLException {
        return new ShowtimeView(
                rs.getString("showtimeId"),
                rs.getString("movieId"),
                rs.getString("title"),
                rs.getInt("duration"),
                rs.getString("roomId"),
                rs.getString("roomName"),
                rs.getTimestamp("startTime").toLocalDateTime(),
                rs.getTimestamp("endTime").toLocalDateTime(),
                rs.getDouble("basePrice"),
                rs.getDouble("vipExtra"),
                rs.getDouble("coupleExtra"),
                rs.getBoolean("active")
        );
    }

    private Connection getConn() throws Exception {
        Connection conn = DBConnection.getConnection();

        if (conn == null) {
            throw new Exception("Không kết nối được MySQL.");
        }

        return conn;
    }

    public static class ShowtimeView {
        private String showtimeId;
        private String movieId;
        private String movieTitle;
        private int duration;
        private String roomId;
        private String roomName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private double basePrice;
        private double vipExtra;
        private double coupleExtra;
        private boolean active;

        public ShowtimeView(String showtimeId,
                            String movieId,
                            String movieTitle,
                            int duration,
                            String roomId,
                            String roomName,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            double basePrice,
                            double vipExtra,
                            double coupleExtra,
                            boolean active) {

            this.showtimeId = showtimeId;
            this.movieId = movieId;
            this.movieTitle = movieTitle;
            this.duration = duration;
            this.roomId = roomId;
            this.roomName = roomName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.basePrice = basePrice;
            this.vipExtra = vipExtra;
            this.coupleExtra = coupleExtra;
            this.active = active;
        }

        public String getShowtimeId() { return showtimeId; }
        public String getMovieId() { return movieId; }
        public String getMovieTitle() { return movieTitle; }
        public int getDuration() { return duration; }
        public String getRoomId() { return roomId; }
        public String getRoomName() { return roomName; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public double getBasePrice() { return basePrice; }
        public double getVipExtra() { return vipExtra; }
        public double getCoupleExtra() { return coupleExtra; }
        public boolean isActive() { return active; }
    }

    public static class GeneratedShowtime {
        private String showtimeId;
        private String movieId;
        private String movieTitle;
        private String roomId;
        private String roomName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private double basePrice;
        private double vipExtra;
        private double coupleExtra;
        private boolean active;
        private boolean goldenHour;

        public GeneratedShowtime(String showtimeId,
                                 String movieId,
                                 String movieTitle,
                                 String roomId,
                                 String roomName,
                                 LocalDateTime startTime,
                                 LocalDateTime endTime,
                                 double basePrice,
                                 double vipExtra,
                                 double coupleExtra,
                                 boolean active,
                                 boolean goldenHour) {
            this.showtimeId = showtimeId;
            this.movieId = movieId;
            this.movieTitle = movieTitle;
            this.roomId = roomId;
            this.roomName = roomName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.basePrice = basePrice;
            this.vipExtra = vipExtra;
            this.coupleExtra = coupleExtra;
            this.active = active;
            this.goldenHour = goldenHour;
        }

        public String getShowtimeId() { return showtimeId; }
        public String getMovieId() { return movieId; }
        public String getMovieTitle() { return movieTitle; }
        public String getRoomId() { return roomId; }
        public String getRoomName() { return roomName; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public double getBasePrice() { return basePrice; }
        public double getVipExtra() { return vipExtra; }
        public double getCoupleExtra() { return coupleExtra; }
        public boolean isActive() { return active; }
        public boolean isGoldenHour() { return goldenHour; }
    }
}