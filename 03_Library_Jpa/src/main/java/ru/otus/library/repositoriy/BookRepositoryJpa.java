package ru.otus.library.repositoriy;

import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> getByTitle(String title) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public List<Book> getAll() {
        EntityGraph<?> authorsGraph = entityManager.getEntityGraph("authors-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b join fetch b.genre", Book.class);

        query.setHint("javax.persistence.fetchgraph", authorsGraph);
        return query.getResultList();
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b " +
                        "join fetch b.author a " +
                        "join fetch b.genre g " +
                        "where a.id = :authorId", Book.class);
        query.setParameter("authorId", author.getId());
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b " +
                        "join fetch b.author a " +
                        "join fetch b.genre g " +
                        "where g.id = :genreId", Book.class);
        query.setParameter("genreId", genre.getId());
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }
}
