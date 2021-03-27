package ru.otus.studenttest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ReadCsvFileImplTest {

    private ReadCsvFileServiceImpl service;

    @BeforeEach
    void setUp() {
        String fileName = "questions.csv"; //не знаю насколько корректно так делать для теста
        service = new ReadCsvFileServiceImpl(fileName);
    }

    @Test
    @DisplayName("Тестовый тест")
    void outputQuestions() throws IOException {
        service.outputQuestions();                       // не знаю как протестить, пусть будет просто что не падает с ошибкой
    }
}