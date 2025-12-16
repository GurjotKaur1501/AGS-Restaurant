package se.yrgo.booking_service.client.clientdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalUserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}