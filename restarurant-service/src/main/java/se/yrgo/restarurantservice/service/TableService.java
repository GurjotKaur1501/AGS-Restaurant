package se.yrgo.restarurantservice.service;

import se.yrgo.restarurantservice.dto.TableDTO;
import se.yrgo.restarurantservice.dto.TableRequestDTO;
import se.yrgo.restarurantservice.entity.Table;

import java.util.List;

public interface TableService {
    TableDTO create(TableRequestDTO dto);
    TableDTO update(Long id, TableRequestDTO dto);
    TableDTO findById(Long id);
    List<TableDTO> findByRestaurantId(Long restaurantId);
    void delete(Long id);

    // Strategy-based allocation
    Table allocateTableForParty(Long restaurantId, int partySize, String strategyName);
}
