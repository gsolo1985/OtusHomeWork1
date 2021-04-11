package ru.otus.studenttest.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.studenttest.config.ApplicationSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@DisplayName("Класс сервиса вывода вопросов")
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class OutputQuestionsServiceImplTest {
    @Mock
    private ApplicationSettings applicationSettings;
    @Autowired
    private MessageSource messageSource;

    private OutputQuestionsServiceImpl service;


    @BeforeEach
    public void setUp() throws IOException {
        Mockito.when(applicationSettings.getFilePath()).thenReturn("questions.csv");

        ReadCsvFileServiceImpl readCsvFileService = new ReadCsvFileServiceImpl(applicationSettings);
        service = new OutputQuestionsServiceImpl(readCsvFileService, messageSource);
    }

    @Test
    @DisplayName("Вывод правильного вопроса")
    void shouldOutputCorrectQuestion() {
        PrintStream consoleStream = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output)); //заменяю System.out

        service.outputQuestion(1);
        System.setOut(new PrintStream(output2));
        System.out.println("3+3");

        Assert.assertEquals(output2.toString(), output.toString());
        System.setOut(consoleStream); //возвращаю обратно System.out
    }

    @Test
    @DisplayName("Получение правильного ответа на вопрос")
    void shouldGetSolutionAnswer() {
        Assert.assertEquals("10", service.getSolutionAnswer(3));
    }

    @Test
    @DisplayName("Количество вопросов в файле")
    void shouldGetCountQuestions() {
        Assert.assertEquals(5, service.getCountQuestions());
    }
}