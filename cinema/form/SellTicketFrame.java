package cinema.form;
import cinema.form.panel.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
public class SellTicketFrame extends javax.swing.JFrame {
    private JPanel indicator = new JPanel(); // Tao thanh danh dau cho chuc nang dang duoc chon
    private JLabel selectedButton = null;
    private JLabel[] menuLabels;
    private NhanVienManagerPanel banVePanel = new NhanVienManagerPanel();
    private LichSuHDPanel lichSuPanel = new LichSuHDPanel();
    private PhimPanel phimPanel = new PhimPanel();
    private LichChieuPanel lichChieuPanel = new LichChieuPanel();
    private ThongTinPanel thongTinPanel = new ThongTinPanel();
    private void setHoverChucNang(JLabel JLabel){
        MouseAdapter hoverEffect = new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                e.getComponent().setBackground(new Color(81,179,255));
            }
            @Override
            public void mouseExited(MouseEvent e){
                e.getComponent().setBackground(new Color(0,146,255));
            }
        };
        
        JLabel.addMouseListener(hoverEffect);
    } 
    private void setHoverLogOut(JLabel JLabel){
        MouseAdapter hoverEffect = new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                e.getComponent().setForeground(new Color(255,51,51));
            }
            @Override
            public void mouseExited(MouseEvent e){
                e.getComponent().setForeground(Color.white);
            }
        };
        JLabel.addMouseListener(hoverEffect);
    } 
    
    private void setSelectedButton(JLabel clickedButton){
        if(selectedButton != null && selectedButton != clickedButton){
            selectedButton.setForeground(Color.white);
        }
        clickedButton.setForeground(new Color(0,74,130));
        indicator.setBounds(clickedButton.getX()-10, clickedButton.getY(), 10, clickedButton.getHeight());
        indicator.setVisible(true);
        selectedButton = clickedButton;
        pMenu.repaint();
        
    }
    private void showPanel(String name){
        CardLayout cl = (CardLayout) pContent.getLayout();
        cl.show(pContent, name);
    }
    private void customizeScrollBar(JScrollPane JScroll){
        scrollPane1.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane1.getVerticalScrollBar().setUI(new BasicScrollBarUI(){

        @Override
        protected void paintThumb(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(new java.awt.Color(180, 180, 180));
            // Bo góc thanh trượt
            g2.fillRoundRect(thumbBounds.x + 4, thumbBounds.y + 2, thumbBounds.width - 8, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }

        @Override
        protected void paintTrack(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        @Override
        protected javax.swing.JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        @Override
        protected javax.swing.JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        private javax.swing.JButton createZeroButton() {
            javax.swing.JButton b = new javax.swing.JButton();
            b.setPreferredSize(new java.awt.Dimension(0, 0));
            return b;
        }
        
    });
    
    scrollPane1.setBorder(null);
    scrollPane1.getViewport().setBackground(java.awt.Color.WHITE);
}

    public SellTicketFrame() {
        initComponents();
        customizeScrollBar(scrollPane1);
        menuLabels = new JLabel[]{btnBanVe, btnPhim, btnLichChieu, btnLichSu};
        for(int i=0;i<menuLabels.length;i++){
            final int index = i;
            setHoverChucNang(menuLabels[i]);
            menuLabels[i].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    setSelectedButton(menuLabels[index]);
                }
            });
        }
        setHoverLogOut(btnDangXuat);
        indicator.setBackground(new Color(0,74,130));
        indicator.setSize(10,btnBanVe.getHeight());
        indicator.setVisible(false);
        pMenu.add(indicator);
        
        // chuyen doi giua cac trang
        CardLayout card = new CardLayout();
        pContent.setLayout(card);
        pContent.add(banVePanel, "BanVe");
        pContent.add(phimPanel, "Phim");
        pContent.add(lichChieuPanel,"LichChieu");
        pContent.add(lichSuPanel,"LichSu");
        pContent.add(thongTinPanel, "ThongTin");
        
        
        
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pSellTicket = new javax.swing.JPanel();
        pMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnPhim = new javax.swing.JLabel();
        btnLichChieu = new javax.swing.JLabel();
        btnBanVe = new javax.swing.JLabel();
        btnLichSu = new javax.swing.JLabel();
        btnDangXuat = new javax.swing.JLabel();
        pThongTin = new javax.swing.JPanel();
        lTen = new javax.swing.JLabel();
        lChucVu = new javax.swing.JLabel();
        scrollPane1 = new javax.swing.JScrollPane();
        pContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("frameSellTicket"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pSellTicket.setBackground(new java.awt.Color(255, 255, 255));

        pMenu.setBackground(new java.awt.Color(0, 146, 255));
        pMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pMenu.setMaximumSize(new java.awt.Dimension(300, 32767));
        pMenu.setPreferredSize(new java.awt.Dimension(150, 532));

        jLabel1.setFont(new java.awt.Font("Leelawadee", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Beta");

        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("cinema");

        btnPhim.setBackground(new java.awt.Color(0, 146, 255));
        btnPhim.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnPhim.setForeground(new java.awt.Color(255, 255, 255));
        btnPhim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPhim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/video.png"))); // NOI18N
        btnPhim.setLabelFor(btnBanVe);
        btnPhim.setText(" Phim");
        btnPhim.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnPhim.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPhim.setOpaque(true);
        btnPhim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPhimMouseClicked(evt);
            }
        });

        btnLichChieu.setBackground(new java.awt.Color(0, 146, 255));
        btnLichChieu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnLichChieu.setForeground(new java.awt.Color(255, 255, 255));
        btnLichChieu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLichChieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/calendar.png"))); // NOI18N
        btnLichChieu.setText(" Lịch chiếu");
        btnLichChieu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnLichChieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLichChieu.setOpaque(true);
        btnLichChieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLichChieuMouseClicked(evt);
            }
        });

        btnBanVe.setBackground(new java.awt.Color(0, 146, 255));
        btnBanVe.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnBanVe.setForeground(new java.awt.Color(255, 255, 255));
        btnBanVe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBanVe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/ticket.png"))); // NOI18N
        btnBanVe.setText(" Bán vé");
        btnBanVe.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnBanVe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBanVe.setOpaque(true);
        btnBanVe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBanVeMouseClicked(evt);
            }
        });

        btnLichSu.setBackground(new java.awt.Color(0, 146, 255));
        btnLichSu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnLichSu.setForeground(new java.awt.Color(255, 255, 255));
        btnLichSu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLichSu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/history.png"))); // NOI18N
        btnLichSu.setText(" Lịch sử hóa đơn");
        btnLichSu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnLichSu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLichSu.setOpaque(true);
        btnLichSu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLichSuMouseClicked(evt);
            }
        });

        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/logout.png"))); // NOI18N
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDangXuatMouseClicked(evt);
            }
        });

        pThongTin.setBackground(new java.awt.Color(0, 146, 255));
        pThongTin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pThongTin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pThongTinMouseClicked(evt);
            }
        });

        lTen.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lTen.setForeground(new java.awt.Color(255, 255, 255));
        lTen.setText("Nguyễn Văn A");

        lChucVu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lChucVu.setForeground(new java.awt.Color(204, 204, 204));
        lChucVu.setText("Nhân viên bán vé");

        javax.swing.GroupLayout pThongTinLayout = new javax.swing.GroupLayout(pThongTin);
        pThongTin.setLayout(pThongTinLayout);
        pThongTinLayout.setHorizontalGroup(
            pThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pThongTinLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lChucVu)
                    .addComponent(lTen))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pThongTinLayout.setVerticalGroup(
            pThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lTen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lChucVu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pMenuLayout = new javax.swing.GroupLayout(pMenu);
        pMenu.setLayout(pMenuLayout);
        pMenuLayout.setHorizontalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(btnDangXuat)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pMenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(29, 29, 29))
            .addGroup(pMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnBanVe, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pMenuLayout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(95, 95, 95)))
                    .addComponent(btnPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLichChieu, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLichSu, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pMenuLayout.createSequentialGroup()
                .addComponent(pThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pMenuLayout.setVerticalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(70, 70, 70)
                .addComponent(btnBanVe, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnLichChieu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnLichSu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnDangXuat)
                .addGap(18, 18, 18))
        );

        pContent.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pContentLayout = new javax.swing.GroupLayout(pContent);
        pContent.setLayout(pContentLayout);
        pContentLayout.setHorizontalGroup(
            pContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 752, Short.MAX_VALUE)
        );
        pContentLayout.setVerticalGroup(
            pContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );

        scrollPane1.setViewportView(pContent);

        javax.swing.GroupLayout pSellTicketLayout = new javax.swing.GroupLayout(pSellTicket);
        pSellTicket.setLayout(pSellTicketLayout);
        pSellTicketLayout.setHorizontalGroup(
            pSellTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSellTicketLayout.createSequentialGroup()
                .addComponent(pMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE))
        );
        pSellTicketLayout.setVerticalGroup(
            pSellTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
            .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pSellTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pSellTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1010, 787));
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void formWindowOpened(java.awt.event.WindowEvent evt) {                                  
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        showPanel("BanVe");
        setSelectedButton(btnBanVe);
    }                                 

    private void btnDangXuatMouseClicked(java.awt.event.MouseEvent evt) {                                         
        this.dispose();
        new LoginFrame().setVisible(true);
    }                                        

    private void btnBanVeMouseClicked(java.awt.event.MouseEvent evt) {                                      
        showPanel("BanVe");
    }                                     

    private void btnPhimMouseClicked(java.awt.event.MouseEvent evt) {                                     
        showPanel("Phim");
    }                                    

    private void btnLichChieuMouseClicked(java.awt.event.MouseEvent evt) {                                          
        showPanel("LichChieu");
    }                                         

    private void btnLichSuMouseClicked(java.awt.event.MouseEvent evt) {                                       
        showPanel("LichSu");
    }                                      

    private void pThongTinMouseClicked(java.awt.event.MouseEvent evt) {                                       
        showPanel("ThongTin");
    }                                      

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SellTicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SellTicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SellTicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SellTicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SellTicketFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel btnBanVe;
    private javax.swing.JLabel btnDangXuat;
    private javax.swing.JLabel btnLichChieu;
    private javax.swing.JLabel btnLichSu;
    private javax.swing.JLabel btnPhim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lChucVu;
    private javax.swing.JLabel lTen;
    private javax.swing.JPanel pContent;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pSellTicket;
    private javax.swing.JPanel pThongTin;
    private javax.swing.JScrollPane scrollPane1;
    // End of variables declaration                   
}
