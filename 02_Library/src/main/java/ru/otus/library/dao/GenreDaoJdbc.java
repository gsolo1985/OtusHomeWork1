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
import ru.otus.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Genre> getById(long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject("select id, name from genre where id = :id",
                    params, new GenreDaoJdbc.GenreMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> getByName(String name) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", name);

        try {
            return Optional.ofNullable(jdbc.queryForObject("select id, name from genre where name = :name",
                    params, new GenreDaoJdbc.GenreMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, name from genre", new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public Genre insert(Genre genre) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("name", genre.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into genre (name) values (:name);",
                paramMap, keyHolder);

        return new Genre(Objects.requireNonNull(keyHolder.getKey()).longValue(),
                genre.getName());
    }

    @Override
    public void delete(Genre genre) {
        SqlParameterSource paramMap = new MapSqlParameterSource()
                .addValue("id", genre.getId());

        jdbc.update("delete genre where id = :id", paramMap);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong("id"),
                    resultSet.getString("name")
            );
        }
    }
}
