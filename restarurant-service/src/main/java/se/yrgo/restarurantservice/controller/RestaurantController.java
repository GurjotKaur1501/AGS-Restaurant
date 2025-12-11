package se.yrgo.restarurantservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.yrgo.restarurantservice.dto.RestaurantDTO;
import se.yrgo.restarurantservice.dto.RestaurantRequestDTO;
import se.yrgo.restarurantservice.service.RestaurantService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@Valid @RequestBody RestaurantRequestDTO dto) {
        RestaurantDTO created = restaurantService.create(dto);
        return ResponseEntity.created(URI.create("/api/restaurants/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDTO dto) {
        return ResponseEntity.ok(restaurantService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
