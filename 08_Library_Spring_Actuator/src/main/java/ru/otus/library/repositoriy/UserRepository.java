package ru.otus.library.repositoriy;

import org.springframework.data.repository.CrudRepository;
import ru.otus.library.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
