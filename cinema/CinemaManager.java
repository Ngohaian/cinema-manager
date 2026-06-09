package cinema;

import cinema.form.*;

public class CinemaManager {
    
    public CinemaManager(){
    }
    public void login(){
            try {
            com.formdev.flatlaf.FlatLightLaf.setup(); 
            
        } catch (Exception e) {
            System.err.println("Không thể khởi tạo FlatLaf");
        }
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        }
    }

