package ru.otus.integration.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirportBoardMessage {
    Flight flight;
    String status;

    public String getInfo() {
        return this.flight.getInfo() + " - " + this.status;
    }
}
