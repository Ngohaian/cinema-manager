package cinema.form.panel;
public class ThongTinPanel extends javax.swing.JPanel {

    public ThongTinPanel() {
        initComponents();
    }

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

    public void loadEmployee(cinema.models.Employee emp) {
    LEmployeeName.setText(emp.getName());
    LEmployeePosition.setText(emp.getPosition().getDisplayName());
    LEmployeeId.setText(emp.getId());
    LEmployeeHireDate.setText(emp.getHireDate().format(
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    LEmployeeUsername.setText(emp.getUsername());
    LEmployeePhone.setText(emp.getPhone());
    LEmployeeEmail.setText(emp.getEmail());
    LEmployeeStatus.setText(
        emp.getStatus() == cinema.models.Employee.EmployeeStatus.ACTIVE 
        ? "Vẫn còn làm việc" : "Đã nghỉ việc");

    for (var l : jButton1.getActionListeners()) jButton1.removeActionListener(l);
    jButton1.addActionListener(e -> openSuaHoSo(emp));

    for (var l : jButton3.getActionListeners()) jButton3.removeActionListener(l);
    jButton3.addActionListener(e -> openDoiMatKhau(emp));
}

private void openSuaHoSo(cinema.models.Employee emp) {
    
    javax.swing.JDialog d = new javax.swing.JDialog();
    d.setTitle("Chỉnh sửa hồ sơ");
    d.setModal(true);
    d.setSize(430, 390);
    d.setLocationRelativeTo(null);
    javax.swing.JPanel p = new javax.swing.JPanel(null);
    p.setBackground(java.awt.Color.WHITE);

    addField(p, "Họ tên",  30,  20, 370); 
    javax.swing.JTextField tfTen   = (javax.swing.JTextField) p.getComponent(1);
    tfTen.setText(emp.getName());
    addField(p, "Tên đăng nhập", 30,  80, 370);  // ← THÊM MỚI
    javax.swing.JTextField tfUser  = (javax.swing.JTextField) p.getComponent(3);
    tfUser.setText(emp.getUsername());
    addField(p, "Số điện thoại",    30, 140, 170);
    javax.swing.JTextField tfSDT   = (javax.swing.JTextField) p.getComponent(5);
    tfSDT.setText(emp.getPhone());
    addField(p, "Email",   220, 140, 180);
    javax.swing.JTextField tfEmail = (javax.swing.JTextField) p.getComponent(7);
    tfEmail.setText(emp.getEmail());

    javax.swing.JButton btn = new javax.swing.JButton("Lưu thay đổi");
    btn.setBounds(30, 220, 370, 38);
    btn.setBackground(new java.awt.Color(0, 146, 255));
    btn.setForeground(java.awt.Color.WHITE);
    btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btn.setFocusPainted(false); btn.setBorderPainted(false);
    btn.addActionListener(e2 -> {
        String ten   = tfTen.getText().trim();
        String user  = tfUser.getText().trim();
        String sdt   = tfSDT.getText().trim();
        String email = tfEmail.getText().trim();
        // Highlight border đỏ nếu trống, xám nếu có chữ
        java.awt.Color errorColor  = new java.awt.Color(220, 53, 69);
        java.awt.Color normalColor = new java.awt.Color(200, 200, 200);
        tfTen.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(ten.isEmpty()   ? errorColor : normalColor),
            javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
        tfUser.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(user.isEmpty()  ? errorColor : normalColor),
            javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
        tfSDT.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(sdt.isEmpty()   ? errorColor : normalColor),
            javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
        tfEmail.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(email.isEmpty() ? errorColor : normalColor),
            javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));

        // Nếu có ô nào trống thì dừng, không lưu
        if (ten.isEmpty() || user.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(d,
                "Vui lòng điền đầy đủ thông tin, không được để trống!",
                "Thiếu thông tin", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        
        }
        if (!sdt.matches("\\d{10}")) {
            javax.swing.JOptionPane.showMessageDialog(d,
                "Số điện thoại chỉ được chứa chữ số và tối đa 10 ký tự!",
                "Không hợp lệ", javax.swing.JOptionPane.WARNING_MESSAGE);
            tfSDT.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(errorColor),
                javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
            return;
        }

        // Kiểm tra email phải có @gmail.com
        if (!email.endsWith("@gmail.com")) {
            javax.swing.JOptionPane.showMessageDialog(d,
                "Email phải có dạng ...@gmail.com!",
                "Không hợp lệ", javax.swing.JOptionPane.WARNING_MESSAGE);
            tfEmail.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(errorColor),
                javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
            return;
        }

        emp.setName(ten);
        emp.setUsername(user);
        emp.setPhone(sdt);
        emp.setEmail(email);
        new cinema.dao.EmployeeDAO().updateEmployee(emp);
        loadEmployee(emp);
        d.dispose();
        javax.swing.JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
    });
    p.add(btn);
    d.setContentPane(p);
    d.setVisible(true);
}

private javax.swing.JTextField addField(javax.swing.JPanel p, String label, int x, int y, int w) {
    javax.swing.JLabel lb = new javax.swing.JLabel(label);
    lb.setBounds(x, y, w, 20);
    lb.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    javax.swing.JTextField tf = new javax.swing.JTextField();
    tf.setBounds(x, y + 24, w, 32);
    tf.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    tf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200,200,200)),
        javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
    p.add(lb); p.add(tf);
    return tf;
}

