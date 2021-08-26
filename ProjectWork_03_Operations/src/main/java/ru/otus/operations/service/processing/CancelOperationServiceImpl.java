package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.exception.BusinessProcessException;
import ru.otus.operations.service.BusinessProcessService;
import ru.otus.operations.service.ProtocolService;
import ru.otus.operations.service.OperationService;
import ru.otus.operations.statemachine.OperationState;
import ru.otus.operations.statemachine.OperationStateMachine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class CancelOperationServiceImpl implements CancelOperationService {
    private final OperationService operationService;
    private final ProtocolService protocolService;
    private final OperationStateMachine operationStateMachine;
    private final BusinessProcessService businessProcessService;

    @Value(value = "${dealGenerate.cancel}")
    private int numberCancel;

    /**
     * Отмена операций за дату
     *
     * @param operDateEntity - дата
     * @return - отмененные операции
     */
    @Override
    public List<OperationEntity> exec(OperDateEntity operDateEntity) {
        System.out.println("Отмена операций за дату " + operDateEntity.getOperDate());
        int cancelByDateNum = numberCancel;
        List<OperationEntity> result = new ArrayList<>();
        var operPlanByDateList = operationService.findByPlanDateAndState(operDateEntity.getOperDate(), OperationState.LOADED);

        var bpOpt = businessProcessService.findBySysName(OPERATIONS_CANCEL_SYS_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPERATIONS_CANCEL_SYS_NAME");
        }

        if (operPlanByDateList != null && operPlanByDateList.size() > 0) {
            Set<Long> operIdSet = new HashSet<>();
            List<OperationEntity> forSave = new ArrayList<>();

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
                    forSave.add(o);
                });
            });

            System.out.println("Отменено " + forSave.size() + " операций");
            result = operationService.saveAll(forSave);
            result.forEach(operationStateMachine::cancelOperation); // раскрутка стейт машины
        }

        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // добавим обработанный протокол
        return result;
    }
}
