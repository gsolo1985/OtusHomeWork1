package ru.otus.operations.service.processing;

import ru.otus.operations.domain.OperDateEntity;

public interface StartOperDayService {
    /**
     * Запуск бизнес-процесса с системным именем "OPEN_OPER_DATE_NAME"
     * Открытие операционного дня
     * Оповещение через кафку, что день открыт
     *
     * @return - открытый операционный день
     */
    OperDateEntity exec();
}
