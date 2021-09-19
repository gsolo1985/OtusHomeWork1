package ru.otus.operations.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.operations.constants.Constants;

@AllArgsConstructor
@Getter
public enum OperationState implements Constants.ConstantsValue {
    LOADED("Загружена"),
    CANCELED("Отменена"),
    EXEC("Исполнена");

    private String name;
}
