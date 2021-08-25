package ru.otus.operations.statemachine;

import ru.otus.operations.domain.OperationEntity;

public interface OperationStateMachine {
    void loadOperation(OperationEntity operationEntity);

    void cancelOperation(OperationEntity operationEntity);

    void execOperation(OperationEntity operationEntity);
}
