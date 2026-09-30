class Seat {

    private final int row;
    private final int number;
    private boolean booked;

    public Seat(int row, int number) {
        this.row = row;
        this.number = number;
        this.booked = false;
    }

    public int getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBooked() {
        return booked;
    }

    public boolean book() {
        if (booked) {
            return false;
        }
        booked = true;
        return true;
    }

    public boolean cancel() {
        if (!booked) {
            return false;
        }
        booked = false;
        return true;
    }

}
