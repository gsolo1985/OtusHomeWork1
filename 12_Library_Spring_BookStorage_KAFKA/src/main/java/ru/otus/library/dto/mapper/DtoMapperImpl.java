package ru.otus.library.dto.mapper;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.BookComment;
import ru.otus.library.domain.Genre;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.dto.BookCommentDto;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.GenreDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DtoMapperImpl implements DtoMapper {
    private final ModelMapper modelMapper;

    @Override
    public BookDto bookToDto(Book book) {
        var bookDto = modelMapper.map(book, BookDto.class);

        bookDto.setAuthor(authorToDto(book.getAuthor()));
        bookDto.setGenre(genreToDto(book.getGenre()));
        bookDto.setComments(commentsToDto(book.getComments()).orElse(null));

        return bookDto;
    }

    @Override
    public BookCommentDto bookCommentToDto(BookComment bookComment) {
        return modelMapper.map(bookComment, BookCommentDto.class);
    }

    @Override
    public AuthorDto authorToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    @Override
    public GenreDto genreToDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }

    @Override
    public Book bookDtoToBook(BookDto bookDto) {
        var book = modelMapper.map(bookDto, Book.class);

        book.setAuthor(authorDtoToAuthor(bookDto.getAuthor()));
        book.setGenre(genreDtoToGenre(bookDto.getGenre()));
        book.setComments(commentsDtoToComment(bookDto.getComments(), book).orElse(null));

        return book;
    }

    @Override
    public BookComment bookCommentDtoToBookComment(BookCommentDto bookCommentDto, Book book) {
        var bookComment = modelMapper.map(bookCommentDto, BookComment.class);
        bookComment.setBook(book);
        return bookComment;
    }

    @Override
    public Author authorDtoToAuthor(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    @Override
    public Genre genreDtoToGenre(GenreDto genreDto) {
        return modelMapper.map(genreDto, Genre.class);
    }

    private Optional<List<BookCommentDto>> commentsToDto(List<BookComment> comments) {
        if (comments != null) {
            List<BookCommentDto> commentDtoList = new ArrayList<>();
            comments.forEach(comment -> commentDtoList.add(bookCommentToDto(comment)));
            return Optional.of(commentDtoList);
        }
        else return Optional.empty();
    }

    private Optional<List<BookComment>> commentsDtoToComment(List<BookCommentDto> commentDtoList, Book book) {
        if (commentDtoList != null) {
            List<BookComment> comments = new ArrayList<>();
            commentDtoList.forEach(commentDto -> comments.add(bookCommentDtoToBookComment(commentDto, book)));
            return Optional.of(comments);
        }
        else return Optional.empty();
    }
}
