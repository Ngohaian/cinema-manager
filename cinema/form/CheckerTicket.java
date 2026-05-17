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

    public CheckerTicket(Employee employee) {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {

        lblTitle = new JLabel();
        txtMaVe = new JTextField();
        btnCheck = new JButton();
        lblKetQua = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiểm tra vé");
        setSize(500, 350);

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setText("KIỂM TRA VÉ");

        txtMaVe.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        btnCheck.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCheck.setText("Kiểm tra");

        lblKetQua.setFont(new Font("Segoe UI", Font.BOLD, 20));

        btnCheck.addActionListener(evt -> checkTicket());

        GroupLayout layout =
                new GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(80)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)

                                        .addComponent(lblTitle)

                                        .addComponent(txtMaVe,
                                                GroupLayout.PREFERRED_SIZE,
                                                300,
                                                GroupLayout.PREFERRED_SIZE)

                                        .addComponent(btnCheck,
                                                GroupLayout.PREFERRED_SIZE,
                                                150,
                                                GroupLayout.PREFERRED_SIZE)

                                        .addComponent(lblKetQua))
                                .addGap(80))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(60)

                        .addComponent(lblTitle)

                        .addGap(40)

                        .addComponent(txtMaVe,
                                GroupLayout.PREFERRED_SIZE,
                                45,
                                GroupLayout.PREFERRED_SIZE)

                        .addGap(30)

                        .addComponent(btnCheck,
                                GroupLayout.PREFERRED_SIZE,
                                45,
                                GroupLayout.PREFERRED_SIZE)

                        .addGap(40)

                        .addComponent(lblKetQua)
        );
    }

    private TicketStatus getTicketStatus(String ticketId)
            throws Exception {

        String sql =
                "SELECT status FROM Ticket WHERE ticketId=?";

        Connection conn = DBConnection.getConnection();

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, ticketId);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            return TicketStatus.valueOf(
                    rs.getString("status")
            );
        }

        return null;
    }

    private void updateUsed(String ticketId)
            throws Exception {

        String sql =
                "UPDATE Ticket SET status='Used' WHERE ticketId=?";

        Connection conn = DBConnection.getConnection();

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ps.setString(1, ticketId);

        ps.executeUpdate();
    }

    private void checkTicket() {

        String maVe = txtMaVe.getText().trim();

        if(maVe.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã vé"
            );

            return;
        }

        try {

            TicketStatus status =
                    getTicketStatus(maVe);

            if(status == null) {

                lblKetQua.setForeground(Color.RED);

                lblKetQua.setText(
                        "❌ Vé không tồn tại"
                );
            }

            else if(status == TicketStatus.Available) {

                lblKetQua.setForeground(Color.RED);

                lblKetQua.setText(
                        "❌ Vé chưa thanh toán"
                );
            }

            else if(status == TicketStatus.Used) {

                lblKetQua.setForeground(Color.RED);

                lblKetQua.setText(
                        "❌ Vé đã sử dụng"
                );
            }

            else if(status == TicketStatus.Canceled) {

                lblKetQua.setForeground(Color.RED);

                lblKetQua.setText(
                        "❌ Vé đã bị hủy"
                );
            }

            else if(status == TicketStatus.Sold) {

                updateUsed(maVe);

                lblKetQua.setForeground(
                        new Color(0,150,0)
                );

                lblKetQua.setText(
                        "✅ Vé hợp lệ"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }


}