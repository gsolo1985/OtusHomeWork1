package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.operations.consumer.RevalOperationList;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;
import ru.otus.operations.exception.BusinessProcessException;
import ru.otus.operations.publish.operationreval.OperationRevalDto;
import ru.otus.operations.publish.operationreval.OperationRevalDtoList;
import ru.otus.operations.publish.operationreval.OperationRevalOutPublishGateway;
import ru.otus.operations.service.*;
import ru.otus.operations.statemachine.OperationState;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class RevalOperationServiceImpl implements RevalOperationService {
    private final OperationService operationService;
    private final OperationRevalOutPublishGateway operationRevalOutPublishGateway;
    private final ProtocolService protocolService;
    private final RevalService revalService;
    private final OperDateService operDateService;
    private final BusinessProcessService businessProcessService;

    @Value(value = "${nationalCurrency.name}")
    private String nationalCurrencyName;

    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_CURRENCY_REVAL_SYS_NAME"
     * Отбирает операции, для которых нужна вал. переоценка и отправляет по кафке в сервис для расчета
     *
     * @param operDateEntity - дата
     */
    @Override
    public void exec(OperDateEntity operDateEntity) {
        System.out.println("Переоценка операций за дату " + operDateEntity.getOperDate());
        var operations = getOperationForRevalByDate(operDateEntity);

        var bpOpt = businessProcessService.findBySysName(OPERATIONS_CURRENCY_REVAL_SYS_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPERATIONS_CURRENCY_REVAL_SYS_NAME");
        }

        if (operations.size() > 0) {
            System.out.println("Отправлено на расчет переоценки " + operations.size() + " операций");
            operationRevalOutPublishGateway.publish(OperationRevalDtoList.builder()
                    .operationList(operations).build());
        }

        // TODO УДАЛИТЬ
//        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // добавим обработанный протокол
//        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSING); // добавим протокол
    }

    /**
     * Сохранить посчитанную другим сервисом валютную переоценку и поменить процесс, как выполненный
     *
     * @param calcRevalList - посчитанная переоценка
     */
    @Override
    public void saveCalcReval(RevalOperationList calcRevalList) {
        if (calcRevalList != null && calcRevalList.getRevalOperationList() != null && calcRevalList.getRevalOperationList().size() > 0) {
            var revalDtoList = calcRevalList.getRevalOperationList();
            System.out.println("Получен расчет переоценки по " + revalDtoList.size() + " операций");

            List<RevalEntity> revalEntities = new ArrayList<>();

            revalDtoList.forEach(r -> {
                revalEntities.add(revalService.dtoTransformToEntity(r));
            });

            revalEntities.forEach(revalService::save);
        }
        var operDay = operDateService.getOperDay();
        var bpOpt = businessProcessService.findBySysName(OPERATIONS_CURRENCY_REVAL_SYS_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPERATIONS_CURRENCY_REVAL_SYS_NAME");
        }

        operDay.ifPresent(operDateEntity -> protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED));
    }

    private List<OperationRevalDto> getOperationForRevalByDate(OperDateEntity operDateEntity) {
        List<OperationRevalDto> result = new ArrayList<>();

        //нужно отобрать все не исполненные сделки, у которых:
        //статус <> EXEC или CANCEL
        //валюте <> нац. валюте из настройи

        var operList = operationService.findByState(OperationState.LOADED);

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

    private OperationRevalDto getOperationDtoRevalFromEntity(OperationEntity oper, OperDateEntity operDateEntity) {
        return OperationRevalDto.builder()
                .operationId(oper.getOperationId())
                .amount(oper.getAmount())
                .currencyName(oper.getCurrencyCashEntity().getName())
                .revalDate(operDateEntity.getOperDate())
                .build();
    }
}
