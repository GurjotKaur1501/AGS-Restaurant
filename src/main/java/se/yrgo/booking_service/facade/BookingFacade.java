package se.yrgo.booking_service.facade;

import se.yrgo.booking_service.dto.BookingDTO;
import se.yrgo.booking_service.dto.response.BookingResponseDTO;
import se.yrgo.booking_service.dto.request.BookingRequestDTO;
import se.yrgo.booking_service.exception.BookingConflictException;
import se.yrgo.booking_service.exception.InvalidBookingTimeException;
import se.yrgo.booking_service.exception.TableNotAvailableException;
import se.yrgo.booking_service.service.AvailabilityService;
import se.yrgo.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingFacade {
    private final BookingService bookingService;
    private final AvailabilityService availabilityService;

    /**
     * FACADE METHOD: One call handles the complete booking flow
     * Hides: validation, availability check, creation, confirmation
     */
    public BookingResponseDTO createCompleteBooking(BookingRequestDTO request) {
        log.info("🎭 FACADE: Starting complete booking process for user: {}", request.getUserId());

        // 1. Validate input (simplified)
        validateBookingRequest(request);

        // 2. Check availability (hidden complexity)
        log.info("🎭 Checking table availability...");
        boolean available = availabilityService.isTableAvailable(
                request.getTableId(),
                request.getBookingTime()
        );

        if (!available) {
            throw new TableNotAvailableException(request.getTableId(), request.getBookingTime());
        }

        // 3. Reserve table
        boolean reserved = availabilityService.checkAndReserveTable(
                request.getTableId(),
                request.getBookingTime()
        );

        if (!reserved) {
            throw new BookingConflictException(request.getTableId(), "Failed to reserve table");
        }

        // 4. Create booking (hidden complexity)
        log.info("🎭 Creating booking...");
        BookingDTO booking = bookingService.createBooking(request);

        // 5. Send notifications (simulated)
        sendNotifications(booking);

        // 6. Return unified response
        log.info("🎭 Booking completed successfully!");
        return BookingResponseDTO.builder()  // 🏗️ Builder Pattern!
                .booking(booking)
                .message("✅ Booking completed successfully!")
                .confirmationCode(booking.getConfirmationCode())
                .nextSteps(List.of(
                        "Check your email for confirmation",
                        "Arrive 10 minutes early",
                        "Bring your confirmation code: " + booking.getConfirmationCode(),
                        "Contact us for special requests"
                ))
                .build();
    }

    /**
     * FACADE METHOD: Simplified cancellation
     */
    public BookingDTO cancelBooking(Long bookingId) {
        log.info("🎭 FACADE: Processing cancellation for booking: {}", bookingId);

        // 1. Get booking details
        BookingDTO booking = bookingService.getBookingById(bookingId);

        // 2. Cancel booking
        BookingDTO cancelled = bookingService.cancelBooking(bookingId);

        // 3. Update availability (hidden)
        availabilityService.releaseTable(
                cancelled.getTableId(),
                cancelled.getBookingTime()
        );

        // 4. Send cancellation notification (simulated)
        log.info("📧 Sending cancellation notification for booking {}", bookingId);

        return cancelled;
    }

    /**
     * FACADE METHOD: Get booking with enhanced details
     */
    public BookingResponseDTO getBookingDetails(Long bookingId) {
        log.info("🎭 FACADE: Getting enhanced booking details for: {}", bookingId);

        // 1. Get basic booking
        BookingDTO booking = bookingService.getBookingById(bookingId);

        // 2. Add external details (simulated)
        String restaurantName = getRestaurantName(booking.getTableId());
        String userName = getUserName(booking.getUserId());

        return BookingResponseDTO.builder()
                .booking(booking)
                .message("Booking details retrieved successfully")
                .confirmationCode(booking.getConfirmationCode())
                .additionalInfo(List.of(
                        "Restaurant: " + restaurantName,
                        "Customer: " + userName,
                        "Table: #" + booking.getTableId(),
                        "Status: " + booking.getStatus()
                ))
                .build();
    }

    /**
     * FACADE METHOD: Update booking with validation
     */
    public BookingDTO updateBookingWithValidation(Long bookingId, BookingRequestDTO request) {
        log.info("🎭 FACADE: Updating booking {} with validation", bookingId);

        // 1. Check if booking exists
        BookingDTO existing = bookingService.getBookingById(bookingId);

        // 2. Check new time availability
        if (!existing.getBookingTime().equals(request.getBookingTime())) {
            boolean available = availabilityService.isTableAvailable(
                    request.getTableId(),
                    request.getBookingTime()
            );

            if (!available) {
                throw new TableNotAvailableException(request.getTableId(), request.getBookingTime());
            }

            // Release old time slot
            availabilityService.releaseTable(existing.getTableId(), existing.getBookingTime());

            // Reserve new time slot
            availabilityService.checkAndReserveTable(request.getTableId(), request.getBookingTime());
        }

        // 3. Update booking
        return bookingService.updateBooking(bookingId, request);
    }

    // Private helper methods (hidden complexity)
    private void validateBookingRequest(BookingRequestDTO request) {
        if (request.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be positive");
        }
        if (request.getBookingTime().isBefore(java.time.LocalDateTime.now())) {
            throw new InvalidBookingTimeException(request.getBookingTime());
        }
        log.info("✅ Booking request validated successfully");
    }

    private void sendNotifications(BookingDTO booking) {
        // Simulate sending notifications
        log.info("📧 Sending confirmation email for booking {}", booking.getConfirmationCode());
        log.info("📱 Sending SMS notification for booking {}", booking.getConfirmationCode());
    }

    private String getRestaurantName(Long tableId) {
        // Simulate API call to restaurant service
        return "Fine Dining Restaurant #" + (tableId % 10 + 1);
    }

    private String getUserName(Long userId) {
        // Simulate API call to user service
        return "Customer #" + userId;
    }
}