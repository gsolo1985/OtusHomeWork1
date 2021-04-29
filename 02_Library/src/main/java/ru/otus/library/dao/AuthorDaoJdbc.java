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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Author> getById(long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject("select id, name from author where id = :id",
                    params, new AuthorMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> getByName(String name) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", name);

        try {
            return Optional.ofNullable(jdbc.queryForObject("select id, name from author where name = :name",
                    params, new AuthorMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from author", new AuthorMapper());
    }

    @Override
    public Author insert(Author author) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("name", author.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into author (name) values (:name);",
                paramMap, keyHolder);

        return new Author(Objects.requireNonNull(keyHolder.getKey()).longValue(),
                author.getName());
    }

    @Override
    public void delete(Author author) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", author.getId());

        jdbc.update("delete author where id = :id", paramMap);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(resultSet.getLong("id"),
                    resultSet.getString("name")
            );
        }
    }
}
