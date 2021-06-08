package ru.otus.library.dto.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.BookCommentDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.GenreDto;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Маппинг dto-объектов")
@SpringBootTest
class DtoMapperImplTest {
    @Autowired
    DtoMapper dtoMapper;

    @Test
    @DisplayName("Маппинг книги в dto-объект")
    void bookToDto() {
        Author author = Author.builder().id(1L).name("Author").build();
        Genre genre = Genre.builder().id(1L).name("Genre").build();
        Book book = Book.builder().id(1L).genre(genre).author(author).title("Book").build();

        BookComment comment1 = BookComment.builder().id(1L).book(book).comment("comment1").build();
        BookComment comment2 = BookComment.builder().id(1L).book(book).comment("comment1").build();

        book.setComments(Arrays.asList(comment1, comment2));

        BookDto bookDto = dtoMapper.bookToDto(book);

        assertThat(bookDto.getId()).isEqualTo(1L);
        assertThat(bookDto.getTitle()).isEqualTo("Book");
        assertThat(bookDto.getAuthor().getName()).isEqualTo("Author");
        assertThat(bookDto.getGenre().getName()).isEqualTo("Genre");
        assertThat(bookDto.getComments()).hasSize(2);
    }

    @Test
    @DisplayName("Маппинг dto-объект в книгу")
    void bookDtoToBook() {
        AuthorDto authorDto = AuthorDto.builder().id(1L).name("Author").build();
        GenreDto genreDto = GenreDto.builder().id(1L).name("Genre").build();
        BookDto bookDto = BookDto.builder().id(1L).genre(genreDto).author(authorDto).title("Book").build();

        BookCommentDto commentDto1 = BookCommentDto.builder().id(1L).comment("commentDto1").build();
        BookCommentDto commentDto2 = BookCommentDto.builder().id(1L).comment("commentDto2").build();

        bookDto.setComments(Arrays.asList(commentDto1, commentDto2));

        Book book = dtoMapper.bookDtoToBook(bookDto);

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Book");
        assertThat(book.getAuthor().getName()).isEqualTo("Author");
        assertThat(book.getGenre().getName()).isEqualTo("Genre");
        assertThat(book.getComments()).hasSize(2);
    }
}