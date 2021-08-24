package ru.otus.operations.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.exception.GenerateOperationNotValidException;
import ru.otus.operations.publish.operationreval.OperationRevalDto;
import ru.otus.operations.repository.OperationRepository;
import ru.otus.operations.state.OperationState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static ru.otus.operations.constants.OperationConstants.*;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository repository;
    private final SecurityService securityService;
    private final CurrencyCashService currencyCashService;

    @Value(value = "${dealGenerate.numberByDate}")
    private int dealCount;
    @Value(value = "${dealGenerate.numberT2}")
    private int numberT2;
    @Value(value = "${dealGenerate.numberT1}")
    private int numberT1;
    @Value(value = "${dealGenerate.cancel}")
    private int numberCancel;
    @Value(value = "${nationalCurrency.name}")
    private String nationalCurrencyName;

    /**
     * Сгенерировать сделки/операции за дату
     *
     * @param operDateEntity - дата
     * @return - сгенерированные сделки/операции
     */
    @Override
    public List<OperationEntity> generateByDate(OperDateEntity operDateEntity) {
        int dealGenerateCount = dealCount;
        int numberT2Generate = numberT2;
        int numberT1Generate = numberT1;

        List<OperationEntity> result = new ArrayList<>();

        if (dealGenerateCount > 0) {
            if (dealGenerateCount < numberT2Generate + numberT1Generate) {
                throw new GenerateOperationNotValidException("Incorrect parameters for generate operations");
            }

            var operDate = operDateEntity.getOperDate();
            var dateT1 = operDate.plusDays(1);
            var dateT2 = operDate.plusDays(2);

            //сначала Т1 операции
            while (numberT1Generate != 0) {
                result.add(OperationEntity.builder()
                        .operationDate(operDate)
                        .planDate(dateT1)
                        .currencyCashEntity(currencyCashService.getRandomCurrency())
                        .securityEntity(securityService.getRandomSecurity())
                        .num(RandomUtils.nextInt(OPERATION_NUM_MIN_VALUE, OPERATION_NUM_MAX_VALUE))
                        .amount(getRandomBigDecimal())
                        .actualDate(LocalDateTime.now())
                        .state(OperationState.LOADED)
                        .build());

                numberT1Generate = numberT1Generate - 1;
                dealGenerateCount = dealGenerateCount - 1;
            }

            // T2 операции
            while (numberT2Generate != 0) {
                result.add(OperationEntity.builder()
                        .operationDate(operDate)
                        .planDate(dateT2)
                        .currencyCashEntity(currencyCashService.getRandomCurrency())
                        .securityEntity(securityService.getRandomSecurity())
                        .num(RandomUtils.nextInt(OPERATION_NUM_MIN_VALUE, OPERATION_NUM_MAX_VALUE))
                        .amount(getRandomBigDecimal())
                        .actualDate(LocalDateTime.now())
                        .state(OperationState.LOADED)
                        .build());

                numberT2Generate = numberT2Generate - 1;
                dealGenerateCount = dealGenerateCount - 1;
            }

            // оставльные операции

            while (dealGenerateCount != 0) {
                result.add(OperationEntity.builder()
                        .operationDate(operDate)
                        .planDate(operDate)
                        .currencyCashEntity(currencyCashService.getRandomCurrency())
                        .securityEntity(securityService.getRandomSecurity())
                        .num(RandomUtils.nextInt(OPERATION_NUM_MIN_VALUE, OPERATION_NUM_MAX_VALUE))
                        .amount(getRandomBigDecimal())
                        .actualDate(LocalDateTime.now())
                        .state(OperationState.EXEC)
                        .build());

                dealGenerateCount = dealGenerateCount - 1;
            }

            return Lists.newArrayList(repository.saveAll(result));
        }

        return result;
    }

    /**
     * Отмена операций за плановую дату
     * @param operDateEntity - дата
     * @return - отмененные операции
     */
    @Override
    public List<OperationEntity> cancelByPlanDate(OperDateEntity operDateEntity) {
        int cancelByDateNum = numberCancel;
        List<OperationEntity> result = new ArrayList<>();

        var operPlanByDateList = repository.findByPlanDateAndState(operDateEntity.getOperDate(), OperationState.LOADED);

        if (operPlanByDateList != null && operPlanByDateList.size() > 0) {
            Set<Long> operIdSet = new HashSet<>();

            while (cancelByDateNum > 0) {
                int getIndex = RandomUtils.nextInt(0, operPlanByDateList.size());
                operIdSet.add(operPlanByDateList.get(getIndex).getOperationId());
                cancelByDateNum = cancelByDateNum - 1;
            }

            operIdSet.forEach(id -> {
                var oper = operPlanByDateList.stream().filter(o -> o.getOperationId().equals(id)).findFirst();
                oper.ifPresent(o -> {
                    o.setActualDate(LocalDateTime.now());
                    o.setState(OperationState.CANCELED);
                    result.add(o);
                });
            });

            return Lists.newArrayList(repository.saveAll(result));
        }
        return result;
    }

    /**
     * Исполнение операций за плановую дату
     * @param operDateEntity - дата
     * @return - исполненные операции
     */
    @Override
    public List<OperationEntity> execByPlanDate(OperDateEntity operDateEntity) {
        List<OperationEntity> result = new ArrayList<>();
        var operPlanByDateList = repository.findByPlanDateAndState(operDateEntity.getOperDate(), OperationState.LOADED); //отбираем операции по плановой дате исполнения (не исполненные и не отмененные)

        if (operPlanByDateList != null && operPlanByDateList.size() > 0) {
            operPlanByDateList.forEach(oper -> {
                oper.setActualDate(LocalDateTime.now());
                oper.setState(OperationState.EXEC);
                result.add(oper);
            });

            return Lists.newArrayList(repository.saveAll(result));
        }
        return result;
    }

    /**+
     * Получить все операции, по которым нужно произвести переоценку на дату
     * @param operDateEntity - дата
     * @return - операции для переоценки
     */
    @Override
    public List<OperationRevalDto> getOperationForRevalByDate(OperDateEntity operDateEntity) {
        List<OperationRevalDto> result = new ArrayList<>();

        //нужно отобрать все не исполненные сделки, у которых:
        //статус <> EXEC или CANCEL
        //валюте <> нац. валюте из настройи

        var operList = repository.findByState(OperationState.LOADED);

        if (operList != null && operList.size() > 0) {
            operList.forEach(oper -> {
                var currency = oper.getCurrencyCashEntity();

                if (!currency.getName().equals(nationalCurrencyName)) {
                    result.add(getOperationDtoRevalFromEntity(oper, operDateEntity));
                }
            });
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperationEntity> findById(long id) {
        return repository.findById(id);
    }

    private OperationRevalDto getOperationDtoRevalFromEntity(OperationEntity oper, OperDateEntity operDateEntity) {
        return OperationRevalDto.builder()
                .operationId(oper.getOperationId())
                .amount(oper.getAmount())
                .currencyName(oper.getCurrencyCashEntity().getName())
                .revalDate(operDateEntity.getOperDate())
                .build();
    }

    private BigDecimal getRandomBigDecimal() {
        int randomInt = RandomUtils.nextInt(OPERATION_AMOUNT_MIN_FIX_VALUE, OPERATION_AMOUNT_MAX_FIX_VALUE);
        int randomFloat = RandomUtils.nextInt(OPERATION_AMOUNT_MIN_FLOAT_VALUE, OPERATION_AMOUNT_MAX_FLOAT_VALUE);
        return new BigDecimal(randomInt + "." + randomFloat);
    }
}
