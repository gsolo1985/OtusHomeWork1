package ru.otus.library.service;

import ru.otus.library.domain.BookComment;

import java.util.Optional;

public interface BookCommentService {
    BookComment save (BookComment bookComment);

    void delete (BookComment bookComment);

    Optional<BookComment> getById(long id);
}
