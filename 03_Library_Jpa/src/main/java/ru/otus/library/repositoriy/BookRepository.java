package ru.otus.library.repositoriy;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> getById(long id);

    List<Book> getByTitle(String title);

    List<Book> getAll();

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre (Genre genre);

    Book save (Book book);

    void delete (Book book);
}
