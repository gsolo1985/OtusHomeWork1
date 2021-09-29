package ru.otus.operations.publish.operationreval;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationRevalOutPublishGatewayImpl implements OperationRevalOutPublishGateway {
    private final KafkaTemplate<String, OperationRevalDtoList> kafkaTemplateR;

    @Value(value = "${deals.kafka-topic-name.operation-reval-out}")
    private String channel;

    @Override
    public void publish(OperationRevalDtoList msg) {
        kafkaTemplateR.send(channel, msg);
    }
}
