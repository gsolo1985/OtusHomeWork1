package ru.otus.integration.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    String number;
    String departure;
    String arrival;

    public String getInfo() {
        return "Рейс №" + this.number + " из '" + this.departure + "' в '" + this.arrival + "'";
    }
}
