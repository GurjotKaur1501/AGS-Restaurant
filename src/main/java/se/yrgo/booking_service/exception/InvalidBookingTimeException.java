package se.yrgo.booking_service.exception;

import java.time.LocalDateTime;

public class InvalidBookingTimeException extends RuntimeException {

    public InvalidBookingTimeException(String message) {
        super(message);
    }

    public InvalidBookingTimeException(LocalDateTime bookingTime) {
        super("Invalid booking time: " + bookingTime + ". Booking time must be in the future.");
    }
}