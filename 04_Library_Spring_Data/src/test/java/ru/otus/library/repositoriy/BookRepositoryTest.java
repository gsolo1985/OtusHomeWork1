package ru.otus.library.repositoriy;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест репозитория на основе Spring Data для работы со книгами BookRepository")
@DataJpaTest
class BookRepositoryTest {

    private static final int EXPECTED_NUMBER_OF_ALL_BOOKS = 5;
    private static final int EXPECTED_NUMBER_OF_TOLSTOY_BOOKS = 2;
    private static final int EXPECTED_NUMBER_OF_DRAMA_BOOKS = 2;

    private static final long FIRST_BOOK_ID = 1L;
    private static final String FIRST_BOOK_TITLE = "War and Peace";
    private static final String NEW_BOOK_NAME = "New Book";

    private static final String NEW_AUTHOR_NAME = "New Author";
    private static final String TOLSTOY_AUTHOR_NAME = "Lev Tolstoy";
    private static final long TOLSTOY_AUTHOR_ID = 2L;

    private static final String NEW_GENRE_NAME = "New Genre";
    private static final String DRAMA_GENRE_NAME = "Drama";
    private static final long DRAMA_GENRE_ID = 1L;

    @Autowired
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать ожидаемую книгу по id")
    @Test
    void shouldReturnExpectedBookById() {
        val optionalActualBook = repository.findById(FIRST_BOOK_ID);
        val expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(optionalActualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Возвращать ожидаемую книгу по title")
    @Test
    void shouldFindExpectedBookByName() {
        val books = repository.findByTitle(FIRST_BOOK_TITLE);
        val expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(books).containsOnlyOnce(expectedBook);
    }

    @DisplayName("Возвращать все книги")
    @Test
    void shouldReturnAllBooks() {
        val books = repository.findAll();

        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_ALL_BOOKS)
                .allMatch(book -> !book.getTitle().equals(""))
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getGenre() != null);
    }

    @DisplayName("Возвращать книги по автору")
    @Test
    void shouldReturnAllBooksByAuthor() {
        val author = Author.builder().id(TOLSTOY_AUTHOR_ID).name(TOLSTOY_AUTHOR_NAME).build();

        val books = repository.findByAuthor(author);
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_TOLSTOY_BOOKS)
                .allMatch(book -> book.getGenre() != null)
                .allMatch(book -> book.getAuthor() != null && book.getAuthor().getName().equals(TOLSTOY_AUTHOR_NAME));
    }

    @DisplayName("Возвращать книги по жанру")
    @Test
    void shouldReturnAllBooksByGenre() {
        val genre = Genre.builder().id(DRAMA_GENRE_ID).name(DRAMA_GENRE_NAME).build();

        val books = repository.findByGenre(genre);
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_DRAMA_BOOKS)
                .allMatch(book -> book.getAuthor() != null)
                .allMatch(book -> book.getGenre() != null && book.getGenre().getName().equals(DRAMA_GENRE_NAME));
    }

    @DisplayName("Сохранять всю информацию о книге")
    @Test
    void shouldSaveBook() {
        val author = Author.builder().id(0).name(NEW_AUTHOR_NAME).build();
        val genre = Genre.builder().id(0).name(NEW_GENRE_NAME).build();
        val newBook = Book.builder().id(0).title(NEW_BOOK_NAME).author(author).genre(genre).build();

        repository.save(newBook);
        assertThat(newBook.getId()).isGreaterThan(0);

        val actualBook = em.find(Book.class, newBook.getId());
        assertThat(actualBook).isNotNull().matches(b -> b.getTitle().equals(NEW_BOOK_NAME))
                .matches(b -> b.getAuthor() != null && b.getAuthor().getId() > 0 && b.getAuthor().getName().equals(NEW_AUTHOR_NAME))
                .matches(b -> b.getGenre() != null && b.getGenre().getId() > 0 && b.getGenre().getName().equals(NEW_GENRE_NAME));
    }

    @DisplayName("Удалять книгу")
    @Test
    void shouldDeleteBook() {
        val firstBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(firstBook).isNotNull();

        repository.delete(firstBook);
        val deletedBook = em.find(Book.class, FIRST_BOOK_ID);

        assertThat(deletedBook).isNull();
    }
}