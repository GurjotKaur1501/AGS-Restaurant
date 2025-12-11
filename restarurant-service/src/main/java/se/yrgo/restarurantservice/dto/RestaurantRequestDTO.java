package se.yrgo.restarurantservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestaurantRequestDTO {
    @NotBlank
    @Size(min = 2)
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String cuisineType;
}
