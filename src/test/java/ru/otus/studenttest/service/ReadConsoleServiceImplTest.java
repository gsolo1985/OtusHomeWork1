package ru.otus.studenttest.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс чтения с консоли")
class ReadConsoleServiceImplTest {

    @DisplayName("Проверка чтения с консоли")
    @Test
    void shouldReadStringFromConsole() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test_input".getBytes());
        ReadConsoleServiceImpl readConsoleService = new ReadConsoleServiceImpl(inputStream);

        assertThat("test_input").isEqualTo(readConsoleService.readStringInfo());
    }
}