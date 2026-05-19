package cinema;

public class test {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Đã nhận MySQL Connector!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}