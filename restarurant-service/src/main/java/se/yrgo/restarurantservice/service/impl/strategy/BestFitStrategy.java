package se.yrgo.restarurantservice.service.impl.strategy;

import org.springframework.stereotype.Component;
import se.yrgo.restarurantservice.entity.Table;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("bestFit")
public class BestFitStrategy implements TableAllocationStrategy {
    @Override
    public Optional<Table> allocate(List<Table> tables, int partySize) {
        if (tables == null) return Optional.empty();
        return tables.stream()
                .filter(t -> Boolean.TRUE.equals(t.getAvailable()))
                .filter(t -> t.getSeats() >= partySize)
                .min(Comparator.comparingInt(Table::getSeats)); // smallest table that fits
    }
    @Override
    public String name() { return "bestFit"; }
}
