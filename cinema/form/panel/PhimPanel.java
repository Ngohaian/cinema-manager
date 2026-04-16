
package cinema.form.panel;
import java.awt.*;
import javax.swing.JPanel;

import javax.swing.border.LineBorder;
public class PhimPanel extends javax.swing.JPanel {
public javax.swing.JPanel createMovieCard(String title, String genre, int duration, String poster) {
    javax.swing.JPanel card = new javax.swing.JPanel();
    card.setLayout(new java.awt.BorderLayout());
    // Để chiều cao thoải mái một chút (tầm 320) để nếu tên xuống 2-3 dòng vẫn đẹp
    card.setPreferredSize(new java.awt.Dimension(185, 320)); 
    card.setBackground(java.awt.Color.WHITE);

    card.putClientProperty("Component.arc", 15);
    card.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 1, true));

    // 1. Poster
    javax.swing.JLabel lblPoster = new javax.swing.JLabel();
    lblPoster.setHorizontalAlignment(javax.swing.JLabel.CENTER);
    lblPoster.setIcon(scaleImage(poster, 185, 170)); 

    // 2. Phần Thông tin (Dùng BoxLayout chiều dọc)
    javax.swing.JPanel pnlInfo = new javax.swing.JPanel();
    pnlInfo.setLayout(new javax.swing.BoxLayout(pnlInfo, javax.swing.BoxLayout.Y_AXIS));
    pnlInfo.setBackground(java.awt.Color.WHITE);
    pnlInfo.setBorder(new javax.swing.border.EmptyBorder(8, 10, 10, 10));

    // --- CĂN GIỮA TÊN PHIM VÀ TỰ XUỐNG DÒNG ---
    // Mẹo: Dùng text-align: center trong HTML để chữ tự nhảy vào giữa
    javax.swing.JLabel lblTitle = new javax.swing.JLabel(
        "<html><body style='width: 150px; text-align: center;'><b>" + title + "</b></body></html>"
    );
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // Căn giữa component trong BoxLayout
    lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Căn giữa nội dung label

    // 3. Dòng dưới cùng (Thời lượng & Thể loại)
    javax.swing.JPanel pnlBottom = new javax.swing.JPanel(new java.awt.BorderLayout());
    pnlBottom.setBackground(java.awt.Color.WHITE);
    pnlBottom.setMaximumSize(new java.awt.Dimension(200, 20)); // Khống chế chiều cao dòng cuối
    
    javax.swing.JLabel lblTime = new javax.swing.JLabel(duration + "m");
    lblTime.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
    lblTime.setForeground(java.awt.Color.GRAY);

    javax.swing.JLabel lblGenreText = new javax.swing.JLabel(genre);
    lblGenreText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
    lblGenreText.setForeground(new java.awt.Color(59, 130, 246));

    pnlBottom.add(lblTime, java.awt.BorderLayout.WEST);
    pnlBottom.add(lblGenreText, java.awt.BorderLayout.EAST);

    // Xếp các thành phần vào pnlInfo
    pnlInfo.add(lblTitle);
    // Dùng Glue để đẩy cái dòng Bottom xuống dưới cùng của card
    pnlInfo.add(javax.swing.Box.createVerticalGlue()); 
    pnlInfo.add(javax.swing.Box.createVerticalStrut(10)); // Khoảng đệm nhỏ
    pnlInfo.add(pnlBottom);

    card.add(lblPoster, java.awt.BorderLayout.CENTER);
    card.add(pnlInfo, java.awt.BorderLayout.SOUTH);

    return card;
}
private javax.swing.ImageIcon scaleImage(String imagePath, int w, int h) {
    try {
        java.awt.Image img = new javax.swing.ImageIcon(imagePath).getImage();
        java.awt.Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
        return new javax.swing.ImageIcon(scaled);
    } catch (Exception e) {
        return null; // Trả về null nếu sai đường dẫn
    }
}
public void addExampleMovies() {
    // 4 cột, khoảng cách ngang 30, khoảng cách dọc 30
    CardPhimPanel.setLayout(new java.awt.GridLayout(0, 4, 30, 30));
    CardPhimPanel.removeAll(); 

    CardPhimPanel.add(createMovieCard("Godzilla x Kong", "Hành Động", 115, "src/cinema/images/edit(black).png"));
    CardPhimPanel.add(createMovieCard("Kung Fu Panda 4", "Hoạt Hình", 94, "src/images/p2.jpg"));
    CardPhimPanel.add(createMovieCard("Lật Mặt 7", "Tâm Lý", 112, "src/images/p3.jpg"));
    CardPhimPanel.add(createMovieCard("Mai", "Tình Cảm", 131, "src/images/p4.jpg"));
    
    // Thêm hàng 2 để test scroll
    CardPhimPanel.add(createMovieCard("Dune 2", "Khoa Học", 166, "src/images/p5.jpg"));

    CardPhimPanel.revalidate();
    CardPhimPanel.repaint();
}
public PhimPanel() {
        initComponents();
        addExampleMovies();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        PhanLoaiPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimPhim = new javax.swing.JTextField();
        jSlider1 = new javax.swing.JSlider();
        cbTrangThai = new javax.swing.JComboBox<>();
        cbTheLoai = new javax.swing.JComboBox<>();
        lMinThoiGianPhim = new javax.swing.JLabel();
        lMaxThoiGianPhim = new javax.swing.JLabel();
        CardPhimPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(248, 250, 252));

        PhanLoaiPanel.setBackground(new java.awt.Color(255, 255, 255));
        PhanLoaiPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel1.setLabelFor(txtTimPhim);
        jLabel1.setText("Tìm phim");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel2.setLabelFor(cbTrangThai);
        jLabel2.setText("Trạng thái");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel3.setLabelFor(cbTheLoai);
        jLabel3.setText("Thể loại");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jLabel4.setLabelFor(jSlider1);
        jLabel4.setText("Thời lượng tối đa");

        txtTimPhim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimPhim.setToolTipText("Nhập vào phim bạn muốn tìm kiếm");

        cbTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang chiếu", "Sắp chiếu", "Đã dừng chiếu" }));

        cbTheLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lMinThoiGianPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        lMaxThoiGianPhim.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lMaxThoiGianPhim.setForeground(new java.awt.Color(102, 102, 102));
        lMaxThoiGianPhim.setText("200");

        javax.swing.GroupLayout PhanLoaiPanelLayout = new javax.swing.GroupLayout(PhanLoaiPanel);
        PhanLoaiPanel.setLayout(PhanLoaiPanelLayout);
        PhanLoaiPanelLayout.setHorizontalGroup(
            PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                        .addGap(115, 115, 115))
                    .addComponent(txtTimPhim))
                .addGap(39, 39, 39)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addGap(90, 90, 90))
                    .addComponent(cbTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addGap(103, 103, 103))
                    .addComponent(cbTheLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addGap(56, 56, 56)
                        .addComponent(lMaxThoiGianPhim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PhanLoaiPanelLayout.setVerticalGroup(
            PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addComponent(txtTimPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(9, 9, 9)
                        .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(9, 9, 9)
                        .addComponent(cbTheLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                        .addGroup(PhanLoaiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(PhanLoaiPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lMaxThoiGianPhim)))
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        CardPhimPanel.setBackground(new java.awt.Color(248, 250, 252));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CardPhimPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(PhanLoaiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(CardPhimPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(423, Short.MAX_VALUE))
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JPanel CardPhimPanel;
    private javax.swing.JPanel PhanLoaiPanel;
    private javax.swing.JComboBox<String> cbTheLoai;
    private javax.swing.JComboBox<String> cbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lMaxThoiGianPhim;
    private javax.swing.JLabel lMinThoiGianPhim;
    private javax.swing.JTextField txtTimPhim;
    // End of variables declaration                   

    
}
