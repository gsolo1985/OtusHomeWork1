package ru.otus.studenttest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.studenttest.Application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест сервиса по проведению тестирования студентов")
@ContextConfiguration(classes = Application.class)
@SpringBootTest
class StudentServiceTestingImplTest {
    StudentServiceTestingImpl service;
    @Autowired
    private OutputQuestionsServiceImpl outputQuestionsService;

    @Test
    void startTesting() throws IOException {
        StringBuilder strings = new StringBuilder();
        strings.append("Ivan").append('\n');
        strings.append("4").append('\n');
        strings.append("6").append('\n');
        strings.append("8").append('\n');
        strings.append("10").append('\n');
        strings.append("13").append('\n');

        String data = strings.toString();

        //Оборачиваем строку в класс ByteArrayInputStream
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());

        ReadConsoleServiceImpl readConsoleService = new ReadConsoleServiceImpl(inputStream);

        service = new StudentServiceTestingImpl(readConsoleService, outputQuestionsService);
        service.startTesting();

        assertEquals(4, service.getCorrectAnswerCount()); // 4 правильных ответов из 5
    }
}