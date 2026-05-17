package cinema.form;

import cinema.DBConnection;
import cinema.enums.TicketStatus;
import cinema.models.Employee;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckerTicket extends JFrame {

    private JLabel lblTitle;
    private JTextField txtMaVe;
    private JButton btnCheck;
    private JLabel lblKetQua;

    // góc trên
    private JButton btnXemTK;
    private JButton btnDangXuat;
    private JLabel lblUsername;

    // panel thông tin tài khoản
    private JPanel accountPanel;
    private boolean accountVisible = false;

    private Employee currentEmployee;

    private static final Color BLUE_MENU = new Color(24, 144, 255);

    // constructor nhận username từ LoginFrame
    public CheckerTicket(Employee employee) {
        this.currentEmployee = employee;
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }


    private void initComponents() {

        lblTitle  = new JLabel();
        txtMaVe   = new JTextField();
        btnCheck  = new JButton();
        lblKetQua = new JLabel();
        btnXemTK  = new JButton();
        btnDangXuat = new JButton();
        lblUsername = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiểm tra vé");
        setMinimumSize(new Dimension(500, 400));
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // ===== HEADER (góc trên) =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        lblUsername.setText("👤 " + currentEmployee.getName());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(new Color(60, 60, 60));

        // nút Xem tài khoản
        btnXemTK = new JButton("Xem tài khoản");
        btnXemTK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnXemTK.setForeground(BLUE_MENU);
        btnXemTK.setBackground(Color.WHITE);
        btnXemTK.setBorder(BorderFactory.createLineBorder(BLUE_MENU));
        btnXemTK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemTK.setFocusPainted(false);
        btnXemTK.addActionListener(e -> toggleAccountPanel());

        // nút Đăng xuất
        btnDangXuat = new JButton("Đăng xuất");
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

        // ===== ACCOUNT PANEL (ẩn ban đầu) =====
        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setBackground(new Color(245, 247, 250));
        accountPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        accountPanel.setVisible(false);

        JLabel lblInfoTitle = new JLabel("Thông tin tài khoản");
        lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfoTitle.setForeground(new Color(40, 40, 40));

        JLabel lblInfoUser = new JLabel("Tên đăng nhập: " + currentEmployee.getName());
        lblInfoUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfoUser.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // TODO: load thêm thông tin từ DB nếu cần
        JLabel lblInfoRole = new JLabel("Vai trò: Nhân viên kiểm vé");
        lblInfoRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfoRole.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        accountPanel.add(lblInfoTitle);
        accountPanel.add(lblInfoUser);
        accountPanel.add(lblInfoRole);

        // ===== CENTER (form kiểm tra vé) =====
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

        // ===== TOP WRAP (header + accountPanel) =====
        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setBackground(Color.WHITE);
        topWrap.add(headerPanel, BorderLayout.NORTH);
        topWrap.add(accountPanel, BorderLayout.SOUTH);

        getContentPane().add(topWrap, BorderLayout.NORTH);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    // toggle hiện/ẩn panel tài khoản
    private void toggleAccountPanel() {
        accountVisible = !accountVisible;
        accountPanel.setVisible(accountVisible);
        btnXemTK.setText(accountVisible ? "Ẩn tài khoản" : "Xem tài khoản");
        revalidate();
        repaint();
    }

    // đăng xuất → mở lại LoginFrame
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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

}