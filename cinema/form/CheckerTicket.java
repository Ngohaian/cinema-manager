package cinema.form;

import cinema.DBConnection;
import cinema.enums.TicketStatus;
import cinema.form.panel.ThongTinPanel;
import cinema.models.Employee;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class CheckerTicket extends JFrame {

    private JLabel lblTitle;
    private JTextField txtMaVe;
    private JButton btnCheck;
    private JLabel lblKetQua;

    private JButton btnXemTK;
    private JButton btnDangXuat;
    private JLabel lblUsername;
    private Employee currentEmployee;
    private static final Color BLUE_MENU = new Color(24, 144, 255);

    public CheckerTicket(Employee employee) {
        this.currentEmployee = employee;
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {

        lblTitle    = new JLabel();
        txtMaVe     = new JTextField();
        btnCheck    = new JButton();
        lblKetQua   = new JLabel();
        btnXemTK    = new JButton();
        btnDangXuat = new JButton();
        lblUsername = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiểm tra vé");
        setMinimumSize(new Dimension(500, 400));
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        lblUsername.setText(currentEmployee.getName());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(new Color(60, 60, 60));

        btnXemTK.setText("Xem tài khoản");
        btnXemTK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnXemTK.setForeground(BLUE_MENU);
        btnXemTK.setBackground(Color.WHITE);
        btnXemTK.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLUE_MENU),         
            BorderFactory.createEmptyBorder(5, 15, 5, 15)        
        ));
        btnXemTK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemTK.setFocusPainted(false);
        btnXemTK.addActionListener(e -> tttaikhoan());

        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setBackground(new Color(220, 50, 50));
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.addActionListener(e -> dangXuat());

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnGroup.setBackground(Color.WHITE);
        btnGroup.add(lblUsername);
        btnGroup.add(btnXemTK);
        btnGroup.add(btnDangXuat);

        headerPanel.add(btnGroup, BorderLayout.EAST);

        // ===== CENTER =====
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setText("KIỂM TRA VÉ");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtMaVe.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        txtMaVe.setMaximumSize(new Dimension(400, 50));
        txtMaVe.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnCheck.setText("Kiểm tra");
        btnCheck.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCheck.setBackground(BLUE_MENU);
        btnCheck.setForeground(Color.WHITE);
        btnCheck.setFocusPainted(false);
        btnCheck.setBorderPainted(false);
        btnCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheck.setMaximumSize(new Dimension(400, 50));
        btnCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCheck.addActionListener(evt -> checkTicket());

        lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblKetQua.setText(" ");
        lblKetQua.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(txtMaVe);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(btnCheck);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(lblKetQua);
        centerPanel.add(Box.createVerticalGlue());

        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }
    private void tttaikhoan(){
        JDialog dialog = new JDialog(this, "Thông tin tài khoản", true);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());
        ThongTinPanel thongTinPanel = new ThongTinPanel();
        thongTinPanel.loadEmployee(currentEmployee);
        dialog.add(wrap(thongTinPanel), java.awt.BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    private JScrollPane wrap(JPanel panel){
        JScrollPane sp = new JScrollPane(panel);
        sp.getVerticalScrollBar().setUnitIncrement(20);
        sp.setBorder(null);
        sp.getViewport().setBackground(java.awt.Color.WHITE);
        return sp;
    }
    


    // ===== ĐĂNG XUẤT =====
    private void dangXuat() {

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn đăng xuất?",
            "Đăng xuất",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    // ===== DB =====
    private TicketStatus getTicketStatus(String ticketId) throws Exception {

        String sql = "SELECT status FROM Ticket WHERE ticketId=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ticketId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return TicketStatus.valueOf(rs.getString("status"));
        }
        return null;
    }

    private void updateUsed(String ticketId) throws Exception {

        String sql = "UPDATE Ticket SET status='Used' WHERE ticketId=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ticketId);
        ps.executeUpdate();
    }

    // ===== KIỂM TRA VÉ =====
    private void checkTicket() {

        String maVe = txtMaVe.getText().trim();

        if (maVe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vé");
            return;
        }

        try {
            TicketStatus status = getTicketStatus(maVe);

            if (status == null) {
                lblKetQua.setForeground(Color.RED);
                lblKetQua.setText("❌ Vé không tồn tại");
            } else if (status == TicketStatus.Available) {
                lblKetQua.setForeground(Color.RED);
                lblKetQua.setText("❌ Vé chưa thanh toán");
            } else if (status == TicketStatus.Used) {
                lblKetQua.setForeground(Color.RED);
                lblKetQua.setText("❌ Vé đã sử dụng");
            } else if (status == TicketStatus.Canceled) {
                lblKetQua.setForeground(Color.RED);
                lblKetQua.setText("❌ Vé đã bị hủy");
            } else if (status == TicketStatus.Sold) {

    updateUsed(maVe);

    lblKetQua.setForeground(new Color(0, 150, 0));

    lblKetQua.setText("✅ Vé hợp lệ");

    showTicketInfo(maVe);
}

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void showTicketInfo(String ticketId) {

    String sql = """
        SELECT
            t.ticketId,
            m.title,
            s.rowIndex,
            s.colIndex,
            st.startTime,
            r.roomName
        FROM Ticket t
        JOIN Showtime st ON t.showtimeId = st.showtimeId
        JOIN Movie m ON st.movieId = m.movieId
        JOIN Seat s ON t.seatId = s.seatId
        JOIN Room r ON st.roomId = r.roomId
        WHERE t.ticketId = ?
    """;

    try {

        Connection conn = DBConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, ticketId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String movie = rs.getString("title");

            String ghe =
                    (char)('A' + rs.getInt("rowIndex"))
                    + String.valueOf(rs.getInt("colIndex") + 1);

            String room = rs.getString("roomName");

            String time = rs.getString("startTime");

            JDialog dialog = new JDialog(this, "Thông tin vé", true);

            dialog.setSize(450, 300);

            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel();

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

            JLabel lb1 = new JLabel("🎬 Phim: " + movie);
            JLabel lb2 = new JLabel("💺 Ghế: " + ghe);
            JLabel lb3 = new JLabel("🏠 Phòng: " + room);
            JLabel lb4 = new JLabel("⏰ Suất chiếu: " + time);
            JLabel lb5 = new JLabel("✅ Vé hợp lệ - Đã check-in");

            lb1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lb2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lb3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lb4.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            lb5.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lb5.setForeground(new Color(0,150,0));

            panel.add(lb1);
            panel.add(Box.createVerticalStrut(10));

            panel.add(lb2);
            panel.add(Box.createVerticalStrut(10));

            panel.add(lb3);
            panel.add(Box.createVerticalStrut(10));

            panel.add(lb4);
            panel.add(Box.createVerticalStrut(20));

            panel.add(lb5);

            dialog.add(panel);

            dialog.setVisible(true);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}

