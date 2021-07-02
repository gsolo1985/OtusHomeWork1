package ru.otus.integration.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExtraStatus {
    DELAY("Опоздание на "),
    ALARM("На борту случилась нештатная ситуация");

    private String name;
}

