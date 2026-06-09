package cinema.form;

public class LoginFrame extends javax.swing.JFrame {
    private static final int MAX_ATTEMPTS = 3; 
    private int loginAttempts = 0;

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        btn_login = new javax.swing.JButton();
        txt_username = new javax.swing.JTextField();
        lbl_error = new javax.swing.JLabel(); 

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setName("panel_login"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("ĐĂNG NHẬP");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Tên đăng nhập");

        txt_password.setName("txt_password"); 
        btn_togglePassword = new javax.swing.JButton();
        btn_togglePassword.setFocusable(false);
        btn_togglePassword.setBackground(new java.awt.Color(255, 255, 255));
        btn_togglePassword.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 6, 2, 6));
        btn_togglePassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_togglePassword.setContentAreaFilled(false);

        java.net.URL visOnUrl  = getClass().getResource("/cinema/images/visibility.png");
        java.net.URL visOffUrl = getClass().getResource("/cinema/images/visibility_off.png");
        javax.swing.ImageIcon iconShow = new javax.swing.ImageIcon(
            new javax.swing.ImageIcon(visOnUrl).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
        javax.swing.ImageIcon iconHide = new javax.swing.ImageIcon(
            new javax.swing.ImageIcon(visOffUrl).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btn_togglePassword.setIcon(iconHide); 

        btn_togglePassword.addActionListener(e -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                txt_password.setEchoChar((char) 0);
                btn_togglePassword.setIcon(iconShow);  // đang hiện → icon visibility
            } else {
                txt_password.setEchoChar('●');
                btn_togglePassword.setIcon(iconHide);  // đang ẩn → icon visibility_off
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Mật khẩu");

        btn_login.setBackground(new java.awt.Color(0, 146, 255));
        btn_login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setText("Đăng nhập");
        btn_login.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btn_login.setName("btn_login"); // NOI18N
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        txt_username.setName("txt_username"); // NOI18N
        lbl_error.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lbl_error.setForeground(new java.awt.Color(220, 50, 50));
        lbl_error.setText("");
        
        passwordPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        passwordPanel.setBackground(new java.awt.Color(255, 255, 255));
        passwordPanel.setBorder(txt_password.getBorder());
        txt_password.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 2, 0, 2));
        passwordPanel.add(txt_password, java.awt.BorderLayout.CENTER);
        passwordPanel.add(btn_togglePassword, java.awt.BorderLayout.EAST);
        passwordPanel.setPreferredSize(new java.awt.Dimension(257, 30));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(passwordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)                            .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_error, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(19, 19, 19)
                .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(17, 17, 17)
                .addComponent(passwordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGap(6, 6, 6)
                .addComponent(lbl_error)
                .addGap(14, 14, 14)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
     private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {
        String username = txt_username.getText().trim();
        String password = new String(txt_password.getPassword()).trim();
 
        if (username.isEmpty() || password.isEmpty()) {
            lbl_error.setText("Vui lòng nhập đầy đủ thông tin.");
            return;
        }
         if (loginAttempts >= MAX_ATTEMPTS) {
            lbl_error.setText("Tài khoản bị khóa do nhập sai " + MAX_ATTEMPTS + " lần.");
            btn_login.setEnabled(false);
            return;
        }
 
        cinema.models.Employee emp = checkLogin(username, password);
        if (emp != null) {
            switch (emp.getPosition()) {
                case MANAGER:
                    new ManagerFrame(emp).setVisible(true);
                    break;
                case TICKET_SELLER:
                    new SellTicketFrame(emp).setVisible(true);
                    break;
                case TICKET_CHECKER:
                    new CheckerTicket(emp).setVisible(true);
                    break;
            }
            this.dispose();
        } else {
            loginAttempts++;
            int remaining = MAX_ATTEMPTS - loginAttempts;
            if (remaining > 0) {
                lbl_error.setText("Sai tài khoản hoặc mật khẩu. Còn " + remaining + " lần thử.");
            } else {
                lbl_error.setText("Tài khoản bị khóa do nhập sai " + MAX_ATTEMPTS + " lần.");
                btn_login.setEnabled(false);
            }
            txt_password.setText("");
        }
    }

    private cinema.models.Employee checkLogin(String username, String password) {
        String sql = "SELECT * FROM employee WHERE username = ? AND status = 'ACTIVE'";
        try (java.sql.Connection conn = cinema.DBConnection.getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (!org.mindrot.jbcrypt.BCrypt.checkpw(password, hashedPassword)) {
                    return null; // sai mật khẩu
                }
                String posStr = rs.getString("Position");
                cinema.models.Employee.Position pos;
                switch (posStr) {
                    case "Quan ly":
                        pos = cinema.models.Employee.Position.MANAGER;
                        break;
                    case "Nhan vien ban ve":
                        pos = cinema.models.Employee.Position.TICKET_SELLER;
                        break;
                    default:
                        pos = cinema.models.Employee.Position.TICKET_CHECKER;
                        break;
                }
                cinema.models.Employee emp = new cinema.models.Employee(
                    rs.getString("EmployeeName"),
                    rs.getString("EmployeePhone"),
                    rs.getString("EmployeeEmail"),
                    rs.getString("username"),
                    rs.getString("password"),
                    pos,
                    rs.getDouble("salary")
                );
                emp.setId(rs.getString("EmployeeId"));
                emp.setHireDate(rs.getDate("hireDate").toLocalDate());
                return emp;
            }
        } catch (java.sql.SQLException e) {
            lbl_error.setText("Lỗi kết nối cơ sở dữ liệu.");
            e.printStackTrace();
        }
        return null;
    }



    private javax.swing.JButton btn_login;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_username;
    private javax.swing.JLabel lbl_error;
    private javax.swing.JButton btn_togglePassword;
    private boolean passwordVisible = false;
    private javax.swing.JPanel passwordPanel; 
}
