package se.yrgo.restarurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.yrgo.restarurantservice.dto.TableDTO;
import se.yrgo.restarurantservice.dto.TableRequestDTO;
import se.yrgo.restarurantservice.entity.Table;
import se.yrgo.restarurantservice.service.TableService;

import java.net.URI;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping
    public ResponseEntity<TableDTO> create(@Valid @RequestBody TableRequestDTO dto) {
        TableDTO created = tableService.create(dto);
        return ResponseEntity.created(URI.create("/api/tables/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> update(@PathVariable Long id, @Valid @RequestBody TableRequestDTO dto) {
        return ResponseEntity.ok(tableService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<TableDTO>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(tableService.findByRestaurantId(restaurantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Allocation endpoint — uses strategy parameter
    @PostMapping("/allocate")
    public ResponseEntity<TableDTO> allocate(
            @RequestParam Long restaurantId,
            @RequestParam int partySize,
            @RequestParam(defaultValue = "firstFit") String strategy) {
        Table t = tableService.allocateTableForParty(restaurantId, partySize, strategy);
        TableDTO dto = new TableDTO();
        dto.setId(t.getId());
        dto.setTableNumber(t.getTableNumber());
        dto.setSeats(t.getSeats());
        dto.setAvailable(t.getAvailable());
        return ResponseEntity.ok(dto);
    }
}
