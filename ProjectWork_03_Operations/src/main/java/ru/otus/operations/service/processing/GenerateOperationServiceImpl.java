package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.exception.BusinessProcessException;
import ru.otus.operations.exception.GenerateOperationNotValidException;
import ru.otus.operations.service.*;
import ru.otus.operations.statemachine.OperationState;
import ru.otus.operations.statemachine.OperationStateMachine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.*;
import static ru.otus.operations.constants.OperationConstants.*;

@Service
@RequiredArgsConstructor
public class GenerateOperationServiceImpl implements GenerateOperationService {
    private final CurrencyCashService currencyCashService;
    private final SecurityService securityService;
    private final OperationService operationService;
    private final ProtocolService protocolService;
    private final OperationStateMachine operationStateMachine;
    private final BusinessProcessService businessProcessService;

    @Value(value = "${dealGenerate.numberByDate}")
    private int dealCount;
    @Value(value = "${dealGenerate.numberT2}")
    private int numberT2;
    @Value(value = "${dealGenerate.numberT1}")
    private int numberT1;

    /**
     * Сгенерировать сделки/операции за дату
     *
     * @param operDateEntity - дата
     * @return - сгенерированные сделки/операции
     */
    @Override
    public List<OperationEntity> exec(OperDateEntity operDateEntity) {
        System.out.println("Генерация операций за дату " + operDateEntity.getOperDate());
        int dealGenerateCount = dealCount;
        int numberT2Generate = numberT2;
        int numberT1Generate = numberT1;

        List<OperationEntity> result = new ArrayList<>();

        var bpOpt = businessProcessService.findBySysName(OPERATIONS_CREATE_SYS_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPERATIONS_CREATE_SYS_NAME");
        }

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
                        .state(OperationState.LOADED)
                        .build());

                dealGenerateCount = dealGenerateCount - 1;
            }

            System.out.println("Загружено " + result.size() + " операций. Из которых со сроком сегодня - " + (dealCount - numberT2 - numberT1) + ", со сроком 1 день - " + numberT1 + ", со сроком 2 дня - " + numberT2);

            result = operationService.saveAll(result);
            result.forEach(operationStateMachine::loadOperation); // раскрутка стейт машины
        }

        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // добавим обработанный протокол
        return result;
    }

    private BigDecimal getRandomBigDecimal() {
        int randomInt = RandomUtils.nextInt(OPERATION_AMOUNT_MIN_FIX_VALUE, OPERATION_AMOUNT_MAX_FIX_VALUE);
        int randomFloat = RandomUtils.nextInt(OPERATION_AMOUNT_MIN_FLOAT_VALUE, OPERATION_AMOUNT_MAX_FLOAT_VALUE);
        return new BigDecimal(randomInt + "." + randomFloat);
    }
}
