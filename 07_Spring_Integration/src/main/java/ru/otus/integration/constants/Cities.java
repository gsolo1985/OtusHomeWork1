package ru.otus.integration.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Cities {
    MOSCOW("Москва"),
    SAINT_PETERSBUG("Санкт-Петербург"),
    EKATERINBURG("Екатеринбург"),
    SOCHI("Сочи"),
    HELSINKI("Хельсинки"),
    LONDON("Лондон"),
    KAZAN("Казань"),
    MADRID("Мадрид"),
    ROMA("Рим"),
    SMOLENSK("Смоленск"),
    BERLIN("Берлин"),
    PARIS("Париж"),
    MINSK("Минск"),
    KIEV("Киев"),
    TALLINN("Таллинн"),
    OSLO("Осло"),
    BARNAUL("Барнаул"),
    VLADIVOSTOK("Владивосток"),
    ISTANBUL("Стамбул"),
    VIENNA("Вена"),
    ROSTOV_ON_DON("Ростов-на-Дону");

    private String name;
}
