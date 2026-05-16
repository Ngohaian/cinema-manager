package cinema.form.panel;

import cinema.dao.EmployeeDAO;
import cinema.models.Employee;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class NhanVienManagerPanel extends javax.swing.JPanel {

    private EmployeeDAO dao = new EmployeeDAO();
    private List<Employee> danhSach;
    private List<Employee> danhSachHienThi;

    public NhanVienManagerPanel() {
        initComponents();
        setTable(jTable1);
        setupSlider();
        setupSearch();
        setupComboBoxFilter();
        danhSach = dao.getAllEmployeesFromDB();
        loadTable(danhSach);
    }

    private void setupSlider() {
        double max = dao.getMaxSalary();
        double min = dao.getMinSalary();
        jSlider1.setMaximum((int) max);
        jSlider1.setMinimum((int) min);
        jSlider1.setValue((int) max);
        jLabel1.setText(String.format("%,.0f", max));

        jSlider1.addChangeListener(e -> {
            jLabel1.setText(String.format("%,.0f", (double) jSlider1.getValue()));
            applyFilters();
        });
    }

    private void setupSearch() {
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                applyFilters(); 
            }
        });
    }

    private void setupComboBoxFilter() {
        jComboBox1.addActionListener(e -> applyFilters()); 
        jComboBox2.addActionListener(e -> applyFilters()); 
    }

    private void applyFilters() {
        String keyword  = jTextField1.getText().trim();
        String status   = jComboBox1.getSelectedIndex() == 0 ? "ALL"
                        : jComboBox1.getSelectedItem().toString();
        String position = jComboBox2.getSelectedIndex() == 0 ? "ALL"
                        : positionEnumName(jComboBox2.getSelectedItem().toString());
        double maxSalary = jSlider1.getValue();
        List<Employee> filtered = dao.filterEmployees(keyword, status, position, maxSalary);
        loadTable(filtered);
    }

    private String positionEnumName(String displayName) {
        for (Employee.Position p : Employee.Position.values()) {
            if (p.getDisplayName().equalsIgnoreCase(displayName)) return p.name();
        }
        return "ALL";
    }

    public void setTable(JTable table) {
        var header = table.getTableHeader();
        header.setBackground(new Color(235, 235, 235));
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false);

        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(0, 146, 255, 40));
        table.setSelectionForeground(Color.BLACK);
        table.setFocusable(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã NV
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Chức vụ
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // SĐT
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Mức lương
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Ngày vào làm

        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(isSelected ? new Color(0, 146, 255, 40) : Color.WHITE);
                if (Boolean.TRUE.equals(value)) {
                    label.setText("Hoạt động");
                    label.setForeground(new Color(34, 139, 34));
                } else {
                    label.setText("Ngừng");
                    label.setForeground(new Color(220, 50, 50));
                }
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                return label;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    String id = table.getValueAt(row, 0).toString();
                    Employee emp = danhSachHienThi.stream()
                        .filter(x -> x.getId().equals(id))
                        .findFirst().orElse(null);
                    if (emp != null) openSuaDialog(emp);
                }
            }
        });
    }

    private void loadTable(List<Employee> list) {
        this.danhSachHienThi = list;
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Employee emp : list) {
            model.addRow(new Object[]{
                emp.getId(),
                emp.getName(),
                emp.getPosition().getDisplayName(),
                emp.getEmail(),
                emp.getPhone(),
                String.format("%,.0f", emp.getSalary()),
                emp.getHireDate(),
                emp.getStatus() == Employee.EmployeeStatus.ACTIVE
            });
        }
    }

    private void openThemDialog() {
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Thêm nhân viên mới", true);
        dialog.setSize(530, 560);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lbMa = makeLabel("Mã nhân viên", 30, 20);
        JTextField txtMa = makeTextField(30, 45, 460);
        txtMa.setText(dao.getNextEmployeeId());
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(245, 245, 245));

        JLabel lbTen = makeLabel("Họ tên", 30, 90);
        JTextField txtTen = makeTextField(30, 115, 460);

        JLabel lbSDT = makeLabel("Số điện thoại", 30, 160);
        JTextField txtSDT = makeTextField(30, 185, 215);
        JLabel lbEmail = makeLabel("Email", 265, 160);
        JTextField txtEmail = makeTextField(265, 185, 225);

        JLabel lbUser = makeLabel("Tên đăng nhập", 30, 230);
        JTextField txtUser = makeTextField(30, 255, 215);
        JLabel lbPass = makeLabel("Mật khẩu", 265, 230);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(265, 255, 225, 35);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        JLabel lbCV = makeLabel("Chức vụ", 30, 305);
        JComboBox<String> cbCV = new JComboBox<>();
        cbCV.setBounds(30, 330, 215, 35);
        cbCV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (Employee.Position p : Employee.Position.values())
            cbCV.addItem(p.getDisplayName());

        JLabel lbLuong = makeLabel("Mức lương cơ bản", 265, 305);
        JTextField txtLuong = makeTextField(265, 330, 225);

        JButton btnThem = new JButton("+ Thêm mới");
        btnThem.setBounds(30, 400, 460, 42);
        btnThem.setBackground(new Color(0, 146, 255));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThem.setFocusPainted(false);
        btnThem.setBorderPainted(false);
        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnThem.setBackground(new Color(0, 120, 215)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnThem.setBackground(new Color(0, 146, 255)); }
        });

        btnThem.addActionListener(e -> {
            String ten   = txtTen.getText().trim();
            String sdt   = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String user  = txtUser.getText().trim();
            String pass  = new String(txtPass.getPassword()).trim();
            String luongStr = txtLuong.getText().trim();

            if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()
                    || user.isEmpty() || pass.isEmpty() || luongStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Vui lòng nhập đầy đủ thông tin!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double luong = Double.parseDouble(luongStr.replace(",", ""));
                Employee.Position pos = Employee.Position.values()[cbCV.getSelectedIndex()];
                Employee emp = new Employee(ten, sdt, email, user, pass, pos, luong);
                emp.setId(dao.getNextEmployeeId());
                if (dao.addEmployee(emp)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!");
                    dialog.dispose();
                    danhSach = dao.getAllEmployeesFromDB();
                    loadTable(danhSach);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Mức lương không hợp lệ!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(lbMa);    panel.add(txtMa);
        panel.add(lbTen);   panel.add(txtTen);
        panel.add(lbSDT);   panel.add(txtSDT);
        panel.add(lbEmail); panel.add(txtEmail);
        panel.add(lbUser);  panel.add(txtUser);
        panel.add(lbPass);  panel.add(txtPass);
        panel.add(lbCV);    panel.add(cbCV);
        panel.add(lbLuong); panel.add(txtLuong);
        panel.add(btnThem);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void openSuaDialog(Employee emp) {
        JDialog dialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Sửa thông tin nhân viên", true);
        dialog.setSize(530, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lbMa = makeLabel("Mã nhân viên", 30, 20);
        JTextField txtMa = makeTextField(30, 45, 460);
        txtMa.setText(emp.getId());
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(245, 245, 245));

        JLabel lbTen = makeLabel("Họ tên", 30, 90);
        JTextField txtTen = makeTextField(30, 115, 460);
        txtTen.setText(emp.getName());

        JLabel lbSDT = makeLabel("Số điện thoại", 30, 160);
        JTextField txtSDT = makeTextField(30, 185, 215);
        txtSDT.setText(emp.getPhone());

        JLabel lbEmail = makeLabel("Email", 265, 160);
        JTextField txtEmail = makeTextField(265, 185, 225);
        txtEmail.setText(emp.getEmail());

        JLabel lbCV = makeLabel("Chức vụ", 30, 230);
        JComboBox<String> cbCV = new JComboBox<>();
        cbCV.setBounds(30, 255, 215, 35);
        cbCV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        for (Employee.Position p : Employee.Position.values())
            cbCV.addItem(p.getDisplayName());
        cbCV.setSelectedItem(emp.getPosition().getDisplayName());

        JLabel lbTT = makeLabel("Trạng thái", 265, 230);
        JComboBox<String> cbTT = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        cbTT.setBounds(265, 255, 225, 35);
        cbTT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTT.setSelectedItem(emp.getStatus().name());

        JLabel lbLuong = makeLabel("Mức lương cơ bản", 30, 305);
        JTextField txtLuong = makeTextField(30, 330, 460);
        txtLuong.setText(String.valueOf(emp.getSalary()));

        JButton btnLuu = new JButton("Lưu thay đổi");
        btnLuu.setBounds(30, 395, 460, 42);
        btnLuu.setBackground(new Color(0, 146, 255));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLuu.setFocusPainted(false);
        btnLuu.setBorderPainted(false);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLuu.addActionListener(e -> {
            try {
                emp.setName(txtTen.getText().trim());
                emp.setPhone(txtSDT.getText().trim());
                emp.setEmail(txtEmail.getText().trim());
                emp.setPosition(Employee.Position.values()[cbCV.getSelectedIndex()]);
                emp.setStatus(Employee.EmployeeStatus.valueOf(cbTT.getSelectedItem().toString()));
                double luong = Double.parseDouble(txtLuong.getText().trim().replace(",", ""));
                emp.setSalary(luong);
                if (dao.updateEmployee(emp)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    dialog.dispose();
                    danhSach = dao.getAllEmployeesFromDB();
                    loadTable(danhSach);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lbMa);    panel.add(txtMa);
        panel.add(lbTen);   panel.add(txtTen);
        panel.add(lbSDT);   panel.add(txtSDT);
        panel.add(lbEmail); panel.add(txtEmail);
        panel.add(lbCV);    panel.add(cbCV);
        panel.add(lbTT);    panel.add(cbTT);
        panel.add(lbLuong); panel.add(txtLuong);
        panel.add(btnLuu);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JLabel makeLabel(String text, int x, int y) {
        JLabel lb = new JLabel(text);
        lb.setBounds(x, y, 300, 25);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return lb;
    }

    private JTextField makeTextField(int x, int y, int w) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, 35);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(248, 250, 252));
        setPreferredSize(new java.awt.Dimension(759, 779));

        jPanel2.setBackground(new java.awt.Color(248, 250, 252));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Mã NV", "Tên NV", "Chức vụ", "Email", "SĐT", "Mức lương", "Ngày vào làm", "Trạng thái"}
        ) {
            Class[] types = {String.class, String.class, String.class,
                String.class, String.class, String.class, Object.class, Boolean.class};
            boolean[] canEdit = {false, false, false, false, false, false, false, false};
            public Class getColumnClass(int i) { return types[i]; }
            public boolean isCellEditable(int r, int c) { return canEdit[c]; }
        });
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup().addComponent(jScrollPane2));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel9.setText("Danh sách nhân viên");
        jButton1.setBackground(new java.awt.Color(0, 146, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setText("+ Thêm nhân viên");
        jButton1.setPreferredSize(new java.awt.Dimension(210, 40));
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(e -> openThemDialog()); // ← gọi dialog

        jPanel1.setBackground(java.awt.Color.WHITE);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 15));
        jLabel2.setText("Tìm kiếm");
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15));
        jLabel3.setText("Trạng thái");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15));
        jLabel4.setText("Mức lương tối đa");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 15));
        jLabel5.setText("Chức vụ");
        jLabel1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("--");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Tất cả", "ACTIVE", "INACTIVE"}));
        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Tất cả",
                Employee.Position.TICKET_SELLER.getDisplayName(),
                Employee.Position.MANAGER.getDisplayName(),
                Employee.Position.TICKET_CHECKER.getDisplayName()}));
        jComboBox2.setFont(new java.awt.Font("Segoe UI", 0, 14));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jTextField1.setToolTipText("Nhập tên hoặc mã nhân viên");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup()
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup()
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, 0, 150, Short.MAX_VALUE))
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup()
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, 0, 150, Short.MAX_VALUE))
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup()
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, 0, 180, Short.MAX_VALUE))
                .addGap(30)
                .addGroup(jPanel1Layout.createParallelGroup()
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(10)
                        .addComponent(jLabel1, 80, 80, 80))
                    .addComponent(jSlider1, 0, 200, Short.MAX_VALUE))
                .addGap(20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup()
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2).addGap(9)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3).addGap(9)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5).addGap(9)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup()
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20).addComponent(jLabel1)))
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addGap(30)
                .addGroup(layout.createParallelGroup()
                    .addComponent(jPanel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addGap(40)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel9;
    private javax.swing.JPanel jPanel1, jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
}