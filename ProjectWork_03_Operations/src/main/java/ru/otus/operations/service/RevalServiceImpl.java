package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.consumer.RevalOperation;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;
import ru.otus.operations.exception.RevalNotValidException;
import ru.otus.operations.repository.RevalRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RevalServiceImpl implements RevalService {
    private final CurrencyCashService currencyCashService;
    private final OperationService operationService;
    private final RevalRepository repository;

    @Value(value = "${nationalCurrency.name}")
    private String nationalCurrencyName;

    private static final String INSERT_ERROR = "New reval insert error: ";

    /**
     * Сохранение переоценки по операции
     *
     * @param reval - объект с переоценкой
     * @return - сохраненный объект
     */
    @Override
    public RevalEntity save(RevalEntity reval) {
        if (reval == null) {
            throw new RevalNotValidException(INSERT_ERROR + "reval isn't defined.");
        }

        var cur1 = reval.getCurrencyEntity();
        var cur2 = reval.getCurrencyRevalEntity();

        if (cur1 == null || cur2 == null) {
            throw new RevalNotValidException(INSERT_ERROR + "currencies aren't defined.");
        }

        var oper = reval.getOperationEntity();

        if (oper == null) {
            throw new RevalNotValidException(INSERT_ERROR + "operation isn't defined");
        }

        var revalFind = repository.findByOperationEntityAndOperDate(oper, reval.getOperDate());

        revalFind.ifPresent(revalEntity -> reval.setRevalId(revalEntity.getRevalId()));
        return repository.save(reval);
    }

    /**
     * Преобразование dto-объкта в Entity-объект
     *
     * @param dto - dto-объкт
     * @return - Entity-объект
     */
    @Override
    public RevalEntity dtoTransformToEntity(RevalOperation dto) {
        var natCurrency = currencyCashService.getByName(nationalCurrencyName);

        if (dto.getCurrencyName() == null)
            dto.setCurrencyName(nationalCurrencyName);

        var revalCurrency = currencyCashService.getByName(dto.getCurrencyName());

        var operation = operationService.findById(dto.getOperationId());

        return operation.map(operationEntity -> RevalEntity.builder()
                .currencyEntity(natCurrency)
                .currencyRevalEntity(revalCurrency)
                .operationEntity(operationEntity)
                .operDate(dto.getRevalDate())
                .revalValue(dto.getRevalAmount())
                .build())
                .orElse(RevalEntity.builder()
                        .currencyEntity(natCurrency)
                        .currencyRevalEntity(revalCurrency)
                        .operationEntity(null)
                        .operDate(dto.getRevalDate())
                        .revalValue(dto.getRevalAmount())
                        .build());
    }

    /**
     * Получить переоценку по операции на дату
     *
     * @param operationEntity - операция
     * @param localDate       - дата
     * @return - переоценка
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RevalEntity> getRevalByOperationAndDate(OperationEntity operationEntity, LocalDate localDate) {
        return repository.findByOperationEntityAndOperDate(operationEntity, localDate);
    }

}
