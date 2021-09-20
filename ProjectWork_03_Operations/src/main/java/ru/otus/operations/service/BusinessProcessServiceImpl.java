package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.exception.OperationException;
import ru.otus.operations.model.BusinessProcessDto;
import ru.otus.operations.repository.BusinessProcessRepository;
import ru.otus.operations.statemachine.OperationState;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessProcessServiceImpl implements BusinessProcessService {
    private final BusinessProcessRepository repository;

    /**
     * Получить все бизнес-процессы (с учетом сортировки)
     *
     * @param sort - сортировка
     * @return - список бизнес-процессов
     */
    @Override
    @Transactional(readOnly = true)
    public List<BusinessProcessEntity> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    @Override
    public BusinessProcessDto entityToDto(BusinessProcessEntity entity) {
        return BusinessProcessDto.builder()
                .sysName(entity.getSysName())
                .businessProcessId(entity.getBusinessProcessId())
                .order(entity.getOrderType())
                .isOn(entity.getIsOn())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusinessProcessEntity> findBySysName(String sysName) {
        return repository.findBySysName(sysName);
    }

    /**
     * Сохранить БП через dto-объект
     *
     * @param businessProcessDto - dto
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public BusinessProcessDto saveDto(BusinessProcessDto businessProcessDto) {
        BusinessProcessEntity entity = BusinessProcessEntity.builder()
                .orderType(businessProcessDto.getOrder())
                .sysName(businessProcessDto.getSysName())
                .isOn(businessProcessDto.getIsOn())
                .build();

        var find = repository.findBySysName(entity.getSysName());

        find.ifPresent(f -> {
            entity.setBusinessProcessId(f.getBusinessProcessId());
        });

        return entityToDto(repository.save(entity));
    }
}
