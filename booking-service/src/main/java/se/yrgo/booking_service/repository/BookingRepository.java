package se.yrgo.booking_service.repository;

import se.yrgo.booking_service.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.yrgo.booking_service.entity.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByConfirmationCode(String confirmationCode);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByTableIdAndBookingTimeBetween(
            Long tableId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
    List<Booking> findByStatus(BookingStatus status);
}