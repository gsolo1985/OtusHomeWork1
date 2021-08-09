package ru.otus.library.consumer;

public interface BookMsgGetListener {
    void consume(BookGetMessage msg);
}
