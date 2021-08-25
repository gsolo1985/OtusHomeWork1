package ru.otus.operations.service.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.service.BusinessProcessByOperDateService;
import ru.otus.operations.service.OperationService;
import ru.otus.operations.statemachine.OperationState;
import ru.otus.operations.statemachine.OperationStateMachine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED;
import static ru.otus.operations.constants.BusinessProcessConstants.OPERATIONS_EXECUTION_SYS_NAME;

@Service
@RequiredArgsConstructor
public class ExecOperationServiceImpl implements ExecOperationService {
    private final OperationService operationService;
    private final BusinessProcessByOperDateService businessProcessByOperDateService;
    private final OperationStateMachine operationStateMachine;

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

        if (operPlanByDateList != null && operPlanByDateList.size() > 0) {
            operPlanByDateList.forEach(oper -> {
                oper.setActualDate(LocalDateTime.now());
                oper.setState(OperationState.EXEC);
                result.add(oper);
            });

            System.out.println("Исполнено " + result.size() + " операций");

            result.forEach(operationStateMachine::execOperation); // раскрутка стейт машины

            businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(operDateEntity, OPERATIONS_EXECUTION_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
            return operationService.saveAll(result);
        }
        businessProcessByOperDateService.setBusinessProcessesByOperDateStatus(operDateEntity, OPERATIONS_EXECUTION_SYS_NAME, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED); // пометим бизнес-процесс как обработанный
        return result;
    }
}
