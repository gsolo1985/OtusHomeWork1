package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({BookDaoJdbc.class})
@DisplayName("Тестрование BookDaoJdbc")
class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc dao;

    @DisplayName("Возвращать ожидаемую книгу по id")
    @Test
    void shouldReturnExpectedBookById() {
        var expectedBook = new Book(1, "War and Peace", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama"));

        Optional<Book> actualBook = dao.getById(expectedBook.getId());

        assertThat(actualBook.isPresent()).isTrue();
        assertThat(actualBook.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Возвращать ожидаемую книгу по названию и автору")
    @Test
    void shouldReturnExpectedBookByTitleAndGenre() {
        var expectedBook = new Book(1, "War and Peace", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama"));

        Optional<Book> actualBook = dao.getByTitleAndAuthor(expectedBook.getTitle(), expectedBook.getAuthor());

        assertThat(actualBook.isPresent()).isTrue();
        assertThat(actualBook.get()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBookList() {
        var book1 = new Book(1, "War and Peace", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama"));
        var book2 = new Book(2, "Crime and Punishment", new Author(1, "Fedor Dostaevskiy"), new Genre(1, "Drama"));
        var book3 = new Book(3, "Uncle Vanya", new Author(3, "Anton Chekhov"), new Genre(2, "Play"));
        var book4 = new Book(4, "Youth", new Author(2, "Lev Tolstoy"), new Genre(3, "Novella"));
        var book5 = new Book(5, "TestTitle", new Author(4, "Test author"), new Genre(4, "Test"));
        var actualBooksList = dao.getAll();

        assertThat(actualBooksList).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(book1, book2, book3, book4, book5);
    }

    @DisplayName("Возвращать список книг нужного автора")
    @Test
    void shouldReturnExpectedBookListByAuthor() {
        var book1 = new Book(1, "War and Peace", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama"));
        var book2 = new Book(4, "Youth", new Author(2, "Lev Tolstoy"), new Genre(3, "Novella"));

        var actualBooksList = dao.getByAuthor(new Author(2, "Lev Tolstoy"));

        assertThat(actualBooksList).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(book1, book2);
    }

    @DisplayName("Возвращать список книг нужного жанра")
    @Test
    void shouldReturnExpectedBookListByGenre() {
        var book1 = new Book(1, "War and Peace", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama"));
        var book2 = new Book(2, "Crime and Punishment", new Author(1, "Fedor Dostaevskiy"), new Genre(1, "Drama"));

        var actualBooksList = dao.getByGenre(new Genre(1, "Drama"));

        assertThat(actualBooksList).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(book1, book2);
    }

    @DisplayName("Добавлять книги")
    @Test
    void shouldInsertBooks() {
        Author author = new Author(4, "Test author");
        var expectedBook = new Book(0L, "1984", author, new Genre(1, "Drama"));
        dao.insert(expectedBook);

        Optional<Book> actualBook = dao.getByTitleAndAuthor("1984", author);

        assertThat(actualBook.isPresent()).isTrue();
        assertThat(expectedBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(actualBook.get());
    }

    @DisplayName("Изменять книги")
    @Test
    void shouldUpdateBooks() {
        Author author = new Author(2, "Lev Tolstoy");
        var warAndPeaceBookStart = dao.getByTitleAndAuthor("War and Peace", author);
        long id = warAndPeaceBookStart.get().getId();
        var expectedBook = new Book(id, "New title", author, new Genre(4, "Test"));

        dao.update(expectedBook);

        assertThat(expectedBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(dao.getById(id).get());
    }

    @DisplayName("удалять книги")
    @Test
    void shouldDelete() {
        Book book = dao.insert(new Book(0L, "Anna Karenina", new Author(2, "Lev Tolstoy"), new Genre(1, "Drama")));
        dao.delete(book);
        assertThat(dao.getById(book.getId()).isEmpty()).isEqualTo(true);
    }
}