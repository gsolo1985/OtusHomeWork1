package ru.otus.library.repositoriy;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест репозитория на основе Spring Data для работы со авторами AuthorRepository")
@DataJpaTest
class AuthorRepositoryTest {

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final String FIRST_AUTHOR_NAME = "Fedor Dostaevskiy";
    private static final int EXPECTED_NUMBER_OF_AUTHORS = 4;

    private static final String AUTHOR_KRYLOV_NAME = "Ivan Krylov";
    private static final String AUTHOR_TOLSTOY_NAME = "Lev Tolstoy";

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать ожидаемого автора по id")
    @Test
    void shouldReturnExpectedAuthorById() {
        val optionalActualAuthor = repository.findById(FIRST_AUTHOR_ID);
        val expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать ожидаемого автора по name")
    @Test
    void shouldFindExpectedAuthorByName() {
        val optionalActualAuthor = repository.findByName(FIRST_AUTHOR_NAME);
        val expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        val authors = repository.findAll();

        assertThat(authors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(s -> !s.getName().equals(""));
    }

    @DisplayName("Сохранять всю информацию об авторе")
    @Test
    void shouldSaveAuthor() {
        val krylov = new Author(0, AUTHOR_KRYLOV_NAME);
        repository.save(krylov);
        assertThat(krylov.getId()).isGreaterThan(0);

        val actualAuthor = em.find(Author.class, krylov.getId());
        assertThat(actualAuthor).isNotNull().matches(author -> author.getName().equals(AUTHOR_KRYLOV_NAME));
    }

    @DisplayName("Удалять заданного автора")
    @Test
    void shouldDeleteAuthor() {
        val firstAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(firstAuthor).isNotNull();

        repository.delete(firstAuthor);
        val deletedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

        assertThat(deletedAuthor).isNull();
    }

    @DisplayName("Возвращать ложь/истрина при поиске по имени")
    @Test
    void shouldCheckExistsByName() {
        assertThat(repository.existsByName(AUTHOR_KRYLOV_NAME)).isFalse();
        assertThat(repository.existsByName(AUTHOR_TOLSTOY_NAME)).isTrue();
    }
}