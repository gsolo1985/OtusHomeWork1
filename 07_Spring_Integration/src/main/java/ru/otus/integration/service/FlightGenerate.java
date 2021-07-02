package ru.otus.integration.service;

import ru.otus.integration.domain.Flight;

import java.util.Collection;

public interface FlightGenerate {
    Collection<Flight> generate();
}
