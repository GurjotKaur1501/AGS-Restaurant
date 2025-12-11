package se.yrgo.restarurantservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private List<TableDTO> tables;
}
