package se.yrgo.restarurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.restarurantservice.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
