package ru.otus.library.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.library.dto.BookDto;

@Service
@RequiredArgsConstructor
public class BookGetOutPublishGatewayImpl implements BookGetOutPublishGateway {
    private final KafkaTemplate<String, BookDto> kafkaTemplate;
    @Value(value = "${library.kafka-topic-name.get-out-book}")
    private String channel;

    @Override
    public void publish(BookDto book) {
        kafkaTemplate.send(channel, book);
    }
}
