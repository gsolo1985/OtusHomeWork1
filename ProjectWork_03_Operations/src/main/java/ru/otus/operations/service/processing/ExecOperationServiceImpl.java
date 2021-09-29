package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class ExecOperationServiceImpl implements ExecOperationService {
    private final OperationService operationService;
    private final ProtocolService protocolService;
    private final OperationStateMachine operationStateMachine;
    private final BusinessProcessService businessProcessService;

    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_EXECUTION_SYS_NAME"
     * Исполнение операций за плановую дату
     *
     * @param operDateEntity - дата
     * @return - исполненные операции
     */
    @Override
    public List<OperationEntity> exec(OperDateEntity operDateEntity) {
        System.out.println("Исполнение операций за плановыую дату " + operDateEntity.getOperDate());
        List<OperationEntity> result = new ArrayList<>();
        var operPlanByDateList = operationService.findByPlanDateAndState(operDateEntity.getOperDate(), OperationState.LOADED); //отбираем операции по плановой дате исполнения (неисполненные и неотмененные)

        var bpOpt = businessProcessService.findBySysName(OPERATIONS_EXECUTION_SYS_NAME);
        if (bpOpt.isEmpty()) {
            throw new BusinessProcessException("No found business process by sys name: OPERATIONS_EXECUTION_SYS_NAME");
        }

        if (operPlanByDateList != null && operPlanByDateList.size() > 0) {
            List<OperationEntity> forSave = new ArrayList<>();

            operPlanByDateList.forEach(oper -> {
                oper.setActualDate(LocalDateTime.now());
                oper.setState(OperationState.EXEC);
                forSave.add(oper);
            });

            System.out.println("Исполнено " + forSave.size() + " операций");

            result = operationService.saveAll(forSave);
            result.forEach(operationStateMachine::execOperation); // раскрутка стейт машины
        }

        protocolService.saveByBusinessProcessesAndOperDate(bpOpt.get(), operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // добавим обработанный протокол
        return result;
    }
}
