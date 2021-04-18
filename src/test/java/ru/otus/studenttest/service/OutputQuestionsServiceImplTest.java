package ru.otus.studenttest.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.studenttest.config.ApplicationSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@DisplayName("Класс сервиса вывода вопросов")
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OutputQuestionsServiceImplTest {
    @Mock
    private ApplicationSettings applicationSettings;
    @Mock
    private MessageSource messageSource;

    private OutputQuestionsServiceImpl service;

    @BeforeAll
    public void setUp() throws Exception {
        Mockito.when(applicationSettings.getFilePath()).thenReturn("questions.csv");

        Mockito.when(messageSource.getMessage("unit.question01", null, Locale.getDefault())).thenReturn("2+2");
        Mockito.when(messageSource.getMessage("unit.answer01", null, Locale.getDefault())).thenReturn("4");

        ReadCsvFileServiceImpl readCsvFileService = new ReadCsvFileServiceImpl(applicationSettings);
        service = new OutputQuestionsServiceImpl(readCsvFileService, messageSource);

        Field questionList = OutputQuestionsServiceImpl.class.getDeclaredField("questionList");
        questionList.setAccessible(true);

        // кладем тестовые данные в поле с вопросами
        List<String[]> questionsTest = Collections.singletonList(new String[]{"unit.question01", "unit.answer01"});
        questionList.set(service, questionsTest);
    }

    @Test
    @DisplayName("Вывод правильного вопроса")
    void shouldOutputCorrectQuestion() throws Exception {
        PrintStream consoleStream = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output)); //заменяю System.out

        service.outputQuestion(0);
        System.setOut(new PrintStream(output2));
        System.out.println("2+2");

        Assert.assertEquals(output2.toString(), output.toString());
        System.setOut(consoleStream); //возвращаю обратно System.out
    }

    @Test
    @DisplayName("Получение правильного ответа на вопрос")
    void shouldGetSolutionAnswer() throws IOException {
        Assert.assertEquals("4", service.getSolutionAnswer(0));
    }

    @Test
    @DisplayName("Количество вопросов в файле")
    void shouldGetCountQuestions() throws IOException {
        Assert.assertEquals(1, service.getCountQuestions());
    }
}