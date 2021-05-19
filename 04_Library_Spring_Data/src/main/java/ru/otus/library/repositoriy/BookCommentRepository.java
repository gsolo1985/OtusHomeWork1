package ru.otus.library.repositoriy;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.BookComment;

import java.util.Optional;

public interface BookCommentRepository extends CrudRepository<BookComment, Long> {
}
