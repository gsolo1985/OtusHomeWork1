package ru.otus.operations.service;

import ru.otus.operations.domain.OperDateEntity;

public interface BusinessProcessByOperDateService {
    /**
     * Добавить все бизнес-процессы в обработку за дату
     *
     * @param operDateEntity - операционная дата
     */
    void addBusinessProcessesByOperDate(OperDateEntity operDateEntity);

    /**
     * Установить статус у БП за дату
     *
     * @param operDateEntity               - дата
     * @param businessProcessEntitySysName - системное имя БП
     * @param status                       - статус
     */
    void setBusinessProcessesByOperDateStatus(OperDateEntity operDateEntity, String businessProcessEntitySysName, int status);


    /**
     * Проверить, все ли настроенные бизнес-процессы завершены за дату (за исключением бизнес-процессов "закрытие операционного дня" и "Подготовка к открытию операционного дня")
     *
     * @param operDateEntity - дата
     * @return - true, если все бизнес-процессы завершены
     */
    boolean checkProcessChainEndByOperDate(OperDateEntity operDateEntity);
}
