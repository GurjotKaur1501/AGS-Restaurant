package se.yrgo.booking_service.service;

public interface AvailabilityService {
    boolean isTableAvailable(Long tableId, java.time.LocalDateTime bookingTime);
    void releaseTable(Long tableId, java.time.LocalDateTime bookingTime);
    boolean checkAndReserveTable(Long tableId, java.time.LocalDateTime bookingTime);
}
