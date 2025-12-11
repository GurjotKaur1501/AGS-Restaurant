package se.yrgo.restarurantservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.restarurantservice.dto.RestaurantDTO;
import se.yrgo.restarurantservice.dto.RestaurantRequestDTO;
import se.yrgo.restarurantservice.entity.CuisineType;
import se.yrgo.restarurantservice.entity.Restaurant;
import se.yrgo.restarurantservice.repository.RestaurantRepository;
import se.yrgo.restarurantservice.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public RestaurantDTO create(RestaurantRequestDTO dto) {
        CuisineType ct;
        try {
            ct = CuisineType.valueOf(dto.getCuisineType().toUpperCase());
        } catch (Exception e) {
            ct = CuisineType.OTHER;
        }
        Restaurant r = Restaurant.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .cuisineType(ct)
                .build();
        Restaurant saved = restaurantRepository.save(r);
        return modelMapper.map(saved, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO update(Long id, RestaurantRequestDTO dto) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + id));
        r.setName(dto.getName());
        r.setAddress(dto.getAddress());
        try {
            r.setCuisineType(CuisineType.valueOf(dto.getCuisineType().toUpperCase()));
        } catch (Exception e) {
            r.setCuisineType(CuisineType.OTHER);
        }
        Restaurant saved = restaurantRepository.save(r);
        return modelMapper.map(saved, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO findById(Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + id));
        return modelMapper.map(r, RestaurantDTO.class);
    }

    @Override
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(r -> modelMapper.map(r, RestaurantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}
