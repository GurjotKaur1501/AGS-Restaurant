package se.yrgo.booking_service.client.clientdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalTableDTO {
    private Long id;
    private String tableNumber;
    private Integer capacity;
    private String location;
    private Long restaurantId;
}