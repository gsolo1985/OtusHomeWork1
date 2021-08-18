package ru.otus.operations.constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Constants {
    EMPTY("", Empty.values()),
    BUSINESS_PROCESS_SYS_NAME("Список бизнес-процессов", BusinessProcessSysName.values()),
    OPER_DATE_STATUS("Статус операционного дня", OperDateStatus.values());

    @RequiredArgsConstructor
    public enum BusinessProcessSysName implements ConstantsValue {
        EMPTY(""),
        CHECK_START_DAY_DATA("Проверка корректности данных на начало дня"),
        OPEN_OPER_DATE("Открытие операционного дня"),
        OPERATIONS_CREATE("Заведение операций"),
        OPERATIONS_EXECUTION("Исполнение операций"),
        OPERATIONS_CANCEL("Отмена операций"),
        OPERATIONS_CURRENCY_REVAL("Валютная переоценка"),
        CLOSE_OPER_DATE("Закрытие операционного дня");

        private final String name;
        public String getName() {return name;}
    }

    @RequiredArgsConstructor
    public enum OperDateStatus implements ConstantsValue {
        EMPTY("-"),
        OPEN("Операционный день открыт"),
        CLOSE("Операционный день закрыт");

        private final String name;
        public String getName() {return name;}
    }

    @RequiredArgsConstructor
    public enum Empty implements ConstantsValue {
        EMPTY("");
        private final String name;

        public String getName() {
            return name;
        }
    }

    public interface ConstantsValue {
        String name();
        int ordinal();
    }

    private final String name;
    private final ConstantsValue[] constantsValues;

    public String getName() {
        return name;
    }

    public ConstantsValue[] propValues() {
        return constantsValues;
    }
}
