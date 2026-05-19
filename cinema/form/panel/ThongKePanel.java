package cinema.form.panel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import cinema.dao.EmployeeDAO;
import cinema.dao.InvoiceDAO;
import cinema.dao.MovieDAO;

public class ThongKePanel extends javax.swing.JPanel {
    private DefaultCategoryDataset dataDoanhThu, dataNhanVien;
    private DefaultCategoryDataset dataNgay, dataThang, dataNam;
    private org.jfree.chart.plot.CategoryPlot globalPlot;
    private DefaultPieDataset dataSuatChieu;
    private MovieDAO moviedao = new MovieDAO();
    private InvoiceDAO invoicedao = new InvoiceDAO();
    private EmployeeDAO employeedao = new EmployeeDAO();
    public ThongKePanel() {
        initComponents();
        loadData();
        setupCustomUI();
    }
    private void loadData() {
        dataDoanhThu = new DefaultCategoryDataset();
        dataDoanhThu.addValue(120, "VND", "T2");
        dataDoanhThu.addValue(150, "VND", "T3");
        dataDoanhThu.addValue(200, "VND", "T4");

        dataNhanVien = new DefaultCategoryDataset();
        List<Object[]> topNhanVien = employeedao.getTop5NhanVienBanVeThang();
        for(int i=0;i<topNhanVien.size();i++){
            String id = topNhanVien.get(i)[0].toString();
            long doanhThu = topNhanVien.get(i)[2] != null ? Long.parseLong(topNhanVien.get(i)[2].toString()) : 0;
            dataNhanVien.addValue(doanhThu, "Doanh thu", id);
        }

        dataSuatChieu = new DefaultPieDataset();
        dataSuatChieu.setValue("08:00 - 11:59", 5);    
        dataSuatChieu.setValue("12:00 - 15:59", 10);    
        dataSuatChieu.setValue("16:00 - 18:00", 25);    
        dataSuatChieu.setValue("19:00 - 22:59", 45);          
        dataSuatChieu.setValue("23:00 - 07:59", 15);
        
        // ---- Doanh thu theo ngày (10 ngày gần nhất) ----
        dataNgay = new DefaultCategoryDataset();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 9; i >= 0; i--) {
            java.time.LocalDate date = today.minusDays(i);
            String label = date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
            String dateStr = date.toString(); // yyyy-MM-dd
            double revenue = invoicedao.getRevenueByDay(dateStr);
            dataNgay.addValue(revenue, "Doanh thu", label);
    }

        // ---- Doanh thu theo tháng (12 tháng năm nay) ----
        dataThang = new DefaultCategoryDataset();
        int currentYear = today.getYear();
        for (int m = 1; m <= 12; m++) {
            double revenue = invoicedao.getRevenueByMonth(m, currentYear);
            dataThang.addValue(revenue, "Doanh thu", "T " + m);
        }

