package ru.otus.library.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.library.repositoriy.BookRepository;

@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator{
    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long count = bookRepository.count();
        if (count == 0) {
            return Health.status(Status.DOWN).build();
        } else {
            return Health.status(Status.UP).build();
        }
    }
}