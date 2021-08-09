package ru.otus.library.repositoriy;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест репозитория на основе Spring Data для работы с жанрами GenreRepository")
@DataJpaTest
class GenreRepositoryTest {

    private static final long FIRST_GENRE_ID = 1L;
    private static final String FIRST_GENRE_NAME = "Drama";
    private static final int EXPECTED_NUMBER_OF_GENRES = 4;

    private static final String GENRE_COMEDY_NAME = "Comedy";
    private static final String GENRE_NOVELLA_NAME = "Novella";

    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать ожидаемый жанр по id")
    @Test
    void shouldReturnExpectedGenreById() {
        val optionalActualGenre = repository.findById(FIRST_GENRE_ID);
        val expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);

        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Возвращать ожидаемый жанр по name")
    @Test
    void shouldFindExpectedGenreByName() {
        val optionalActualGenre = repository.findByName(FIRST_GENRE_NAME);
        val expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);

        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Сохранять всю информацию о жанре")
    @Test
    void shouldSaveGenre() {
        val comedy = new Genre(0, GENRE_COMEDY_NAME);
        repository.save(comedy);
        assertThat(comedy.getId()).isGreaterThan(0);

        val actualGenre = em.find(Genre.class, comedy.getId());
        assertThat(actualGenre).isNotNull().matches(genre -> genre.getName().equals(GENRE_COMEDY_NAME));
    }

    @DisplayName("Удалять заданный жанр")
    @Test
    void shouldDeleteGenre() {
        val firstGenre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(firstGenre).isNotNull();

        repository.delete(firstGenre);
        val deletedGenre = em.find(Genre.class, FIRST_GENRE_ID);

        assertThat(deletedGenre).isNull();
    }

    @DisplayName("Возвращать ложь/истрина при поиске по имени")
    @Test
    void shouldCheckExistsByName() {
        assertThat(repository.existsByName(GENRE_COMEDY_NAME)).isFalse();
        assertThat(repository.existsByName(GENRE_NOVELLA_NAME)).isTrue();
    }
}