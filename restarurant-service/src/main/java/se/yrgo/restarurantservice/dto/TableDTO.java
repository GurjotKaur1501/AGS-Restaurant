package se.yrgo.restarurantservice.dto;

import lombok.Data;

@Data
public class TableDTO {
    private Long id;
    private Integer tableNumber;
    private Integer seats;
    private Boolean available;
}
