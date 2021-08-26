package ru.otus.operations.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.model.OperationDto;
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

    /**
     * Получить список операций по дате заключения
     *
     * @param date - дата заключения
     * @return - список операций
     */
    @Override
    @Transactional(readOnly = true)
    public List<OperationEntity> findByOperationDate(LocalDate date) {
        return repository.findByOperationDate(date);
    }

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    @Override
    public OperationDto entityToDto(OperationEntity entity) {
        if (entity == null)
            return new OperationDto();

        var result = OperationDto.builder()
                .amount(entity.getAmount())
                .num(entity.getNum())
                .operationDate(entity.getOperationDate())
                .operationId(entity.getOperationId())
                .planDate(entity.getPlanDate())
                .stateName(entity.getState().getName())
                .build();

        Optional.ofNullable(entity.getSecurityEntity()).ifPresent(s -> {
            result.setSecurityName(s.getName());
            result.setSecurityTypeName(Constants.SecurityType.values()[s.getType()].getName());
        });

        Optional.ofNullable(entity.getCurrencyCashEntity()).ifPresent(c -> {
            result.setCurrencyName(c.getName());
        });
        return result;
    }

    /**
     * Удаление по id
     *
     * @param id - идентификатор
     */
    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
