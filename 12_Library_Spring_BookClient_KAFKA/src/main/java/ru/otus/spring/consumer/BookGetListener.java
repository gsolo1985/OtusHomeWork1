package ru.otus.spring.consumer;

import ru.otus.spring.dto.BookDto;

public interface BookGetListener {
    void consume(BookDto msg);
}
