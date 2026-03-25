package cinema;
import javax.swing.*;
import javax.swing.border.*;

import cinema.models.Employee;

import java.awt.*;

public class Menu {
    private CinemaManager manager;
    public Menu(CinemaManager manager) {
        this.manager = manager;
    }
    public void login(){
            try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }

        JFrame frame = new JFrame("Quản lý rạp chiếu phim");
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font standardFont = new Font("Segoe UI", Font.PLAIN, 15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        title.setFont(titleFont);
        mainPanel.add(title,gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setFont(standardFont);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx =1.0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        JTextField txtUser = new JTextField(20);
        txtUser.setFont(standardFont);
        mainPanel.add(txtUser, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setFont(standardFont);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setFont(standardFont);
        mainPanel.add(txtPass, gbc);

        gbc.gridx = 1; 
        gbc.gridy = 3; 
        gbc.gridwidth = 1;
        gbc.fill=GridBagConstraints.NONE;
        gbc.anchor=GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 10, 5); 
        
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(100, 40));
        mainPanel.add(btnLogin, gbc);

        frame.add(mainPanel);
        frame.setVisible(true);

        btnLogin.addActionListener(e-> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());
            Employee emp = manager.getPosition(username, password);
            if(emp != null){
                frame.dispose();
                switch(emp.getPosition()){
                    case MANAGER
                    -> showManagerMenu();
                    case TICKET_SELLER
                    -> showSellerMenu();
                    case TICKET_CHECKER
                    -> showCheckerMenu();

                }
            }
        });
    }
    public void showManagerMenu(){
        /* danh sách menu bên trái: thống kê, phim, phòng chiếu, suất chiếu
        , khách hàng, nhân viên, hóa đơn, đăng xuất
        */ 
    }      
    public void showSellerMenu(){
        
    }
    public void showCheckerMenu(){}
}
