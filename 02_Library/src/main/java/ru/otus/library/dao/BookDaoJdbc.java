package ru.otus.library.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private final String MAIN_QUERY_SELECT = "select b.id      as id, " +
                                                    "b.title as title, " +
                                                    "a.id     as authorId, " +
                                                    "a.name   as authorName, " +
                                                    "g.id     as genreId, " +
                                                    "g.name   as genreName, " +
                                               "from book b " +
                                               "join author a " +
                                               "  on a.id = b.authorid " +
                                               "join genre g " +
                                               "  on g.id = b.genreid ";

    @Override
    public Optional<Book> getById(long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject(MAIN_QUERY_SELECT + "where b.id = :id",
                    params, new BookDaoJdbc.BookMapper()));


        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> getByTitleAndAuthor(String title, Author author) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("authorId", author.getId());

        try {
            return Optional.ofNullable(jdbc.queryForObject(MAIN_QUERY_SELECT + "where b.title = :title and b.authorId = :authorId",
                    params, new BookDaoJdbc.BookMapper()));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query(MAIN_QUERY_SELECT, new BookDaoJdbc.BookMapper());
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        if (author != null) {
            SqlParameterSource params = new MapSqlParameterSource().addValue("authorId", author.getId());
            return jdbc.query(MAIN_QUERY_SELECT + "where b.authorId = :authorId",
                    params, new BookDaoJdbc.BookMapper());
        }
        else
            return new ArrayList<>();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        if (genre != null) {
            SqlParameterSource params = new MapSqlParameterSource().addValue("genreId", genre.getId());
            return jdbc.query(MAIN_QUERY_SELECT + "where b.genreId = :genreId",
                    params, new BookDaoJdbc.BookMapper());
        }
        else
            return new ArrayList<>();
    }

    @Override
    public Book insert(Book book) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into book (title, authorId, genreId) values (:title, :authorId, :genreId);",
                paramMap, keyHolder);

        return new Book(Objects.requireNonNull(keyHolder.getKey()).longValue(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre());
    }

    @Override
    public Book update(Book book) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());

        jdbc.update("update book " +
                        " set title = :title, " +
                        "     authorId = :authorId, " +
                        "     genreId  = :genreId " +
                        "where id = :id;",
                paramMap);

        return book;
    }

    @Override
    public void delete(Book book) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", book.getId());

        jdbc.update("delete book where id = :id", paramMap);
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Author author = new Author(resultSet.getLong("authorId"),resultSet.getString("authorName"));
            Genre genre = new Genre(resultSet.getLong("genreId"),resultSet.getString("genreName"));

            return new Book(resultSet.getLong("id"),
                    resultSet.getString("title"),
                    author,
                    genre);
        }
    }
}
