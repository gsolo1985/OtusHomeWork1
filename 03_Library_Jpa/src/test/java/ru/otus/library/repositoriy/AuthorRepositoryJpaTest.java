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

@DisplayName("Тест репозитория на основе Jpa для работы со авторами AuthorRepositoryJpa")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    private static final long FIRST_AUTHOR_ID = 1L;
    private static final String FIRST_AUTHOR_NAME = "Fedor Dostaevskiy";
    private static final int EXPECTED_NUMBER_OF_AUTHORS = 4;

    private static final String AUTHOR_KRYLOV_NAME = "Ivan Krylov";

    @Autowired
    private AuthorRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Возвращать ожидаемого автора по id")
    @Test
    void shouldReturnExpectedAuthorById() {
        val optionalActualAuthor = repositoryJpa.getById(FIRST_AUTHOR_ID);
        val expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать ожидаемого автора по name")
    @Test
    void shouldFindExpectedAuthorByName() {
        val optionalActualAuthor = repositoryJpa.getByName(FIRST_AUTHOR_NAME);
        val expectedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Возвращать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        val authors = repositoryJpa.getAll();

        assertThat(authors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(s -> !s.getName().equals(""));
    }

    @DisplayName("Сохранять всю информацию об авторе")
    @Test
    void shouldSaveAuthor() {
        val krylov = new Author(0, AUTHOR_KRYLOV_NAME);
        repositoryJpa.save(krylov);
        assertThat(krylov.getId()).isGreaterThan(0);

        val actualAuthor = em.find(Author.class, krylov.getId());
        assertThat(actualAuthor).isNotNull().matches(author -> author.getName().equals(AUTHOR_KRYLOV_NAME));
    }

    @DisplayName("Удалять заданного автора")
    @Test
    void shouldDeleteAuthor() {
        val firstAuthor = em.find(Author.class, FIRST_AUTHOR_ID);
        assertThat(firstAuthor).isNotNull();

        repositoryJpa.delete(firstAuthor);
        val deletedAuthor = em.find(Author.class, FIRST_AUTHOR_ID);

        assertThat(deletedAuthor).isNull();
    }
}