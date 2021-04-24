package ru.otus.studenttest.service;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import ru.otus.studenttest.config.ApplicationSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Service
public class ReadCsvFileServiceImpl implements ReadCsvFileService {
    private final ApplicationSettings applicationSettings;

    public ReadCsvFileServiceImpl(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @Override
    public File readResourceFile() {
        return new File(getClass().getClassLoader().getResource(applicationSettings.getFilePath()).getFile());
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
