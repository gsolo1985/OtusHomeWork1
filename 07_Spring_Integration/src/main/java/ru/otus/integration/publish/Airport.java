package ru.otus.integration.publish;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.integration.domain.AirportBoardMessage;
import ru.otus.integration.domain.Flight;

import java.util.Collection;

@MessagingGateway
public interface Airport {
    @Gateway(requestChannel = "departureArriveChannel", replyChannel = "resultChannel")
    Collection<AirportBoardMessage> action(Collection<Flight> flights);
}
