package ru.otus.library.repositoriy;

import ru.otus.library.domain.BookComment;

import java.util.Optional;

public interface BookCommentRepository {
    Optional<BookComment> getById(long id);

    BookComment save (BookComment bookComment);

    void delete (BookComment bookComment);
}
