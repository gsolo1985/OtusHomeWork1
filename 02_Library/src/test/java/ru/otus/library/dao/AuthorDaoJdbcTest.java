package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({AuthorDaoJdbc.class})
@DisplayName("Тестрование AuthorDaoJdbc")
class AuthorDaoJdbcTest {
    @Autowired
    private AuthorDaoJdbc dao;

    @DisplayName("Возвращать ожидаемого автора по id")
    @Test
    void shouldReturnExpectedAuthorById() {
        var expectedAuthor = new Author(1, "Fedor Dostaevskiy");

        Optional<Author> actualAuthor = dao.getById(expectedAuthor.getId());

        assertThat(actualAuthor.isPresent()).isTrue();
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать ожидаемого автора по имени")
    @Test
    void shouldReturnExpectedAuthorByName() {
        var expectedAuthor = new Author(2L, "Lev Tolstoy");

        Optional<Author> actualAuthor = dao.getByName(expectedAuthor.getName());

        assertThat(actualAuthor.isPresent()).isTrue();
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorsList() {
        var author1 = new Author(1L, "Fedor Dostaevskiy");
        var author2 = new Author(2L, "Lev Tolstoy");
        var author3 = new Author(3L, "Anton Chekhov");
        var author4 = new Author(4L, "Test author");
        var actualAuthorsList = dao.getAll();

        assertThat(actualAuthorsList).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(author1, author2, author3, author4);
     }

    @DisplayName("Добавлять авторов")
    @Test
    void shouldInsertAuthor() {
        var expectedAuthor = new Author(0L, "Vladimir Mayakovskiy");
        dao.insert(expectedAuthor);

        Optional<Author> actualAuthor = dao.getByName("Vladimir Mayakovskiy");

        assertThat(actualAuthor.isPresent()).isTrue();
        assertThat(expectedAuthor).usingRecursiveComparison().ignoringFields("id").isEqualTo(actualAuthor.get());
    }

    @DisplayName("Удалять заданного автора по id")
    @Test
    void shouldDelete() {
        Author author = dao.insert(new Author(0L, "Vladimir Nabokov"));
        dao.delete(author);
        assertThat(dao.getById(author.getId()).isEmpty()).isEqualTo(true);
    }
}