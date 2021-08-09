package ru.otus.spring.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookGetOutPublishGatewayImpl implements BookGetOutPublishGateway {
    private final KafkaTemplate<String, BookGetMessage> kafkaTemplate;
    @Value(value = "${library.kafka-topic-name.get-out-msg}")
    private String channel;

    @Override
    public void publish(BookGetMessage msg) {
        kafkaTemplate.send(channel, msg);
    }
}
