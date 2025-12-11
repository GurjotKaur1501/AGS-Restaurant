package se.yrgo.restarurantservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TableRequestDTO {
    @NotNull
    private Integer tableNumber;

    @NotNull
    @Min(1)
    private Integer seats;

    private Boolean available = true;

    @NotNull
    private Long restaurantId;
}
