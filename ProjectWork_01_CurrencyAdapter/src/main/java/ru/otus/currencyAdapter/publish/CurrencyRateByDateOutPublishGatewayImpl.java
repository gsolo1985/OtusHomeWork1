package ru.otus.currencyAdapter.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyRateByDateOutPublishGatewayImpl implements CurrencyRateByDateOutPublishGateway {
    private final KafkaTemplate<String, CurrencyRateDtoList> kafkaTemplate;

    @Value(value = "${deals.kafka-topic-name.currency-rate-by-date-out}")
    private String channel;

    @Override
    public void publish(CurrencyRateDtoList msg) {
        kafkaTemplate.send(channel, msg);
    }
}
