package ru.otus.library.repositoriy;

import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Optional<Author> getByName(String name) {
        TypedQuery<Author> query = entityManager.createQuery(
                "select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        }
        catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = entityManager.createQuery(
                "select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return author;
        }
        return entityManager.merge(author);
    }

    @Override
    public void delete(Author author) {
        entityManager.remove(author);
    }
}
