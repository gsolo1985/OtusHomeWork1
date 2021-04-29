package ru.otus.library.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;
import ru.otus.library.exception.BookNotValidException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование BookServiceImpl")
class BookServiceImplTest {
    private BookService bookService;

    @Mock
    private BookDao bookDao;

    private Book warAndPeaceBook;
    private Book uncleVanyaBook;
    private Author tolstoyAuthor;
    private Author chekhovAuthor;
    private Genre dramaGenre;
    private Genre novellaGenre;

    @BeforeAll
    public void setUp() {
        tolstoyAuthor = new Author(10, "Lev Tolstoy");
        chekhovAuthor = new Author(11, "Anton Chekhov");
        dramaGenre = new Genre(10, "Drama");
        novellaGenre = new Genre(11, "Novella");

        warAndPeaceBook = new Book(10, "War and Peace", tolstoyAuthor, dramaGenre);
        uncleVanyaBook = new Book(11, "Uncle Vanya", chekhovAuthor, novellaGenre);

        Mockito.when(bookDao.getById(10L)).thenReturn(Optional.ofNullable(warAndPeaceBook));
        Mockito.when(bookDao.getByTitleAndAuthor("Uncle Vanya", chekhovAuthor)).thenReturn(Optional.ofNullable(uncleVanyaBook));
        Mockito.when(bookDao.getByAuthor(tolstoyAuthor)).thenReturn(Collections.singletonList(warAndPeaceBook));
        Mockito.when(bookDao.getAll()).thenReturn(Arrays.asList(warAndPeaceBook, uncleVanyaBook));
        Mockito.when(bookDao.getByGenre(novellaGenre)).thenReturn(Collections.singletonList(uncleVanyaBook));
    }

    @BeforeEach
    public void init() {
        bookService = new BookServiceImpl(bookDao);
    }

    @DisplayName("Возвращать книгу по id")
    @Test
    void shouldGetById() {
        Book book = bookService.getById(10).get();

        assertThat(book.getTitle()).isEqualTo("War and Peace");
    }

    @DisplayName("Возвращать книгу по наименованию и автору")
    @Test
    void getByTitleAndAuthor() {
        Book book = bookService.getByTitleAndAuthor("Uncle Vanya", chekhovAuthor).get();

        assertThat(book.getTitle()).isEqualTo("Uncle Vanya");
    }

    @DisplayName("Возвращать все книги")
    @Test
    void getAll() {
        List<Book> actualBookList = bookService.getAll();
        assertThat(actualBookList).hasSize(2).containsExactly(warAndPeaceBook, uncleVanyaBook);
    }

    @DisplayName("Возвращать все книги по автору")
    @Test
    void getByAuthor() {
        List<Book> actualBookList = bookService.getByAuthor(tolstoyAuthor);
        assertThat(actualBookList).hasSize(1).containsExactly(warAndPeaceBook);
    }

    @DisplayName("Возвращать все книги по жанру")
    @Test
    void getByGenre() {
        List<Book> actualBookList = bookService.getByGenre(novellaGenre);
        assertThat(actualBookList).hasSize(1).containsExactly(uncleVanyaBook);
    }

    @DisplayName("Возвращать ошибку при добавлении если нет жанра или автора")
    @Test
    void insert() {
        Book newBook = new Book(0, "test", null, null);

        Throwable thrown = assertThrows(BookNotValidException.class, () -> {
            bookService.insert(newBook);
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

}