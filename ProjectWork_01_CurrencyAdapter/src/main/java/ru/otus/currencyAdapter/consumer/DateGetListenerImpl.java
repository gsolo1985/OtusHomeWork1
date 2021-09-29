package ru.otus.currencyAdapter.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.currencyAdapter.feign.CurrencyLayer;
import ru.otus.currencyAdapter.publish.CurrencyRateByDateOutPublishGateway;
import ru.otus.currencyAdapter.publish.CurrencyRateDtoList;
import ru.otus.currencyAdapter.service.RateTransformService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DateGetListenerImpl implements DateGetListener {
    private final CurrencyLayer currencyLayer;
    private final RateTransformService rateTransformService;
    private final CurrencyRateByDateOutPublishGateway gateway;

    @KafkaListener(
            topics = "${deals.kafka-topic-name.new-oper-date}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")

    @Override
    public void consume(DateMessage msg) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(msg.getDate());
        if (date != null) {
            System.out.println("Открыт новый операционный день. Запрашиваем валюты за " + date);
            var rates = rateTransformService.layerTransformToDto(currencyLayer.getRatesByDate(date.format(formatter)), 0);

            if (rates.size() > 0) {
                gateway.publish(CurrencyRateDtoList.builder()
                        .currencyRateDtoList(rates)
                        .build());
            }
        }
    }
}
