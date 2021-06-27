package ru.otus.integration.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.integration.domain.AirportBoardMessage;
import ru.otus.integration.domain.Flight;
import ru.otus.integration.publish.Airport;

import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class AirportScheduler {
    private final Airport airport;
    private final FlightGenerate flightGenerate;
    private String ROW_DELIMITER = "__________________________________________________";

    @Scheduled(fixedDelay = 8000)
    public void manageFlight() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        
        pool.execute(() -> {
            Collection<Flight> flights = flightGenerate.generate();

            System.out.println(ROW_DELIMITER);
            System.out.println("New flights:\n" + ROW_DELIMITER + "\n" +
                    flights.stream().map(Flight::getInfo).collect(Collectors.joining(", \n")));
            Collection<AirportBoardMessage> table = airport.action(flights);

            System.out.println(ROW_DELIMITER);
            System.out.println("Result flights:\n" + ROW_DELIMITER + "\n" +
                    table.stream()
                    .map(AirportBoardMessage::getInfo)
                    .collect(Collectors.joining(", \n")));
        });

    }

}
