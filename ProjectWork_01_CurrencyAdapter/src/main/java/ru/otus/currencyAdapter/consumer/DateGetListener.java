package ru.otus.currencyAdapter.consumer;

import java.io.IOException;

public interface DateGetListener {
    void consume(DateMessage msg) throws IOException;
}
