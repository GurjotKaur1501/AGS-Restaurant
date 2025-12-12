package se.yrgo.restarurantservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.restarurantservice.dto.RestaurantDTO;
import se.yrgo.restarurantservice.dto.TableDTO;
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

    private RestaurantDTO toDto(Restaurant r) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setAddress(r.getAddress());
        dto.setCuisineType(r.getCuisineType() == null ? null : r.getCuisineType().name());
        // map tables explicitly
        List<TableDTO> tableDtos = r.getTables().stream()
                .map(t -> modelMapper.map(t, TableDTO.class))
                .collect(Collectors.toList());
        dto.setTables(tableDtos);
        return dto;
    }

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
        return toDto(saved);
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
        return toDto(saved);
    }

    @Override
    public RestaurantDTO findById(Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + id));
        return toDto(r);
    }

    @Override
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}
