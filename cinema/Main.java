package cinema;
public class Main {
    public static void main(String[] args) {
        new cinema.dao.EmployeeDAO().migratePasswordsToBcrypt();        // dòng này lúc chạy test thì chạy trước 1 lần rồi xóa đi nhé
        CinemaManager cinemaManager = new CinemaManager();
        cinemaManager.login();
    }
}
