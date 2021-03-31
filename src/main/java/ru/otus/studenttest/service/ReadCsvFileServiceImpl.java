package ru.otus.studenttest.service;

import java.io.File;

public class ReadCsvFileServiceImpl implements ReadCsvFileService {
    private final String fileName;

    public ReadCsvFileServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public File readResourseFile() {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }
}
