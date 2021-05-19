package ru.otus.library.repositoriy;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест репозитория на основе Jpa для работы с жанрами GenreRepositoryJpa")
@DataJpaTest
@Import({GenreRepositoryJpa.class, BookRepositoryJpa.class})
class GenreRepositoryJpaTest {

    private static final long FIRST_GENRE_ID = 1L;
    private static final String FIRST_GENRE_NAME = "Drama";
    private static final int EXPECTED_NUMBER_OF_GENRES = 4;

    private static final String GENRE_COMEDY_NAME = "Comedy";

    @Autowired
    private GenreRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать ожидаемый жанр по id")
    @Test
    void shouldReturnExpectedGenreById() {
        val optionalActualGenre = repositoryJpa.getById(FIRST_GENRE_ID);
        val expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Возвращать ожидаемый жанр по name")
    @Test
    void shouldFindExpectedGenreByName() {
        val optionalActualGenre = repositoryJpa.getByName(FIRST_GENRE_NAME);
        val expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Возвращать все жанры")
    @Test
    void shouldReturnAllGenres() {
        val genres = repositoryJpa.getAll();

        assertThat(genres).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
                .allMatch(s -> !s.getName().equals(""));
    }

    @DisplayName("Сохранять всю информацию о жанре")
    @Test
    void shouldSaveGenre() {
        val comedy = new Genre(0, GENRE_COMEDY_NAME);
        repositoryJpa.save(comedy);
        assertThat(comedy.getId()).isGreaterThan(0);

        val actualGenre = em.find(Genre.class, comedy.getId());
        assertThat(actualGenre).isNotNull().matches(genre -> genre.getName().equals(GENRE_COMEDY_NAME));
    }

    @DisplayName("Удалять заданный жанр")
    @Test
    void shouldDeleteGenre() {
        val firstGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(firstGenre).isNotNull();

        repositoryJpa.delete(firstGenre);
        val deletedGenre = em.find(Genre.class, FIRST_GENRE_ID);

        assertThat(deletedGenre).isNull();
    }
}