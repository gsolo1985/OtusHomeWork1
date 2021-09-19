package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.exception.OperationException;
import ru.otus.operations.model.OperationDto;
import ru.otus.operations.repository.OperationRepository;
import ru.otus.operations.statemachine.OperationState;

@Service
@RequiredArgsConstructor
public class OperationSaveServiceImpl implements OperationSaveService {
    private final OperationRepository repository;
    private final SecurityService securityService;
    private final CurrencyCashService currencyCashService;
    private final OperationService operationService;

    /**
     * Сохранить операцию через dto-объект
     *
     * @param dto - dto
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public OperationDto saveDto(OperationDto dto) {
        OperationEntity entity = OperationEntity.builder()
                .amount(dto.getAmount())
                .num(dto.getNum())
                .operationDate(dto.getOperationDate())
                .planDate(dto.getPlanDate())
                .state(OperationState.LOADED)
                .operationId(dto.getOperationId())
                .build();

        var sec = securityService.getByName(dto.getSecurityName());

        if (sec.isEmpty()) {
            throw new OperationException("Save operation error: security no found");
        }
        entity.setSecurityEntity(sec.get());

        var currency = currencyCashService.getByName(dto.getCurrencyName());
        if (currency == null) {
            throw new OperationException("Save operation error: security no found");
        }
        entity.setCurrencyCashEntity(currency);

        var saved = repository.save(entity);

        return operationService.entityToDto(saved);
    }
}
