package se.yrgo.booking_service.service;
import se.yrgo.booking_service.dto.BookingDTO;
import se.yrgo.booking_service.dto.request.BookingRequestDTO;
import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingRequestDTO request);
    BookingDTO getBookingById(Long id);
    List<BookingDTO> getBookingsByUserId(Long userId);
    BookingDTO updateBooking(Long id, BookingRequestDTO request);
    BookingDTO cancelBooking(Long id);
    void deleteBooking(Long id);
    List<BookingDTO> getAllBookings();
}
