package se.yrgo.restarurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.restarurantservice.entity.Table;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> findByRestaurantId(Long restaurantId);
}
