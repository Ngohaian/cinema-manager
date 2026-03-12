package managers;

import Model.SeatLayout;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    private List<Room> danhSachPhong;

    public RoomManager() {
        danhSachPhong = new ArrayList<>();
    }

    /*
     * addRoom
     */
    public boolean addRoom(Room phongMoi) {

        for (Room phong : danhSachPhong) {

            if (phong.getRoomId() == phongMoi.getRoomId()) {
                System.out.println("Room already exists!");
                return false;
            }
        }

        danhSachPhong.add(phongMoi);
        return true;
    }

    /*
     * removeRoom
     */
    public boolean removeRoom(int idPhong) {

        for (Room phong : danhSachPhong) {

            if (phong.getRoomId() == idPhong) {

                danhSachPhong.remove(phong);
                return true;
            }
        }

        return false;
    }

    /*
     * updateRoom
     */
    public boolean updateRoom(Room phongCapNhat) {

        for (int i = 0; i < danhSachPhong.size(); i++) {

            Room phong = danhSachPhong.get(i);

            if (phong.getRoomId() == phongCapNhat.getRoomId()) {

                danhSachPhong.set(i, phongCapNhat);
                return true;
            }
        }

        return false;
    }

    /*
     * findRoomById
     */
    public Room findRoomById(int idPhong) {

        for (Room phong : danhSachPhong) {

            if (phong.getRoomId() == idPhong) {
                return phong;
            }
        }

        return null;
    }

    /*
     * getAllRooms
     */
    public List<Room> getAllRooms() {
        return danhSachPhong;
    }

    /*
     * getSeatLayoutOfRoom
     */
    public SeatLayout getSeatLayoutOfRoom(int idPhong) {

        Room phong = findRoomById(idPhong);

        if (phong != null) {
            return phong.getSeatLayout();
        }

        return null;
    }

    /*
     * displayRooms
     */
    public void displayRooms() {

        System.out.println("===== ROOM LIST =====");

        for (Room phong : danhSachPhong) {

            System.out.println(
                    "Room ID: " + phong.getRoomId()
                    + " | Room Name: " + phong.getRoomName()
                    + " | Total Seats: " + phong.getSeatLayout().getTotalSeats()
            );
        }
    }

}