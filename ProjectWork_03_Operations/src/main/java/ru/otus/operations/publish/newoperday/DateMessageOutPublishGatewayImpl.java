package ru.otus.operations.publish.newoperday;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DateMessageOutPublishGatewayImpl implements DateMessageOutPublishGateway {
    private final KafkaTemplate<String, DateMessage> kafkaTemplate;

    @Value(value = "${deals.kafka-topic-name.new-oper-date}")
    private String channel;

    @Override
    public void publish(DateMessage msg) {
        kafkaTemplate.send(channel, msg);
    }
}
