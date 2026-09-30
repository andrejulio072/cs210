import java.util.Scanner;

public class Cinema {
    private static final int ROWS = 5;
    private static final int SEATS_PER_ROW = 8;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Seat[][] cinema = createCinema(ROWS, SEATS_PER_ROW);
            int choice = -1;

            while (choice != 0) {
                showMenu();
                choice = readInt(input, "Enter your choice: ");

                switch (choice) {
                    case 1:
                        handleBookSeat(input, cinema);
                        break;
                    case 2:
                        handleCancelSeat(input, cinema);
                        break;
                    case 3:
                        displaySeats(cinema);
                        break;
                    case 4:
                        printBookingSummary(cinema);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }

    private static Seat[][] createCinema(int rows, int seatsPerRow) {
        Seat[][] cinema = new Seat[rows][seatsPerRow];
        for (int row = 0; row < cinema.length; row++) {
            for (int number = 0; number < cinema[row].length; number++) {
                cinema[row][number] = new Seat(row + 1, number + 1);
            }
        }
        return cinema;
    }

    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                return input.nextInt();
            }
            System.out.println("Please enter a valid number.");
            input.next();
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("=== Cinema Booking System ===");
        System.out.println("1. Book a seat");
        System.out.println("2. Cancel a booking");
        System.out.println("3. Display seating arrangement");
        System.out.println("4. Show booking summary");
        System.out.println("0. Exit");
    }

    private static void handleBookSeat(Scanner input, Seat[][] cinema) {
        int row = readInt(input, "Enter row (1-" + cinema.length + "): ");
        int number = readInt(input, "Enter seat number (1-" + cinema[0].length + "): ");

        if (isValidSeat(cinema, row, number)) {
            Seat seat = cinema[row - 1][number - 1];
            if (seat.book()) {
                System.out.println("Seat booked successfully.");
            } else {
                System.out.println("That seat is already booked.");
            }
        } else {
            System.out.println("Invalid seat position.");
        }
    }

    private static void handleCancelSeat(Scanner input, Seat[][] cinema) {
        int row = readInt(input, "Enter row (1-" + cinema.length + "): ");
        int number = readInt(input, "Enter seat number (1-" + cinema[0].length + "): ");

        if (isValidSeat(cinema, row, number)) {
            Seat seat = cinema[row - 1][number - 1];
            if (seat.cancel()) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("That seat is not currently booked.");
            }
        } else {
            System.out.println("Invalid seat position.");
        }
    }

    private static void printBookingSummary(Seat[][] cinema) {
        int bookedSeats = countBookedSeats(cinema);
        int totalSeats = cinema.length * cinema[0].length;
        int availableSeats = totalSeats - bookedSeats;

        System.out.println("Total seats: " + totalSeats);
        System.out.println("Booked seats: " + bookedSeats);
        System.out.println("Available seats: " + availableSeats);
    }

    private static boolean isValidSeat(Seat[][] cinema, int row, int number) {
        return row >= 1 && row <= cinema.length && number >= 1 && number <= cinema[0].length;
    }

    private static void displaySeats(Seat[][] cinema) {
        System.out.println("Seating arrangement (O = available, X = booked):");
        for (int row = 0; row < cinema.length; row++) {
            System.out.print("Row " + (row + 1) + ": ");
            for (int number = 0; number < cinema[row].length; number++) {
                Seat seat = cinema[row][number];
                System.out.print(isSeatBooked(seat) ? "X " : "O ");
            }
            System.out.println();
        }
    }

    private static int countBookedSeats(Seat[][] cinema) {
        int count = 0;
        for (int row = 0; row < cinema.length; row++) {
            for (int number = 0; number < cinema[row].length; number++) {
                Seat seat = cinema[row][number];
                if (isSeatBooked(seat)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isSeatBooked(Seat seat) {
        return seat != null && seat.isBooked();
    }
}
