package ru.otus.library.publish;

import ru.otus.library.dto.BookDto;

public interface BookGetOutPublishGateway {
    void publish(BookDto msg);
}