        // ---- Doanh thu theo năm (4 năm gần nhất) ----
        dataNam = new DefaultCategoryDataset();
        for (int y = currentYear - 3; y <= currentYear; y++) {
            double revenue = invoicedao.getRevenueByYear(y);
            dataNam.addValue(revenue, "Doanh thu", String.valueOf(y));
        }
    }
    private void setupCustomUI() {
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(Box.createVerticalStrut(20));
        JPanel kpiRow = createKpiRow();
        kpiRow.setMaximumSize(new Dimension(2000, 120));
        contentPanel.add(kpiRow);
        
        contentPanel.add(Box.createVerticalStrut(20)); 
        
        JPanel middleRow = new JPanel(new BorderLayout(20, 0));
        middleRow.setOpaque(false);
        middleRow.setMaximumSize(new Dimension(2000, 400));
        middleRow.add(createLineChartRow(), BorderLayout.CENTER); 
        middleRow.add(createTopFilmsPanel(), BorderLayout.EAST);
        contentPanel.add(middleRow);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        JPanel chartsRow = createChartsRow();
        chartsRow.setMaximumSize(new Dimension(2000, 360)); 
        contentPanel.add(chartsRow);
        
        contentPanel.add(Box.createVerticalGlue());

        contentPanel.setPreferredSize(new Dimension(contentPanel.getPreferredSize().width, 1050));
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 1100));

        this.revalidate();
        this.repaint();
    }

    private JPanel createKpiRow() {
        double todayRevenue = invoicedao.getTodayRevenue();
        int todayTickets    = invoicedao.getTodayTicketSold();

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###đ");

        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setOpaque(false);
        row.add(createCard("Doanh thu vé (Hôm nay)", df.format(todayRevenue), Color.black));
        row.add(createCard("Số vé bán (Hôm nay)", todayTickets + " vé", Color.black));
        return row;
    }

    private JPanel createChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        
        org.jfree.chart.JFreeChart barChart = ChartFactory.createBarChart("Hiệu suất nhân viên của tháng này", null, "Doanh thu", dataNhanVien);
        barChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        barChart.getTitle().setPadding(10, 0, 20, 0);
        barChart.setBackgroundPaint(Color.WHITE); 
        
        org.jfree.chart.plot.CategoryPlot plot = barChart.getCategoryPlot();
        org.jfree.chart.renderer.category.BarRenderer renderer = (org.jfree.chart.renderer.category.BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0,146,255));
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        
        org.jfree.chart.plot.CategoryPlot barPlot = barChart.getCategoryPlot();
        barPlot.setBackgroundPaint(Color.WHITE); 
        barPlot.setRangeGridlinePaint(new Color(230, 230, 230));
        barPlot.setOutlineVisible(false);

        ChartPanel barPanel = new ChartPanel(barChart);
        barPanel.setPreferredSize(new Dimension(300, 300)); 
        barPanel.setBackground(Color.WHITE);
        barPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        org.jfree.chart.JFreeChart pieChart = ChartFactory.createPieChart(
            "Cơ cấu lượng khách theo khung giờ (%)", dataSuatChieu, true, true, false);

        pieChart.setBackgroundPaint(Color.WHITE);
        pieChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 18));
        pieChart.getTitle().setPadding(10, 0, 20, 0);
        org.jfree.chart.plot.PiePlot piePlot = (org.jfree.chart.plot.PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Color.WHITE);
        piePlot.setOutlineVisible(false);
        piePlot.setShadowPaint(null);

        piePlot.setSectionPaint("08:00 - 11:59", new Color(255, 250, 72));   
        piePlot.setSectionPaint("12:00 - 15:59", new Color(147, 246, 112));
        piePlot.setSectionPaint("16:00 - 18:00", new Color(112, 125, 246));
        piePlot.setSectionPaint("19:00 - 22:59", new Color(201, 112, 246)); 
        piePlot.setSectionPaint("23:00 - 07:59", new Color(246, 115, 112));
        
        ChartPanel piePanel = new ChartPanel(pieChart);
        piePanel.setPreferredSize(new Dimension(300, 300));
        piePanel.setBackground(Color.WHITE);
        piePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        row.add(barPanel);
        row.add(piePanel);

        return row;
    }

    private JPanel createCard(String title, String val, Color c) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new javax.swing.border.EmptyBorder(15, 15, 15, 15)));
        
        JLabel t = new JLabel(title); 
        t.setForeground(Color.GRAY);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel v = new JLabel(val); v.setFont(new Font("Segoe UI", Font.BOLD, 20)); v.setForeground(c);
        
        card.add(t); card.add(v);
        return card;
    }
    private JPanel createLineChartRow() {
    org.jfree.chart.JFreeChart lineChart = org.jfree.chart.ChartFactory.createLineChart(
            "Doanh thu theo thời gian", null, null, dataNgay, 
            org.jfree.chart.plot.PlotOrientation.VERTICAL, false, true, false);

        lineChart.setBackgroundPaint(Color.WHITE);
        lineChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 20));
        globalPlot = lineChart.getCategoryPlot();
        globalPlot.setBackgroundPaint(Color.WHITE);
        globalPlot.setRangeGridlinePaint(new Color(230, 230, 230));
        globalPlot.setOutlineVisible(false);

        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = new org.jfree.chart.renderer.category.LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(13, 110, 253));
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));  
        renderer.setSeriesShapesVisible(0, true);            
        globalPlot.setRenderer(renderer);

        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(0, 300));
        chartPanel.setBackground(Color.WHITE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        String[] labels = {"Ngày", "Tháng", "Năm"};
        for (String label : labels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> {
                if (label.equals("Ngày")) globalPlot.setDataset(dataNgay);
                if (label.equals("Tháng")) globalPlot.setDataset(dataThang);
                if (label.equals("Năm")) globalPlot.setDataset(dataNam);
        });
        btnPanel.add(btn);
    }

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(235, 235, 235)),
            new javax.swing.border.EmptyBorder(10, 10, 10, 10)));

        p.add(btnPanel, BorderLayout.NORTH);
        p.add(chartPanel, BorderLayout.CENTER);

        p.setMaximumSize(new Dimension(2000, 350)); 

        return p;
    }
    private JPanel createTopFilmsPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(235, 235, 235)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("Top 5 phim có doanh thu cao nhất");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        Object[][] dsTopPhim = moviedao.getTop5Movie(); 

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###đ");
        for (int i = 0; i < dsTopPhim.length; i++) {
            String ten   = (String) dsTopPhim[i][0];
            long tien  = (long) dsTopPhim[i][1];
            int ve       = (int) dsTopPhim[i][2];
            if(i==0){
                listPanel.add(createFilmItem(ten, tien, ve, new Color(13, 110, 253)));
            }else if(i==1){
                listPanel.add(createFilmItem(ten, tien, ve, new Color(16, 185, 129)));
            }else if(i==2){
                listPanel.add(createFilmItem(ten, tien, ve, new Color(245, 158, 11)));
            }else if(i==3){
                listPanel.add(createFilmItem(ten, tien, ve, new Color(239, 68, 68)));
            }else if(i==4){
                listPanel.add(createFilmItem(ten, tien, ve, Color.YELLOW));
            }

            listPanel.add(Box.createVerticalStrut(15));
            
        }
        card.add(listPanel, BorderLayout.CENTER);
    
        card.setPreferredSize(new Dimension(400, 400));
        return card;
    }
 
    private JPanel createFilmItem(String name, long revenue, int tickets, Color color) {
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setOpaque(false);

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        int progress = (int) (revenue / 50000); 
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(progress);
        bar.setForeground(color);
        bar.setBackground(new Color(240, 240, 240));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 12));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###đ");
        JLabel lblStats = new JLabel(df.format(revenue) + "  •  " + tickets + " vé");
        lblStats.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStats.setForeground(Color.GRAY);
        lblStats.setAlignmentX(Component.LEFT_ALIGNMENT);

        item.add(lblName);
        item.add(Box.createVerticalStrut(5));
        item.add(bar);
        item.add(Box.createVerticalStrut(5));
        item.add(lblStats);

        return item;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        contentPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(248, 250, 252));
        setPreferredSize(new java.awt.Dimension(759, 779));

        contentPanel.setBackground(new java.awt.Color(248, 250, 252));

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 767, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 652, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("THỐNG KÊ DOANH THU");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );
    }// </editor-fold>                        


    // Variables declaration - do not modify                     
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   
}
