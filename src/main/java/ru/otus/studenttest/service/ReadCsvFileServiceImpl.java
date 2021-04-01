package ru.otus.studenttest.service;

import au.com.bytecode.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Service
@RequiredArgsConstructor
public class ReadCsvFileServiceImpl implements ReadCsvFileService {

    @Value("${file.name}")
    private String fileName;

    public ReadCsvFileServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public File readResourceFile() {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

    @Override
    public CSVReader getCSVReader(File file) throws FileNotFoundException {
        return new CSVReader(new FileReader(file), ';', '"', 1);
    }

    @Override
    public CSVReader getCSVReaderFromResourceFile() throws FileNotFoundException {
        return getCSVReader(readResourceFile());
    }

}
