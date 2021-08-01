package ru.otus.library.service;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> getById(long id);

    Optional<Book> getByTitleAndAuthor(String title, Author author);

    List<Book> getAll();

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre (Genre genre);

    Book insert (Book book);

    Book update (Book book);

    void delete (Book book);
}
