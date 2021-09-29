package ru.otus.operations.statemachine;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.service.OperationService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperationStateMachineImpl implements OperationStateMachine {
    private final OperationService operationService;
    private final StateMachineFactory<OperationState, OperationEvent> stateMachineFactory;
    private final OperationStateChangeInterceptor operationStateChangeInterceptor;

    public static final String OPERATION_ENTITY_HEADER = "operationEntity";

    @Override
    public void loadOperation(OperationEntity operationEntity) {
        if (Optional.ofNullable(operationEntity).isEmpty())
            return;

        operationEntity.setState(OperationState.LOADED);
        StateMachine<OperationState, OperationEvent> stateMachine = build(operationEntity.getOperationId());

        if (stateMachine != null && operationEntity.getState() != null)
            sendEvent(operationEntity, stateMachine, OperationEvent.LOAD);
    }

    @Override
    public void cancelOperation(OperationEntity operationEntity) {
        if (Optional.ofNullable(operationEntity).isEmpty())
            return;

        operationEntity.setState(OperationState.CANCELED);
        StateMachine<OperationState, OperationEvent> stateMachine = build(operationEntity.getOperationId());

        if (stateMachine != null && operationEntity.getState() != null)
            sendEvent(operationEntity, stateMachine, OperationEvent.CANCEL);
    }

    @Override
    public void execOperation(OperationEntity operationEntity) {
        if (Optional.ofNullable(operationEntity).isEmpty())
            return;

        operationEntity.setState(OperationState.EXEC);
        StateMachine<OperationState, OperationEvent> stateMachine = build(operationEntity.getOperationId());

        if (stateMachine != null && operationEntity.getState() != null)
            sendEvent(operationEntity, stateMachine, OperationEvent.EXECUTE);
    }

    public StateMachine<OperationState, OperationEvent> build(Long operationId) {
        var operation = operationService.findById(operationId);

        if (operation.isPresent()) {
            StateMachine<OperationState, OperationEvent> stateMachine = stateMachineFactory.getStateMachine(Long.toString(operation.get().getOperationId()));

            stateMachine.stop();
            stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(sma -> {
                        sma.addStateMachineInterceptor(operationStateChangeInterceptor); //подписчик события, сюда можно добавить любую логику
                        sma.resetStateMachine(new DefaultStateMachineContext<>(operation.get().getState(), null, null, null));
                    });
            stateMachine.start();

            return stateMachine;
        }
        return stateMachineFactory.getStateMachine();
    }

    private void sendEvent(OperationEntity operation, StateMachine<OperationState, OperationEvent> stateMachine, OperationEvent event) {
        Message<OperationEvent> msg = MessageBuilder.withPayload(event)
                .setHeader(OPERATION_ENTITY_HEADER, operation)
                .build();

        stateMachine.sendEvent(msg);
    }
}
