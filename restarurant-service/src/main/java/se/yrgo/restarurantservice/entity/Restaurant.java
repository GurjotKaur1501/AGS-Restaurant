package se.yrgo.restarurantservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // ensures arraylist is initiated even when using Builder
    private List<Table> tables = new ArrayList<>();

    public void addTable(Table table) {
        validateTable(table);
        linkTable(table);
    }

    private void validateTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("Table must not be null");
        }
    }

    private void linkTable(Table table) {
        table.setRestaurant(this); // calls Table.setRestaurant(...)
        tables.add(table);
    }
}
