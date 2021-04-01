package ru.otus.studenttest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование сервиса чтения файла")
@SpringBootTest
class ReadCsvFileServiceImplTest {
    @Autowired
    private ReadCsvFileServiceImpl service;

    @Test
    @DisplayName("Чтение файла")
    void outputQuestions() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("questions.csv").getFile());

        assertThat(file).isEqualTo(service.readResourceFile());
    }

}