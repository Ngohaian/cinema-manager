package managers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Model.ShowTime;

public class ShowTimeManager {
	 private List<ShowTime> danhSachSuatChieu;

	    public ShowTimeManager() {
	        danhSachSuatChieu = new ArrayList<>();
	    }

	    /*
	     * addShowTime
	     */
	    public boolean addShowTime(Showtime suatChieuMoi) {

	        if (isScheduleConflict(suatChieuMoi)) {
	            System.out.println("Schedule conflict detected!");
	            return false;
	        }

	        danhSachSuatChieu.add(suatChieuMoi);
	        return true;
	    }

	    /*
	     * removeShowTime
	     */
	    public boolean removeShowTime(String idSuatChieu) {

	        for (ShowTime sc : danhSachSuatChieu) {

	            if (sc.getId().equals(idSuatChieu)) {
	                danhSachSuatChieu.remove(sc);
	                return true;
	            }
	        }

	        return false;
	    }

	    /*
	     * updateShowTime
	     */
	    public boolean updateShowTime(ShowTime suatChieuCapNhat) {

	        for (int i = 0; i < danhSachSuatChieu.size(); i++) {

	            ShowTime sc = danhSachSuatChieu.get(i);

	            if (sc.getId().equals(suatChieuCapNhat.getId())) {

	                danhSachSuatChieu.set(i, suatChieuCapNhat);
	                return true;
	            }
	        }

	        return false;
	    }

	    /*
	     * getAllShowTimes
	     */
	    public List<ShowTime> getAllShowTimes() {
	        return danhSachSuatChieu;
	    }

	    /*
	     * findShowTimeById
	     */
	    public ShowTime findShowTimeById(String idSuatChieu) {

	        for (ShowTime sc : danhSachSuatChieu) {

	            if (sc.getId().equals(idSuatChieu)) {
	                return sc;
	            }
	        }

	        return null;
	    }

	    /*
	     * getShowTimesByMovie
	     */
	    public List<ShowTime> getShowTimesByMovie(String tenPhim) {

	        List<ShowTime> ketQua = new ArrayList<>();

	        for (ShowTime sc : danhSachSuatChieu) {

	            if (sc.getMovieName().equalsIgnoreCase(tenPhim)) {
	                ketQua.add(sc);
	            }
	        }

	        return ketQua;
	    }

	    /*
	     * getShowTimesByRoom
	     */
	    public List<ShowTime> getShowTimesByRoom(int idPhong) {

	        List<ShowTime> ketQua = new ArrayList<>();

	        for (ShowTime sc : danhSachSuatChieu) {

	            if (sc.getScreenId() == idPhong) {
	                ketQua.add(sc);
	            }
	        }

	        return ketQua;
	    }

	    /*
	     * getShowTimesByDate
	     */
	    public List<ShowTime> getShowTimesByDate(LocalDate ngay) {

	        List<ShowTime> ketQua = new ArrayList<>();

	        for (ShowTime sc : danhSachSuatChieu) {

	            LocalDate ngayChieu = sc.getStartTime().toLocalDate();

	            if (ngayChieu.equals(ngay)) {
	                ketQua.add(sc);
	            }
	        }

	        return ketQua;
	    }

	    /*
	     * isScheduleConflict
	     */
	    public boolean isScheduleConflict(ShowTime suatChieuMoi) {

	        for (ShowTime sc : danhSachSuatChieu) {

	           
	            if (sc.getScreenId() == suatChieuMoi.getScreenId()) {

	                boolean conflict = sc.isConflict(
	                        suatChieuMoi.getStartTime(),
	                        suatChieuMoi.getEndTime()
	                );

	                if (conflict) {
	                    return true;
	                }
	            }
	        }

	        return false;
	    }

	    /*
	     * getNextAvailableTime
	     */
	    public LocalDateTime getNextAvailableTime(int idPhong) {

	        LocalDateTime latestTime = null;

	        for (ShowTime sc : danhSachSuatChieu) {

	            if (sc.getScreenId() == idPhong) {

	                if (latestTime == null ||
	                        sc.getEndTime().isAfter(latestTime)) {

	                    latestTime = sc.getEndTime();
	                }
	            }
	        }

	        return latestTime;
	    }

	    /*
	     * displaySchedule
	     */
	    public void displaySchedule() {

	        System.out.println("===== CINEMA SCHEDULE =====");

	        for (ShowTime sc : danhSachSuatChieu) {

	            System.out.println(
	                    "Movie: " + sc.getMovieName()
	                    + " | Room: " + sc.getScreenId()
	                    + " | Start: " + sc.getStartTime()
	                    + " | End: " + sc.getEndTime()
	            );
	        }
	    }
	

}
