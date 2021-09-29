package ru.otus.operations.service;

import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.model.ProtocolDto;

import java.time.LocalDate;
import java.util.List;

public interface ProtocolService {

    /**
     * Сохранить протокол по бизнес-процессу на дату
     * @param businessProcessEntity - БП
     * @param operDateEntity - дата
     * @param status - статус
     * @return - протокол
     */
    ProtocolEntity saveByBusinessProcessesAndOperDate(BusinessProcessEntity businessProcessEntity, OperDateEntity operDateEntity, int status);

    /**
     * Установить статус у протокола
     *
     * @param operDateEntity               - дата
     * @param businessProcessEntitySysName - системное имя БП
     * @param status                       - статус
     */
    void setProtocolStatus(OperDateEntity operDateEntity, String businessProcessEntitySysName, int status);

    /**
     * Проверить, все ли настроенные бизнес-процессы завершены за дату (за исключением бизнес-процессов "закрытие операционного дня" и "Подготовка к открытию операционного дня")
     *
     * @param operDateEntity - дата
     * @return - true, если все бизнес-процессы завершены
     */
    boolean checkProcessChainEndByOperDate(OperDateEntity operDateEntity);

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    ProtocolDto entityToDto(ProtocolEntity entity);

    /**
     * Получить список выполнения бизнес-процессов за дату
     *
     * @param operDate - дата
     * @return - результат
     */
    List<ProtocolEntity> findByOperDate(LocalDate operDate);
}
