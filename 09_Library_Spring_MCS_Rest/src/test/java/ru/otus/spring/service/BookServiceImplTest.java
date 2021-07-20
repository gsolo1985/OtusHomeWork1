package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.dto.Book;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Тестрование BookServiceImpl")
@ExtendWith(MockitoExtension.class)

@AutoConfigureMockMvc
class BookServiceImplTest {
    private BookService service;

    @Mock
    RestTemplate rest;

    @Value("${rest-template.url.getBook}")
    private String url;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(rest);
    }

    @DisplayName("Возвращать книгу")
    @Test
    void getBook() {
        Mockito.when(rest.getForObject(url + "1", Book.class)).thenReturn(new Book());
        var book = service.getBook(1L);
        verify(rest, times(1)).getForObject(url + "1", Book.class);
    }
}