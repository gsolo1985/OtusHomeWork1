package ru.otus.operations.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.operations.constants.Constants;

@AllArgsConstructor
@Getter
public enum OperationEvent implements Constants.ConstantsValue {
    LOAD("Загрузка"),
    CANCEL("Отмена"),
    EXECUTE("Исполнение");

    private String name;
}
