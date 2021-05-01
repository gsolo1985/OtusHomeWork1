package ru.otus.library.repositoriy;

import org.springframework.stereotype.Repository;
import ru.otus.library.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BookComment> getById(long id) {
        return Optional.ofNullable(entityManager.find(BookComment.class, id));
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() == 0) {
            entityManager.persist(bookComment);
            return bookComment;
        }
        return entityManager.merge(bookComment);
    }

    @Override
    public void delete(BookComment bookComment) {
        entityManager.remove(bookComment);
    }
}
