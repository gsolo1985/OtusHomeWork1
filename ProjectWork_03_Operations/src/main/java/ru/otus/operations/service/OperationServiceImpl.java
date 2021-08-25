package ru.otus.operations.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.repository.OperationRepository;
import ru.otus.operations.statemachine.OperationState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository repository;

    /**
     * Получить операции по id
     *
     * @param id - идентификатор
     * @return - операция
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OperationEntity> findById(long id) {
        return repository.findById(id);
    }

    /**
     * Сохранить список операций
     *
     * @param list - список на сохранение
     * @return - сохраненные объекты
     */
    @Override
    @Transactional
    public List<OperationEntity> saveAll(List<OperationEntity> list) {
        return Lists.newArrayList(repository.saveAll(list));
    }

    /**
     * Получить список операций по плановой дате и статусу
     *
     * @param planDate - плановая дата
     * @param state    - статус
     * @return - список операций
     */
    @Override
    @Transactional(readOnly = true)
    public List<OperationEntity> findByPlanDateAndState(LocalDate planDate, OperationState state) {
        return repository.findByPlanDateAndState(planDate, state);
    }

    /**
     * Получить список операций статусу
     *
     * @param state - статус
     * @return - список операций
     */
    @Override
    @Transactional(readOnly = true)
    public List<OperationEntity> findByState(OperationState state) {
        return repository.findByState(state);
    }
}
