package se.yrgo.restarurantservice.service;

import se.yrgo.restarurantservice.dto.RestaurantDTO;
import se.yrgo.restarurantservice.dto.RestaurantRequestDTO;

import java.util.List;

public interface RestaurantService {
    RestaurantDTO create(RestaurantRequestDTO dto);
    RestaurantDTO update(Long id, RestaurantRequestDTO dto);
    RestaurantDTO findById(Long id);
    List<RestaurantDTO> findAll();
    void delete(Long id);
}
