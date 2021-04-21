package ru.otus.studenttest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.studenttest.Application;
import ru.otus.studenttest.config.ApplicationSettings;
import ru.otus.studenttest.domain.Student;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс проведения тестирования студентов")
@ContextConfiguration(classes = Application.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class StudentServiceTestingImplTest {
    StudentServiceTestingImpl service;
    @Autowired
    private OutputQuestionsServiceImpl outputQuestionsService;
    @Autowired
    private MessageSource messageSource;

    @Mock
    private ApplicationSettings applicationSettings;

    @Test
    @DisplayName("Метод тестирования студентов")
    void shouldTakeTheTest() throws IOException {
        Mockito.when(applicationSettings.getFilePath()).thenReturn("questions.csv");
        Mockito.when(applicationSettings.getTrueAnswers()).thenReturn(4);
        Mockito.when(applicationSettings.getLocale()).thenReturn("ru_RU");

        StringBuilder strings = new StringBuilder();
        strings.append("4").append('\n');
        strings.append("6").append('\n');
        strings.append("8").append('\n');
        strings.append("10").append('\n');
        strings.append("13").append('\n');
        String data = strings.toString();


        //Оборачиваем строку в класс ByteArrayInputStream
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ReadConsoleServiceImpl readConsoleService = new ReadConsoleServiceImpl(inputStream);
        service = new StudentServiceTestingImpl(readConsoleService, outputQuestionsService, applicationSettings, messageSource);
        service.startTesting();

        assertEquals(4, service.getCorrectAnswerCount()); // 4 правильных ответов из 5
    }

    @Test
    @DisplayName("Логирование студентов")
    void shouldLoginStudent() throws IOException {
        Mockito.when(applicationSettings.getFilePath()).thenReturn("questions.csv");
        String data = "Ivan" + '\n';
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ReadConsoleServiceImpl readConsoleService = new ReadConsoleServiceImpl(inputStream);

        service = new StudentServiceTestingImpl(readConsoleService, outputQuestionsService, applicationSettings, messageSource);

        Student studentLogin = service.loginStudent();
        assertEquals("Ivan", studentLogin.getName());
    }
}