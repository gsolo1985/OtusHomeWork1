package ru.otus.studenttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import ru.otus.studenttest.service.StudentServiceTestingImpl;

import java.io.IOException;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        StudentServiceTestingImpl service = context.getBean(StudentServiceTestingImpl.class);

        service.startTesting();
    }

}
