package ru.otus.operations.statemachine;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.service.OperDateService;
import ru.otus.operations.service.accounting.AccountingService;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OperationStateChangeInterceptor extends StateMachineInterceptorAdapter<OperationState, OperationEvent> {
    private final AccountingService accountingService;
    private final OperDateService operDateService;

    @Override
    public void preStateChange(State<OperationState, OperationEvent> state, Message<OperationEvent> message, Transition<OperationState, OperationEvent> transition,
                               StateMachine<OperationState, OperationEvent> stateMachine, StateMachine<OperationState, OperationEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(msg.getHeaders().getOrDefault(OperationStateMachineImpl.OPERATION_ENTITY_HEADER, null))
                    .ifPresent(oper -> {
                        if (oper instanceof OperationEntity) {
                            var operation = (OperationEntity) oper;
                            var openedDate = operDateService.getOperDay();

                            openedDate.ifPresent(operDateEntity -> accountingService.execAccountingByOperationOnDate(operation, openedDate.get().getOperDate()));
                        }
                    });
        });
    }
}
