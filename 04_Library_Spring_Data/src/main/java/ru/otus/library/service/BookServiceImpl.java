package ru.otus.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookNotValidException;
import ru.otus.library.repositoriy.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String INSERT_ERROR = "New book insert error: ";
    private static final String UPDATE_ERROR = "Update book error: ";
    private final BookRepository bookRepository;
    private final BookCommentService bookCommentService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getByTitleAndAuthor(String title, Author author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByAuthor(Author author) {
        if (author != null) {
            return bookRepository.findByAuthor(author);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getByGenre(Genre genre) {
        if (genre != null) {
            return bookRepository.findByGenre(genre);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Book insert(Book book) {
        if (book == null) {
            throw new BookNotValidException(INSERT_ERROR + "book isn't define.");
        }

        if (book.getAuthor() == null) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's author.");
        }

        if (book.getGenre() == null) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's genre.");
        }

        String title = book.getTitle();

        if (title == null || title.equals("")) {
            throw new BookNotValidException(INSERT_ERROR + "you must set a book's title.");
        }

        if (getByTitleAndAuthor(title, book.getAuthor()).isPresent()) {
            throw new BookNotValidException(INSERT_ERROR + "a book with this title and author already exists.");
        }

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        if (book == null || book.getId() == 0) {
            throw new BookNotValidException(UPDATE_ERROR + "book isn't define.");
        }

        if (book.getAuthor() == null) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's author.");
        }

        if (book.getGenre() == null) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's genre.");
        }

        String title = book.getTitle();

        if (title == null || title.equals("")) {
            throw new BookNotValidException(UPDATE_ERROR + "you must set a book's title.");
        }

        long currentId = book.getId();
        Optional<Book> bookOptionalByTitle = getByTitleAndAuthor(title, book.getAuthor());

        if (bookOptionalByTitle.isPresent() && bookOptionalByTitle.get().getId() != currentId) {
            throw new BookNotValidException(UPDATE_ERROR + "a book with this title and author already exists.");
        }

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
