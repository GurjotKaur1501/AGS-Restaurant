package se.yrgo.booking_service.exception;

import java.time.LocalDateTime;

public class TableNotAvailableException extends RuntimeException {

    public TableNotAvailableException(String message) {
        super(message);
    }

    public TableNotAvailableException(Long tableId, LocalDateTime bookingTime) {
        super("Table " + tableId + " is not available at " + bookingTime);
    }
}