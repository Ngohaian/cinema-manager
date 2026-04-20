package cinema.enums;

public enum MovieStatus {
    INACTIVE(1),
    ACTIVE(2),
    COMING_SOON(3);

    private final int value;
    MovieStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static MovieStatus fromInt(int value){
        for(MovieStatus status: MovieStatus.values()){
            if(status.getValue() == value){
                return status;
            }
        }
        return null;
    }
    public static String getNameMovieStatus(MovieStatus value) {
        switch(value) {
            case INACTIVE: 
                return "Dừng chiếu";
            case ACTIVE:
                return "Đang chiếu";
            case COMING_SOON:
                return "Sắp ra mắt";
            default:
                return null;
        }
    }
}
