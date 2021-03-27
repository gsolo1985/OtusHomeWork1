package ru.otus.studenttest.service;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadCsvFileServiceImpl implements ReadCsvFileService {
    private final String fileName;

    public ReadCsvFileServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void outputQuestions() throws IOException {
        String[] questions;
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());

        CSVReader csvreader = new CSVReader(new FileReader(file), ';', '"', 1);

        System.out.println("Questions:");
        while (true) {
            questions = csvreader.readNext();
            if (questions == null || questions.length == 0) {
                return;
            } else {
                System.out.println(questions[0]);
            }
        }
    }
}
