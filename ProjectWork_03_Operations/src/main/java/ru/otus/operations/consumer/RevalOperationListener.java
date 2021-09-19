package ru.otus.operations.consumer;

import java.io.IOException;

public interface RevalOperationListener {
    void consume(RevalOperationList msg) throws IOException;
}
