package ru.otus.reval.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.reval.service.CurrencyRateService;
import ru.otus.reval.service.RateTransformService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CurrencyRateListenerImpl implements CurrencyRateListener {
    private final RateTransformService rateTransformService;
    private final CurrencyRateService currencyRateService;

    @KafkaListener(
            topics = "${deals.kafka-topic-name.currency-rate-by-date-out}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    @Override
    public void consume(CurrencyRateDto msg) {
        var rateDtoList = Collections.singletonList(msg);
        rateTransformService.setCurrencyEntityToListRateDto(rateDtoList); //проставим валюты из БД, если валют нет, то добавим их
        var rateList = rateTransformService.getCurrencyRateEntityFromRateDto(rateDtoList); //трансформируем dto в entity
        rateList.forEach(currencyRateService::save); //сохраняем в БД
    }
}