private void openDoiMatKhau(cinema.models.Employee emp) {
    javax.swing.JDialog d = new javax.swing.JDialog();
    d.setTitle("Đổi mật khẩu");
    d.setModal(true);
    d.setSize(380, 260);
    d.setLocationRelativeTo(null);
    javax.swing.JPanel p = new javax.swing.JPanel(null);
    p.setBackground(java.awt.Color.WHITE);

    javax.swing.JLabel lbCu = new javax.swing.JLabel("Mật khẩu hiện tại");
    lbCu.setBounds(30, 20, 310, 20);
    lbCu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    javax.swing.JPasswordField tfCu = new javax.swing.JPasswordField();
    tfCu.setBounds(30, 44, 310, 32);
    tfCu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    tfCu.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200,200,200)),
        javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));

    javax.swing.JLabel lbMoi = new javax.swing.JLabel("Mật khẩu mới");
    lbMoi.setBounds(30, 90, 310, 20);
    lbMoi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    javax.swing.JPasswordField tfMoi = new javax.swing.JPasswordField();
    tfMoi.setBounds(30, 114, 310, 32);
    tfMoi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    tfMoi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200,200,200)),
        javax.swing.BorderFactory.createEmptyBorder(4,8,4,8)));
    javax.swing.JButton btn = new javax.swing.JButton("Xác nhận");
    btn.setBounds(30, 165, 310, 38);
    btn.setBackground(new java.awt.Color(0, 146, 255));
    btn.setForeground(java.awt.Color.WHITE);
    btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    btn.setFocusPainted(false); btn.setBorderPainted(false);
    btn.addActionListener(e2 -> {
        String cu  = new String(tfCu.getPassword()).trim();
        String moi = new String(tfMoi.getPassword()).trim();
        boolean matKhauDung = false;
        try (java.sql.Connection conn = cinema.DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM employee WHERE username = ? AND password = ?")) {
            ps.setString(1, emp.getUsername());
            ps.setString(2, cu);
            matKhauDung = ps.executeQuery().next();
        } catch (java.sql.SQLException ex) { ex.printStackTrace(); }
        if (!matKhauDung) {
            javax.swing.JOptionPane.showMessageDialog(d, "Mật khẩu hiện tại không đúng!", "Lỗi",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (moi.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(d, "Mật khẩu mới không được để trống!", "Lỗi",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        emp.setPassword(moi);
        new cinema.dao.EmployeeDAO().updateEmployee(emp);
        d.dispose();
        javax.swing.JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!");
    });
    p.add(lbCu); p.add(tfCu);
    p.add(lbMoi); p.add(tfMoi);
    p.add(btn);
    d.setContentPane(p);
    d.setVisible(true);
}

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
