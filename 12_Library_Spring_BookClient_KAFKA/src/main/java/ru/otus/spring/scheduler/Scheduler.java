package ru.otus.spring.scheduler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.spring.publish.BookGetMessage;
import ru.otus.spring.publish.BookGetOutPublishGateway;

@Service
@RequiredArgsConstructor
public class Scheduler {
    private final BookGetOutPublishGateway gateway;

    @Scheduled(fixedDelayString = "7000")
    public void sendBookRequest() {
        System.out.println("Шедуллер начал работу");
        int id = RandomUtils.nextInt(1, 4);
        System.out.println("Запрашиваем книгу с id = " + id);

        gateway.publish(new BookGetMessage(id));
        System.out.println("Шедуллер закончил работу");
        System.out.println("________________________");
    }
}
