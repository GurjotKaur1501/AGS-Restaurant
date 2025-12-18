package se.yrgo.booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.yrgo.booking_service.dto.BookingDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {
    private BookingDTO booking;
    private String message;
    private String confirmationCode;
    private List<String> nextSteps;
    private List<String> additionalInfo;

    // Convenience builder method
    public static BookingResponseDTOBuilder builder() {
        return new BookingResponseDTOBuilder();
    }
}