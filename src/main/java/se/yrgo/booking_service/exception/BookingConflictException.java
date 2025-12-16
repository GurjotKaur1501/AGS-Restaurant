package se.yrgo.booking_service.exception;

public class BookingConflictException extends RuntimeException {

    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(Long tableId, String reason) {
        super("Booking conflict for table " + tableId + ": " + reason);
    }
}