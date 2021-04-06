package ru.otus.studenttest.service;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@SpringBootTest
@DisplayName("Класс сервиса вывода вопросов")
class OutputQuestionsServiceImplTest {
    final private String fileName = "questions.csv";
    private ReadCsvFileServiceImpl readCsvFileService = new ReadCsvFileServiceImpl(fileName);
    private OutputQuestionsServiceImpl service = new OutputQuestionsServiceImpl(readCsvFileService);

    OutputQuestionsServiceImplTest() throws IOException {
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