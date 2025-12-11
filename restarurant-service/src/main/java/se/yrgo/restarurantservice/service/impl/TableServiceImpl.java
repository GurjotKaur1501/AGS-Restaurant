package se.yrgo.restarurantservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.restarurantservice.dto.TableDTO;
import se.yrgo.restarurantservice.dto.TableRequestDTO;
import se.yrgo.restarurantservice.entity.Restaurant;
import se.yrgo.restarurantservice.entity.Table;
import se.yrgo.restarurantservice.repository.RestaurantRepository;
import se.yrgo.restarurantservice.repository.TableRepository;
import se.yrgo.restarurantservice.service.TableService;
import se.yrgo.restarurantservice.service.impl.strategy.TableAllocationStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final Map<String, TableAllocationStrategy> strategies; // Spring will autowire strategy beans by name

    @Override
    public TableDTO create(TableRequestDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + dto.getRestaurantId()));
        Table t = Table.builder()
                .tableNumber(dto.getTableNumber())
                .seats(dto.getSeats())
                .available(dto.getAvailable() == null ? true : dto.getAvailable())
                .restaurant(restaurant)
                .build();
        Table saved = tableRepository.save(t);
        return modelMapper.map(saved, TableDTO.class);
    }

    @Override
    public TableDTO update(Long id, TableRequestDTO dto) {
        Table t = tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + id));
        t.setTableNumber(dto.getTableNumber());
        t.setSeats(dto.getSeats());
        t.setAvailable(dto.getAvailable());
        // optionally change restaurant
        if (!t.getRestaurant().getId().equals(dto.getRestaurantId())) {
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + dto.getRestaurantId()));
            t.setRestaurant(restaurant);
        }
        Table saved = tableRepository.save(t);
        return modelMapper.map(saved, TableDTO.class);
    }

    @Override
    public TableDTO findById(Long id) {
        Table t = tableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + id));
        return modelMapper.map(t, TableDTO.class);
    }

    @Override
    public List<TableDTO> findByRestaurantId(Long restaurantId) {
        return tableRepository.findByRestaurantId(restaurantId).stream()
                .map(t -> modelMapper.map(t, TableDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        tableRepository.deleteById(id);
    }

    @Override
    public Table allocateTableForParty(Long restaurantId, int partySize, String strategyName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));
        List<Table> tables = tableRepository.findByRestaurantId(restaurantId);
        TableAllocationStrategy strategy = strategies.getOrDefault(strategyName, strategies.get("firstFit"));
        Optional<Table> found = strategy.allocate(tables, partySize);
        if (found.isPresent()) {
            Table t = found.get();
            t.setAvailable(false);
            return tableRepository.save(t);
        } else {
            throw new IllegalArgumentException("No table available for party size " + partySize);
        }
    }
}
