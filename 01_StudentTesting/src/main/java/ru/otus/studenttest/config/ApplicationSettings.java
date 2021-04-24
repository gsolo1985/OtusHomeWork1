package ru.otus.studenttest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("application")
public class ApplicationSettings {
    private String filePath;
    private int trueAnswers;
    private String locale;
}
