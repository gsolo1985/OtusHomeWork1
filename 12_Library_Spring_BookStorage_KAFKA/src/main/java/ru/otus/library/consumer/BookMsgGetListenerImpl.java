package ru.otus.library.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.mapper.DtoMapper;
import ru.otus.library.publish.BookGetOutPublishGateway;
import ru.otus.library.service.BookService;

@Service
@RequiredArgsConstructor
public class BookMsgGetListenerImpl implements BookMsgGetListener {
    private final BookGetOutPublishGateway gateway;
    private final BookService bookService;
    private final DtoMapper dtoMapper;

    @KafkaListener(
            topics = "${library.kafka-topic-name.get-out-msg}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    @Override
    @Transactional(readOnly = true)
    public void consume(BookGetMessage msg) {
        var book = bookService.getById(msg.getId());

        book.ifPresent(b -> {
            System.out.println("была запрошенна книга: " + b);
            gateway.publish(dtoMapper.bookToDto(b));
        });
    }
}
