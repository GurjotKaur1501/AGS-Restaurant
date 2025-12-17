package se.yrgo.booking_service.controller;

import se.yrgo.booking_service.dto.BookingDTO;
import se.yrgo.booking_service.dto.response.BookingResponseDTO;
import se.yrgo.booking_service.dto.request.BookingRequestDTO;
import se.yrgo.booking_service.facade.BookingFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.yrgo.booking_service.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow all origins for development
public class BookingController {

    private final BookingFacade bookingFacade;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO request) {
        BookingResponseDTO response = bookingFacade.createCompleteBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //    get all bookings
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        BookingDTO booking = bookingFacade.getBookingDetails(id).getBooking();
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingDetails(@PathVariable Long id) {
        BookingResponseDTO details = bookingFacade.getBookingDetails(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDTO> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long id) {
        BookingDTO cancelled = bookingFacade.cancelBooking(id);
        return ResponseEntity.ok(cancelled);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequestDTO request) {
        BookingDTO updated = bookingFacade.updateBookingWithValidation(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("✅ Booking Service is UP! Using Builder + Facade patterns");
    }
}