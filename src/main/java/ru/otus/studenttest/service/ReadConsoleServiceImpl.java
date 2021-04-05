package ru.otus.studenttest.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ReadConsoleServiceImpl implements ReadConsoleService {
    private InputStream inputStream; // весь этот ужас с полями и с конструкторами для junit-теста(
    private BufferedReader reader;

    public ReadConsoleServiceImpl(InputStream inputStream) {
        this.inputStream = inputStream;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public ReadConsoleServiceImpl() {
        this.inputStream = System.in;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String readStringInfo() throws IOException {
        return reader.readLine();
    }
}
