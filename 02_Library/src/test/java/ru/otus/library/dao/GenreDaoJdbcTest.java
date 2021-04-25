package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({GenreDaoJdbc.class})
@DisplayName("Тестрование GenreDaoJdbc")
class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc dao;

    @DisplayName("Возвращать ожидаемый жанр по id")
    @Test
    void shouldReturnExpectedGenreById() {
        var expectedGenre = new Genre(1, "Drama");

        Optional<Genre> actualGenre = dao.getById(expectedGenre.getId());

        assertThat(actualGenre.isPresent()).isTrue();
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Возвращать ожидаемый жанр по имени")
    @Test
    void shouldReturnExpectedGenreByName() {
        var expectedGenre = new Genre(2L, "Play");

        Optional<Genre> actualGenre = dao.getByName(expectedGenre.getName());

        assertThat(actualGenre.isPresent()).isTrue();
        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Возвращать ожидаемый список жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        var genre1 = new Genre(1L, "Drama");
        var genre2 = new Genre(2L, "Play");
        var genre3 = new Genre(3L, "Novella");
        var genre4 = new Genre(4L, "Test");
        var actualGenresList = dao.getAll();

        assertThat(actualGenresList).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(genre1, genre2, genre3, genre4);
    }

    @DisplayName("Добавлять жанры")
    @Test
    void shouldInsertGenre() {
        var expectedGenre = new Genre(0L, "Comedy");
        dao.insert(expectedGenre);

        Optional<Genre> actualGenre = dao.getByName("Comedy");

        assertThat(actualGenre.isPresent()).isTrue();
        assertThat(expectedGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(actualGenre.get());
    }

    @DisplayName("Удалять заданный жанр по id")
    @Test
    void shouldDelete() {
        Genre genre = dao.insert(new Genre(0L, "Fantastic"));
        dao.delete(genre);
        assertThat(dao.getById(genre.getId()).isEmpty()).isEqualTo(true);
    }
}