package ru.otus.library.service;

import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    BookComment save (BookComment bookComment);

    void delete (BookComment bookComment);

    List<BookComment> getByBook (Book book);

    Optional<BookComment> getById(long id);
}
