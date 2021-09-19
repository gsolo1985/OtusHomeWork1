package ru.otus.spring.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.dto.Book;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final RestTemplate restTemplate;
    private static final String MAIN_SERVICE = "mainService";

    @Value("${rest-template.url.getBook}")
    private String url;

    @Cacheable("books")
    @Override
    @CircuitBreaker(name = MAIN_SERVICE, fallbackMethod = "getBookFallBack")
    public Book getBook(long id) {
        return restTemplate.getForObject(url + String.valueOf(id), Book.class);
    }

    private Book getBookFallBack(long id, Exception e) {
        return new Book(id, "N/A", null, null, null);
    }
}
