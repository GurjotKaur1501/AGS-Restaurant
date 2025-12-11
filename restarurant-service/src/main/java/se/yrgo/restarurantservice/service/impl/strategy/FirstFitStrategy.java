package se.yrgo.restarurantservice.service.impl.strategy;

import org.springframework.stereotype.Component;
import se.yrgo.restarurantservice.entity.Table;

import java.util.List;
import java.util.Optional;

@Component("firstFit")
public class FirstFitStrategy implements TableAllocationStrategy {
    @Override
    public Optional<Table> allocate(List<Table> tables, int partySize) {
        if (tables == null) return Optional.empty();
        return tables.stream()
                .filter(t -> Boolean.TRUE.equals(t.getAvailable()))
                .filter(t -> t.getSeats() >= partySize)
                .findFirst();
    }
    @Override
    public String name() { return "firstFit"; }
}
