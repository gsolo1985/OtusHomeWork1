package ru.otus.integration.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FlightStatus {
    LAND("Успешно приземлился"),
    FLY("Успешно вылетел");

    private String name;
}
