package ru.otus.reval.publish;

import ru.otus.reval.model.RevalOperationList;

public interface CalcRevalOutPublishGateway {
    void publish(RevalOperationList msg);
}

