package ru.otus.operations.service;

import org.springframework.data.domain.Sort;
import ru.otus.operations.domain.BusinessProcessEntity;

import java.util.List;

public interface BusinessProcessService {
    /**
     * Получить все бизнес-процессы (с учетом сортировки)
     *
     * @param sort - сортировка
     * @return - список бизнес-процессов
     */
    List<BusinessProcessEntity> findAll(Sort sort);
}
