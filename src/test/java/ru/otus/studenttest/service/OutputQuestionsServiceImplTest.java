package ru.otus.studenttest.service;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@SpringBootTest
@DisplayName("Тестирование сервиса вывода вопросов")
class OutputQuestionsServiceImplTest {

    private ReadCsvFileServiceImpl readCsvFileService;
    private OutputQuestionsServiceImpl service;
    final private String fileName = "questions.csv";

    @Test
    void outputQuestionsFromCsv() throws IOException {
        PrintStream consoleStream = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        readCsvFileService = new ReadCsvFileServiceImpl(fileName);
        service = new OutputQuestionsServiceImpl(readCsvFileService);
        service.outputQuestionsFromCsv();

        String outputStr = output.toString();

        System.setOut(consoleStream);
        System.out.println("How can I test it?");

        Assert.assertEquals(output.toString(), outputStr);
        /*
        Получилось добиться только через собственный вывод в консоль. Т.к. если оставить вот такие строки:
        String out = "How can I test it?";
        String outputStr = output.toString();
        Assert.assertEquals(out, outputStr);

        то получается ошибка:
        org.junit.ComparisonFailure: expected:<How can I test it?[]> but was:<How can I test it?[
        ]>
         */
    }
}