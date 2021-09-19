package ru.otus.operations.service;

import org.springframework.data.domain.Sort;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.model.BusinessProcessDto;

import java.util.List;
import java.util.Optional;

public interface BusinessProcessService {
    /**
     * Получить все бизнес-процессы (с учетом сортировки)
     *
     * @param sort - сортировка
     * @return - список бизнес-процессов
     */
    List<BusinessProcessEntity> findAll(Sort sort);

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    BusinessProcessDto entityToDto(BusinessProcessEntity entity);

    /**
     * Получить БП по системному имени
     *
     * @param sysName - системное имя
     * @return - БП
     */
    Optional<BusinessProcessEntity> findBySysName(String sysName);
}
