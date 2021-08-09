package ru.otus.library.repositoriy;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
    @EntityGraph("author-genre-entity-graph")
    List<Book> findByTitle(String title);

    @Override
    @EntityGraph("author-genre-entity-graph")
    List<Book> findAll();

    @EntityGraph("author-genre-entity-graph")
    List<Book> findByAuthor(Author author);

    @EntityGraph("author-genre-entity-graph")
    List<Book> findByGenre (Genre genre);

    @EntityGraph("author-genre-entity-graph")
    Optional<Book> findByTitleAndAuthor(String title, Author author);
}
