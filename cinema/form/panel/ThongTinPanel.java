package cinema.form.panel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
public class ThongTinPanel extends javax.swing.JPanel {

    public ThongTinPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        PThongTinCaNhan = new javax.swing.JPanel();
        LEmployeeName = new javax.swing.JLabel();
        LEmployeePosition = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        LEmployeeId = new javax.swing.JLabel();
        LEmployeeHireDate = new javax.swing.JLabel();
        LEmployeePosition1 = new javax.swing.JLabel();
        PThongTinChiTiet = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        LEmployeeUsername = new javax.swing.JLabel();
        LEmployeeEmail = new javax.swing.JLabel();
        LEmployeeStatus = new javax.swing.JLabel();
        LEmployeePhone = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(248, 250, 252));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Hồ sơ cá nhân");

        PThongTinCaNhan.setBackground(new java.awt.Color(255, 255, 255));
        PThongTinCaNhan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        LEmployeeName.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        LEmployeeName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LEmployeeName.setText("Nguyễn Văn A");

        LEmployeePosition.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LEmployeePosition.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeePosition.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LEmployeePosition.setText("Nhân viên bán vé");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel13.setLabelFor(LEmployeeId);
        jLabel13.setText("Mã nhân viên: ");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/user.png"))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel14.setLabelFor(LEmployeeId);
        jLabel14.setText("Ngày gia nhập:");

        LEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        LEmployeeId.setText("EM001");

        LEmployeeHireDate.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        LEmployeeHireDate.setText("15-01-2023");

        LEmployeePosition1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        LEmployeePosition1.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeePosition1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PThongTinCaNhanLayout = new javax.swing.GroupLayout(PThongTinCaNhan);
        PThongTinCaNhan.setLayout(PThongTinCaNhanLayout);
        PThongTinCaNhanLayout.setHorizontalGroup(
            PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PThongTinCaNhanLayout.createSequentialGroup()
                .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PThongTinCaNhanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LEmployeePosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LEmployeeName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PThongTinCaNhanLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13))
                        .addGap(49, 49, 49)
                        .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LEmployeeHireDate)
                            .addComponent(LEmployeeId))
                        .addGap(0, 403, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(PThongTinCaNhanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(LEmployeePosition1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        PThongTinCaNhanLayout.setVerticalGroup(
            PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PThongTinCaNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LEmployeeName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LEmployeePosition)
                .addGap(64, 64, 64)
                .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LEmployeeId)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(LEmployeeHireDate))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(PThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PThongTinCaNhanLayout.createSequentialGroup()
                    .addContainerGap(269, Short.MAX_VALUE)
                    .addComponent(LEmployeePosition1)
                    .addGap(166, 166, 166)))
        );

        PThongTinChiTiet.setBackground(new java.awt.Color(255, 255, 255));
        PThongTinChiTiet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        PThongTinChiTiet.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel12.setText("Số điện thoại");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Chi tiết thông tin");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel16.setText("Trạng thái hoạt động");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel17.setText("Tên đăng nhập");

        LEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LEmployeeUsername.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeeUsername.setText("a12345");

        LEmployeeEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LEmployeeEmail.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeeEmail.setText("an.nguyen@gmail.com");

        LEmployeeStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LEmployeeStatus.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeeStatus.setText("Vẫn còn làm việc");

        LEmployeePhone.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        LEmployeePhone.setForeground(new java.awt.Color(51, 51, 51));
        LEmployeePhone.setText("0912345678");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel19.setText("Email");

        jButton1.setBackground(new java.awt.Color(0, 146, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/edit.png"))); // NOI18N
        jButton1.setText(" Chỉnh sửa hồ sơ");

        jButton3.setBackground(new java.awt.Color(242, 242, 242));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/padlock.png"))); // NOI18N
        jButton3.setText(" Đổi mật khẩu");

        javax.swing.GroupLayout PThongTinChiTietLayout = new javax.swing.GroupLayout(PThongTinChiTiet);
        PThongTinChiTiet.setLayout(PThongTinChiTietLayout);
        PThongTinChiTietLayout.setHorizontalGroup(
            PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PThongTinChiTietLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PThongTinChiTietLayout.createSequentialGroup()
                        .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(LEmployeeUsername)
                                .addComponent(LEmployeeEmail)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                        .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LEmployeePhone)
                            .addComponent(LEmployeeStatus))
                        .addGap(119, 119, 119))
                    .addGroup(PThongTinChiTietLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton1)
                        .addGap(65, 65, 65)
                        .addComponent(jButton3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PThongTinChiTietLayout.setVerticalGroup(
            PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PThongTinChiTietLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel10)
                .addGap(75, 75, 75)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LEmployeeUsername)
                    .addComponent(LEmployeePhone))
                .addGap(44, 44, 44)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LEmployeeStatus)
                    .addComponent(LEmployeeEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addGroup(PThongTinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(52, 52, 52))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PThongTinCaNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(PThongTinChiTiet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(62, 62, 62))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(55, 55, 55)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PThongTinCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(PThongTinChiTiet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LEmployeeEmail;
    private javax.swing.JLabel LEmployeeHireDate;
    private javax.swing.JLabel LEmployeeId;
    private javax.swing.JLabel LEmployeeName;
    private javax.swing.JLabel LEmployeePhone;
    private javax.swing.JLabel LEmployeePosition;
    private javax.swing.JLabel LEmployeePosition1;
    private javax.swing.JLabel LEmployeeStatus;
    private javax.swing.JLabel LEmployeeUsername;
    private javax.swing.JPanel PThongTinCaNhan;
    private javax.swing.JPanel PThongTinChiTiet;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
