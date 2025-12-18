package se.yrgo.booking_service.dto;

import se.yrgo.booking_service.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long tableId;
    private LocalDateTime bookingTime;
    private Integer numberOfGuests;
    private BookingStatus status;
    private String specialRequests;
    private String confirmationCode;
    private LocalDateTime createdAt;
}