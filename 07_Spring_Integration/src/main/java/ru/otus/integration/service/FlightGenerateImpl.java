package ru.otus.integration.service;

import org.springframework.stereotype.Service;
import ru.otus.integration.constants.Cities;
import ru.otus.integration.domain.Flight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

@Service
public class FlightGenerateImpl implements FlightGenerate {
    @Override
    public Collection<Flight> generate() {
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            flights.add(getNewFlight());
        }
        return flights;
    }

    private Flight getNewFlight() {
        boolean departure = RandomUtils.nextBoolean();

        return Flight.builder()
                .number(departure? "D-" + RandomUtils.nextInt(299, 599) : "A-" + RandomUtils.nextInt(699, 999))
                .arrival(departure? Cities.values()[RandomUtils.nextInt(1, Cities.values().length)].getName() : Cities.MOSCOW.getName())
                .departure(departure? Cities.MOSCOW.getName() : Cities.values()[RandomUtils.nextInt(1, Cities.values().length)].getName())
                .build();
    }
}
