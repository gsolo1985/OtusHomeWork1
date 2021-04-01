package ru.otus.studenttest.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Тестирование сервиса чтения файла")
class ReadCsvFileImplTest {
    private ReadCsvFileServiceImpl service;
    final private String fileName = "questions.csv";

    @BeforeAll
    void setUp() {
        service = new ReadCsvFileServiceImpl(fileName);
    }

    @Test
    @DisplayName("Чтение файла")
    void outputQuestions() throws IOException {
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());

        assertThat(file).isEqualTo(service.readResourseFile());
    }
}