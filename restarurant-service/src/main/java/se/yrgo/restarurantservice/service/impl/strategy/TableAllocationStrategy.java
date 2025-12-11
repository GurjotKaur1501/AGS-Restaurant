package se.yrgo.restarurantservice.service.impl.strategy;

import se.yrgo.restarurantservice.entity.Table;

import java.util.List;
import java.util.Optional;

public interface TableAllocationStrategy {
    Optional<Table> allocate(List<Table> tables, int partySize);
    String name();
}
