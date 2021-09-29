package ru.otus.reval.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.reval.model.RevalOperationList;

@Service
@RequiredArgsConstructor
public class CalcRevalOutPublishGatewayImpl implements CalcRevalOutPublishGateway {
    private final KafkaTemplate<String, RevalOperationList> kafkaTemplate;

    @Value(value = "${deals.kafka-topic-name.calc-reval-out}")
    private String channel;

    @Override
    public void publish(RevalOperationList msg) {
        kafkaTemplate.send(channel, msg);
    }
}
