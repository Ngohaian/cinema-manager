package cinema.enums;

enum MovieStatus {
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
}
