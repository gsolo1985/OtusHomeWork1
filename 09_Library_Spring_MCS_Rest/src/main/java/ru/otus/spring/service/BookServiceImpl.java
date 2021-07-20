package ru.otus.spring.service;

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
    @Value("${rest-template.url.getBook}")
    private String url;

    @Cacheable("books")
    @Override
    public Book getBook(long id) {
        return restTemplate.getForObject(url + String.valueOf(id), Book.class);
    }
}
