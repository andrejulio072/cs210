import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BookingSystemTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        runTest("Seat starts unbooked", BookingSystemTests::testSeatStartsUnbooked);
        runTest("Seat can be booked", BookingSystemTests::testSeatCanBeBooked);
        runTest("Seat cannot be double-booked", BookingSystemTests::testSeatCannotBeDoubleBooked);
        runTest("Seat can be cancelled", BookingSystemTests::testSeatCanBeCancelled);
        runTest("Unbooked seat cannot be cancelled", BookingSystemTests::testUnbookedSeatCannotBeCancelled);
        runTest("Cinema shows menu", BookingSystemTests::testCinemaShowsMenu);
        runTest("Cinema books valid seat", BookingSystemTests::testCinemaBooksValidSeat);
        runTest("Cinema prevents double booking", BookingSystemTests::testCinemaPreventsDoubleBooking);
        runTest("Cinema cancels booking", BookingSystemTests::testCinemaCancelsBooking);
        runTest("Cinema rejects invalid seat", BookingSystemTests::testCinemaRejectsInvalidSeat);
        runTest("Cinema handles non-numeric input", BookingSystemTests::testCinemaHandlesNonNumericInput);
        runTest("Cinema summary updates", BookingSystemTests::testCinemaSummaryUpdates);

        System.out.println();
        System.out.println("Tests passed: " + passed);
        System.out.println("Tests failed: " + failed);
        System.out.println("Total tests: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + name);
        } catch (AssertionError ex) {
            failed++;
            System.out.println("FAIL: " + name + " -> " + ex.getMessage());
        } catch (Exception ex) {
            failed++;
            System.out.println("FAIL: " + name + " -> Unexpected error: " + ex.getMessage());
        }
    }

    private static void testSeatStartsUnbooked() {
        Seat seat = new Seat(1, 1);
        assertTrue(!seat.isBooked(), "Seat should start unbooked");
    }

    private static void testSeatCanBeBooked() {
        Seat seat = new Seat(1, 1);
        assertTrue(seat.book(), "Booking should return true on first booking");
        assertTrue(seat.isBooked(), "Seat should be booked");
    }

    private static void testSeatCannotBeDoubleBooked() {
        Seat seat = new Seat(1, 1);
        seat.book();
        assertTrue(!seat.book(), "Second booking should return false");
    }

    private static void testSeatCanBeCancelled() {
        Seat seat = new Seat(1, 1);
        seat.book();
        assertTrue(seat.cancel(), "Cancel should return true for booked seat");
        assertTrue(!seat.isBooked(), "Seat should be unbooked after cancel");
    }

    private static void testUnbookedSeatCannotBeCancelled() {
        Seat seat = new Seat(1, 1);
        assertTrue(!seat.cancel(), "Cancel should return false for unbooked seat");
    }

    private static void testCinemaShowsMenu() {
        String out = runCinemaSession("0\n");
        assertContains(out, "=== Cinema Booking System ===");
        assertContains(out, "1. Book a seat");
        assertContains(out, "0. Exit");
    }

    private static void testCinemaBooksValidSeat() {
        String out = runCinemaSession("1\n1\n1\n0\n");
        assertContains(out, "Seat booked successfully.");
    }

    private static void testCinemaPreventsDoubleBooking() {
        String out = runCinemaSession("1\n1\n1\n1\n1\n1\n0\n");
        assertContains(out, "Seat booked successfully.");
        assertContains(out, "That seat is already booked.");
    }

    private static void testCinemaCancelsBooking() {
        String out = runCinemaSession("1\n1\n1\n2\n1\n1\n0\n");
        assertContains(out, "Booking cancelled successfully.");
    }

    private static void testCinemaRejectsInvalidSeat() {
        String out = runCinemaSession("1\n9\n1\n0\n");
        assertContains(out, "Invalid seat position.");
    }

    private static void testCinemaHandlesNonNumericInput() {
        String out = runCinemaSession("abc\n0\n");
        assertContains(out, "Please enter a valid number.");
    }

    private static void testCinemaSummaryUpdates() {
        String out = runCinemaSession("1\n1\n1\n4\n0\n");
        assertContains(out, "Booked seats: 1");
        assertContains(out, "Available seats: 39");
    }

    private static String runCinemaSession(String inputScript) {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(inputScript.getBytes(StandardCharsets.UTF_8));
        try {
            System.setIn(input);
            System.setOut(new PrintStream(outputCapture));
            Cinema.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return outputCapture.toString(StandardCharsets.UTF_8);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertContains(String output, String expectedSubstring) {
        if (!output.contains(expectedSubstring)) {
            throw new AssertionError("Expected output to contain: " + expectedSubstring);
        }
    }
}
