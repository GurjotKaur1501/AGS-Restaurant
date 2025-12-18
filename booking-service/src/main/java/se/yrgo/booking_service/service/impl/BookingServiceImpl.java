package se.yrgo.booking_service.service.impl;

import se.yrgo.booking_service.dto.BookingDTO;
import se.yrgo.booking_service.dto.request.BookingRequestDTO;
import se.yrgo.booking_service.entity.Booking;
import se.yrgo.booking_service.entity.BookingStatus;
import se.yrgo.booking_service.exception.BookingNotFoundException;
import se.yrgo.booking_service.repository.BookingRepository;
import se.yrgo.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public BookingDTO createBooking(BookingRequestDTO request) {
        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .tableId(request.getTableId())
                .bookingTime(request.getBookingTime())
                .numberOfGuests(request.getNumberOfGuests())
                .specialRequests(request.getSpecialRequests())
                .status(BookingStatus.CONFIRMED)
                .confirmationCode(generateConfirmationCode())
                .build();

        Booking saved = bookingRepository.save(booking);

        return BookingDTO.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .tableId(saved.getTableId())
                .bookingTime(saved.getBookingTime())
                .numberOfGuests(saved.getNumberOfGuests())
                .status(saved.getStatus())
                .confirmationCode(saved.getConfirmationCode())
                .specialRequests(saved.getSpecialRequests())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        return BookingDTO.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .tableId(booking.getTableId())
                .bookingTime(booking.getBookingTime())
                .numberOfGuests(booking.getNumberOfGuests())
                .status(booking.getStatus())
                .confirmationCode(booking.getConfirmationCode())
                .specialRequests(booking.getSpecialRequests())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    @Override
    public List<BookingDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(booking -> BookingDTO.builder()
                        .id(booking.getId())
                        .userId(booking.getUserId())
                        .tableId(booking.getTableId())
                        .bookingTime(booking.getBookingTime())
                        .numberOfGuests(booking.getNumberOfGuests())
                        .status(booking.getStatus())
                        .confirmationCode(booking.getConfirmationCode())
                        .specialRequests(booking.getSpecialRequests())
                        .createdAt(booking.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelled = bookingRepository.save(booking);

        return BookingDTO.builder()
                .id(cancelled.getId())
                .userId(cancelled.getUserId())
                .tableId(cancelled.getTableId())
                .bookingTime(cancelled.getBookingTime())
                .numberOfGuests(cancelled.getNumberOfGuests())
                .status(cancelled.getStatus())
                .confirmationCode(cancelled.getConfirmationCode())
                .specialRequests(cancelled.getSpecialRequests())
                .createdAt(cancelled.getCreatedAt())
                .build();
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(booking -> BookingDTO.builder()
                        .id(booking.getId())
                        .userId(booking.getUserId())
                        .tableId(booking.getTableId())
                        .bookingTime(booking.getBookingTime())
                        .numberOfGuests(booking.getNumberOfGuests())
                        .status(booking.getStatus())
                        .confirmationCode(booking.getConfirmationCode())
                        .specialRequests(booking.getSpecialRequests())
                        .createdAt(booking.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        booking.setTableId(request.getTableId());
        booking.setBookingTime(request.getBookingTime());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setSpecialRequests(request.getSpecialRequests());

        Booking updated = bookingRepository.save(booking);

        return BookingDTO.builder()
                .id(updated.getId())
                .userId(updated.getUserId())
                .tableId(updated.getTableId())
                .bookingTime(updated.getBookingTime())
                .numberOfGuests(updated.getNumberOfGuests())
                .status(updated.getStatus())
                .confirmationCode(updated.getConfirmationCode())
                .specialRequests(updated.getSpecialRequests())
                .createdAt(updated.getCreatedAt())
                .build();
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }

    private String generateConfirmationCode() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}