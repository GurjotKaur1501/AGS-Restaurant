package se.yrgo.restarurantservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer tableNumber;
    private Integer seats;
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private se.yrgo.restarurantservice.entity.Restaurant restaurant;

    // Explicit setter — add this to be 100% sure it's available
    public void setRestaurant(se.yrgo.restarurantservice.entity.Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
