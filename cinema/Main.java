package cinema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;

import cinema.DBConnection;
import cinema.form.LoginFrame; 
public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                // 2. Tạo câu lệnh truy vấn
                String sql = "SELECT EmployeeId, EmployeeName, Position, salary FROM Employee";
                Statement stmt = (Statement) conn.createStatement();
                ResultSet rs = ((java.sql.Statement) stmt).executeQuery(sql);

                // 3. Duyệt và in kết quả ra Console
                System.out.println("--------------------------------------------------");
                System.out.printf("%-10s | %-20s | %-15s | %-10s\n", "ID", "Họ Tên", "Chức Vụ", "Lương");
                System.out.println("--------------------------------------------------");
                
                while (rs.next()) {
                    System.out.printf("%-10s | %-20s | %-15s | %,.0f VNĐ\n", 
                        rs.getString("EmployeeId"),
                        rs.getString("EmployeeName"),
                        rs.getString("Position"),
                        rs.getDouble("salary")
                    );
                }
                System.out.println("--------------------------------------------------");

                // Đóng kết nối
                conn.close();
            } catch (Exception e) {
                System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
            }
        } else {
                System.out.println("Kết nối thất bại!");
        }
    }
}
