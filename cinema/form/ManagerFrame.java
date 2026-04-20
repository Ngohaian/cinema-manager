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
public class ManagerFrame extends javax.swing.JFrame {
    private JPanel indicator = new JPanel(); // Tao thanh danh dau cho chuc nang dang duoc chon
    private JLabel selectedButton = null;
    private JLabel[] menuLabels_1;
    private NhanVienManagerPanel thongKePanel = new NhanVienManagerPanel();
    private BanVePanel nhanVienMP = new BanVePanel();
    private KhachHangManagerPanel khachHangMP = new KhachHangManagerPanel();
    private SuatChieuManagerPanel suatChieuMP = new SuatChieuManagerPanel();
    private PhimManagerPanel phimMP = new PhimManagerPanel();
    private PhongManagerPanel phongMP = new PhongManagerPanel();
    private HoaDonManagerPanel hoaDonMP = new HoaDonManagerPanel();
    private ThongTinPanel thongTinPanel = new ThongTinPanel();

    public ManagerFrame() {
        initComponents();
        menuLabels_1 = new JLabel[]{btnThongKe, btnNhanVien, btnKhachHang, btnSuatChieu, btnPhimMP, btnPhong, btnHoaDon};
        for(int i=0;i<menuLabels_1.length;i++){
            final int index = i;
            setHoverChucNang(menuLabels_1[i]);
            menuLabels_1[i].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    setSelectedButton(menuLabels_1[index]);
                }
            });
        }
        setHoverLogOut(btnDangXuat);
        indicator.setBackground(new Color(0,74,130));
        indicator.setSize(10,btnThongKe.getHeight());
        indicator.setVisible(false);
        pMenu.add(indicator);
        
        // chuyen doi giua cac trang
        CardLayout card = new CardLayout();
        pContent.setLayout(card);
        pContent.add(wrap(thongKePanel), "ThongKe");
        pContent.add(wrap(nhanVienMP), "NhanVien");
        pContent.add(wrap(khachHangMP),"KhachHang");
        pContent.add(wrap(phimMP),"Phim");
        pContent.add(wrap(phongMP),"Phong");
        pContent.add(wrap(suatChieuMP),"SuatChieu");
        pContent.add(wrap(hoaDonMP),"HoaDon");
        pContent.add(wrap(thongTinPanel), "ThongTin");
        pContent.revalidate();
        pContent.repaint();
    }
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
        JScroll.getVerticalScrollBar().setUnitIncrement(20);
        JScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI(){

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

        JScroll.setBorder(null);
        JScroll.getViewport().setBackground(java.awt.Color.WHITE);
    }
    private JScrollPane wrap(JPanel panel){
        JScrollPane sp = new JScrollPane(panel);
        customizeScrollBar(sp);
        return sp;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pSellTicket = new javax.swing.JPanel();
        pContent = new javax.swing.JPanel();
        pMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnNhanVien = new javax.swing.JLabel();
        btnKhachHang = new javax.swing.JLabel();
        btnThongKe = new javax.swing.JLabel();
        btnDangXuat = new javax.swing.JLabel();
        pThongTin = new javax.swing.JPanel();
        lTen = new javax.swing.JLabel();
        lChucVu = new javax.swing.JLabel();
        btnPhimMP = new javax.swing.JLabel();
        btnSuatChieu = new javax.swing.JLabel();
        btnPhong = new javax.swing.JLabel();
        btnHoaDon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("frameSellTicket"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pSellTicket.setBackground(new java.awt.Color(255, 255, 255));

        pContent.setBackground(new java.awt.Color(255, 255, 255));
        pContent.setToolTipText("");

        javax.swing.GroupLayout pContentLayout = new javax.swing.GroupLayout(pContent);
        pContent.setLayout(pContentLayout);
        pContentLayout.setHorizontalGroup(
            pContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
        );
        pContentLayout.setVerticalGroup(
            pContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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

        btnNhanVien.setBackground(new java.awt.Color(0, 146, 255));
        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnNhanVien.setForeground(new java.awt.Color(255, 255, 255));
        btnNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/employee.png"))); // NOI18N
        btnNhanVien.setLabelFor(btnThongKe);
        btnNhanVien.setText(" Nhân viên");
        btnNhanVien.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnNhanVien.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNhanVien.setOpaque(true);
        btnNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNhanVienMouseClicked(evt);
            }
        });

        btnKhachHang.setBackground(new java.awt.Color(0, 146, 255));
        btnKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnKhachHang.setForeground(new java.awt.Color(255, 255, 255));
        btnKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/customer.png"))); // NOI18N
        btnKhachHang.setText(" Khách hàng");
        btnKhachHang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnKhachHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKhachHang.setOpaque(true);
        btnKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnKhachHangMouseClicked(evt);
            }
        });

        btnThongKe.setBackground(new java.awt.Color(0, 146, 255));
        btnThongKe.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        btnThongKe.setForeground(new java.awt.Color(255, 255, 255));
        btnThongKe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/bar-chart.png"))); // NOI18N
        btnThongKe.setText(" Thống kê");
        btnThongKe.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnThongKe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThongKe.setOpaque(true);
        btnThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThongKeMouseClicked(evt);
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
        lTen.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lTen.setText("Nguyễn Văn A");

        lChucVu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lChucVu.setForeground(new java.awt.Color(204, 204, 204));
        lChucVu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lChucVu.setText("Quản lý");

        javax.swing.GroupLayout pThongTinLayout = new javax.swing.GroupLayout(pThongTin);
        pThongTin.setLayout(pThongTinLayout);
        pThongTinLayout.setHorizontalGroup(
            pThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pThongTinLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lTen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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

        btnPhimMP.setBackground(new java.awt.Color(0, 146, 255));
        btnPhimMP.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnPhimMP.setForeground(new java.awt.Color(255, 255, 255));
        btnPhimMP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPhimMP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/video.png"))); // NOI18N
        btnPhimMP.setText(" Phim");
        btnPhimMP.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnPhimMP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPhimMP.setOpaque(true);
        btnPhimMP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPhimMPMouseClicked(evt);
            }
        });

        btnSuatChieu.setBackground(new java.awt.Color(0, 146, 255));
        btnSuatChieu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSuatChieu.setForeground(new java.awt.Color(255, 255, 255));
        btnSuatChieu.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSuatChieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/calendar.png"))); // NOI18N
        btnSuatChieu.setText(" Suất chiếu");
        btnSuatChieu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnSuatChieu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSuatChieu.setOpaque(true);
        btnSuatChieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuatChieuMouseClicked(evt);
            }
        });

        btnPhong.setBackground(new java.awt.Color(0, 146, 255));
        btnPhong.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnPhong.setForeground(new java.awt.Color(255, 255, 255));
        btnPhong.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/seats.png"))); // NOI18N
        btnPhong.setText(" Phòng");
        btnPhong.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPhong.setOpaque(true);
        btnPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPhongMouseClicked(evt);
            }
        });

        btnHoaDon.setBackground(new java.awt.Color(0, 146, 255));
        btnHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cinema/images/bill.png"))); // NOI18N
        btnHoaDon.setText("Hóa đơn");
        btnHoaDon.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 1));
        btnHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHoaDon.setOpaque(true);
        btnHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHoaDonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pMenuLayout = new javax.swing.GroupLayout(pMenu);
        pMenu.setLayout(pMenuLayout);
        pMenuLayout.setHorizontalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(btnDangXuat)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pMenuLayout.createSequentialGroup()
                .addComponent(pThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pMenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(29, 29, 29))
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuatChieu, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPhimMP, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPhong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pMenuLayout.setVerticalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(33, 33, 33)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPhimMP, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuatChieu, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(pThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDangXuat)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout pSellTicketLayout = new javax.swing.GroupLayout(pSellTicket);
        pSellTicket.setLayout(pSellTicketLayout);
        pSellTicketLayout.setHorizontalGroup(
            pSellTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pSellTicketLayout.createSequentialGroup()
                .addComponent(pMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pSellTicketLayout.setVerticalGroup(
            pSellTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pSellTicketLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pSellTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 744, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pSellTicketLayout.createSequentialGroup()
                        .addComponent(pContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)))
                .addGap(41, 41, 41))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pSellTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pSellTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(1018, 754));
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void formWindowOpened(java.awt.event.WindowEvent evt) {                                  
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        showPanel("ThongKe");
        setSelectedButton(btnThongKe);
    }                                 

    private void btnDangXuatMouseClicked(java.awt.event.MouseEvent evt) {                                         
        this.dispose();
        new LoginFrame().setVisible(true);
    }                                        

    private void btnThongKeMouseClicked(java.awt.event.MouseEvent evt) {                                        
        showPanel("ThongKe");
    }                                       

    private void btnNhanVienMouseClicked(java.awt.event.MouseEvent evt) {                                         
        showPanel("NhanVien");
    }                                        

    private void btnKhachHangMouseClicked(java.awt.event.MouseEvent evt) {                                          
        showPanel("KhachHang");
    }                                         

    private void btnSuatChieuMouseClicked(java.awt.event.MouseEvent evt) {                                          
        showPanel("SuatChieu");
    }                                         

    private void pThongTinMouseClicked(java.awt.event.MouseEvent evt) {                                       
        showPanel("ThongTin");
    }                                      

    private void btnPhimMPMouseClicked(java.awt.event.MouseEvent evt) {                                       
        showPanel("Phim");
    }                                      

    private void btnPhongMouseClicked(java.awt.event.MouseEvent evt) {                                      
        showPanel("Phong");
    }                                     

    private void btnHoaDonMouseClicked(java.awt.event.MouseEvent evt) {                                       
        showPanel("HoaDon");
    }                                      

    public static void main(String args[]) {
        try {
            com.formdev.flatlaf.FlatLightLaf.setup(); 
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel btnDangXuat;
    private javax.swing.JLabel btnHoaDon;
    private javax.swing.JLabel btnKhachHang;
    private javax.swing.JLabel btnNhanVien;
    private javax.swing.JLabel btnPhimMP;
    private javax.swing.JLabel btnPhong;
    private javax.swing.JLabel btnSuatChieu;
    private javax.swing.JLabel btnThongKe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lChucVu;
    private javax.swing.JLabel lTen;
    private javax.swing.JPanel pContent;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pSellTicket;
    private javax.swing.JPanel pThongTin;
    // End of variables declaration                   
}
