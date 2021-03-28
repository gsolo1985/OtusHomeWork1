package ru.otus.studenttest.service;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;


public class OutputQuestionsServiceImpl implements OutputQuestionsService {
    final ReadCsvFileService readCsvFileService;

    public OutputQuestionsServiceImpl(ReadCsvFileService readCsvFileService) {
        this.readCsvFileService = readCsvFileService;
    }

    @Override
    public void outputQuestionsFromCsv() throws IOException {
        String[] questions;
        CSVReader csvreader = new CSVReader(new FileReader(readCsvFileService.readResourseFile()), ';', '"', 1);

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
