package ru.otus.library.dto.mapper;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.BookCommentDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.GenreDto;

public interface DtoMapper {
    BookDto bookToDto(Book book);
    BookCommentDto bookCommentToDto(BookComment bookComment);
    AuthorDto authorToDto(Author author);
    GenreDto genreToDto(Genre genre);

    Book bookDtoToBook (BookDto bookDto);
    BookComment bookCommentDtoToBookComment(BookCommentDto bookCommentDto, Book book);
    Author authorDtoToAuthor(AuthorDto authorDto);
    Genre genreDtoToGenre(GenreDto genreDto);
}
