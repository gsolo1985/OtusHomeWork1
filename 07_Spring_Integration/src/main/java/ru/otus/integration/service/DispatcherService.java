package ru.otus.integration.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.integration.constants.Cities;
import ru.otus.integration.constants.ExtraStatus;
import ru.otus.integration.constants.FlightStatus;
import ru.otus.integration.domain.AirportBoardMessage;
import ru.otus.integration.domain.Flight;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DispatcherService {

    public AirportBoardMessage result(Flight flight) throws Exception {
        if (flight.getDeparture().equals(Cities.MOSCOW.getName())) {
            System.out.println("Объявляется посадка на: " + flight.getInfo());
            Thread.sleep(1000);
        } else {
            System.out.println("Заходит на посадку: " + flight.getInfo());
            Thread.sleep(2000);
        }

        return getBoardMessage(flight);
    }

    private AirportBoardMessage getBoardMessage(Flight flight) {
        int random = RandomUtils.nextInt(0, 99);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss");

        StringBuilder sb = new StringBuilder((flight.getDeparture().equals(Cities.MOSCOW.getName())) ?
                FlightStatus.FLY.getName() : FlightStatus.LAND.getName()).append(" в ").append(formatForDateNow.format(new Date()));

        if (random < 20)
            sb.append(". ").append(ExtraStatus.DELAY.getName()).append(RandomUtils.nextInt(1, 5)).append(" (мин.)");

        if (random < 10)
            sb.append(". ").append(ExtraStatus.ALARM.getName());

        return AirportBoardMessage.builder()
                .flight(flight)
                .status(sb.toString())
                .build();
    }


}
