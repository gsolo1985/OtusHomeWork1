package ru.otus.studenttest.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.studenttest.config.ApplicationSettings;
import ru.otus.studenttest.domain.Student;
import ru.otus.studenttest.service.StudentServiceTesting;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ShellComponent
@RequiredArgsConstructor
public class ShellStudentService {
    private final StudentServiceTesting service;
    private Student student;
    private final MessageSource messageSource;
    private final ApplicationSettings applicationSettings;

    @PostConstruct //хотел сделать через ApplicationRunner, но почему-то ApplicationRunner просто перестает работать как только подключаешь зависимость pring-shell-starter
    public void init() {
        List<String> localeList = Arrays.asList(applicationSettings.getLocale().split("_"));
        if(!localeList.isEmpty() && localeList.size() > 1) {
            Locale.setDefault(new Locale(localeList.get(0), localeList.get(1)));
        }
    }

    @ShellMethod(value = "User login", key = {"login", "l"})
    public void login() throws IOException {
        student = service.loginStudent();
    }

    @ShellMethod(value = "Start testing", key = {"start", "test", "s", "t"})
    @ShellMethodAvailability(value = "isLoginCorrect")
    public void startTest() throws IOException {
        service.startTesting();
    }

    private Availability isLoginCorrect() {
        return student != null ?
                Availability.available() :
                Availability.unavailable(messageSource.getMessage("shell.loginNotAvailable", new Object[] {}, Locale.getDefault()));
    }

}
