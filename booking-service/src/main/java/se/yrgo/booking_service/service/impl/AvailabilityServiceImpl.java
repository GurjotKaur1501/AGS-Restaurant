package se.yrgo.booking_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.yrgo.booking_service.entity.Booking;
import se.yrgo.booking_service.entity.BookingStatus;
import se.yrgo.booking_service.repository.BookingRepository;
import se.yrgo.booking_service.service.AvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

    private final BookingRepository bookingRepository;

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime bookingTime) {
        log.info("Checking availability for table {} at {}", tableId, bookingTime);

        // Check if there's any booking for this table within 2 hours of the requested time
        LocalDateTime startWindow = bookingTime.minusHours(2);
        LocalDateTime endWindow = bookingTime.plusHours(2);

        List<Booking> conflictingBookings = bookingRepository.findByTableIdAndBookingTimeBetween(
                tableId,
                startWindow,
                endWindow
        );

        // Filter out cancelled bookings
        boolean hasConflict = conflictingBookings.stream()
                .anyMatch(booking -> booking.getStatus() != BookingStatus.CANCELLED);

        if (hasConflict) {
            log.warn("Table {} is NOT available at {} - conflict found", tableId, bookingTime);
            return false;
        }

        log.info("Table {} is available at {}", tableId, bookingTime);
        return true;
    }

    @Override
    public void releaseTable(Long tableId, LocalDateTime bookingTime) {
        log.info("Releasing table {} for time {}", tableId, bookingTime);
        // In a real system, this would update a reservation system
        // For now, we just log it since cancellation updates the booking status
    }

    @Override
    public boolean checkAndReserveTable(Long tableId, LocalDateTime bookingTime) {
        log.info("Attempting to reserve table {} for {}", tableId, bookingTime);

        if (isTableAvailable(tableId, bookingTime)) {
            log.info("✅ Table {} reserved successfully for {}", tableId, bookingTime);
            return true;
        }

        log.warn("❌ Failed to reserve table {} for {}", tableId, bookingTime);
        return false;
    }
}