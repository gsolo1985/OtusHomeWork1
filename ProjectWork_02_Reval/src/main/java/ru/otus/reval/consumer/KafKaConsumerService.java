package ru.otus.reval.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.reval.consumer.currencyRate.CurrencyRateDto;
import ru.otus.reval.consumer.currencyRate.CurrencyRateDtoList;
import ru.otus.reval.consumer.reval.OperationRevalDtoList;
import ru.otus.reval.model.RevalOperation;
import ru.otus.reval.model.RevalOperationList;
import ru.otus.reval.publish.CalcRevalOutPublishGateway;
import ru.otus.reval.service.CurrencyRateService;
import ru.otus.reval.service.RateTransformService;
import ru.otus.reval.service.RevalService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KafKaConsumerService {
    private final RateTransformService rateTransformService;
    private final CurrencyRateService currencyRateService;
    private final RevalService revalService;
    private final CalcRevalOutPublishGateway calcRevalOutPublishGateway;

    @KafkaListener(topics = "${deals.kafka-topic-name.currency-rate-by-date-out}",
                   groupId = "${spring.kafka.consumer.group-id}")

    /**
     * Слушатель события по добавлению курса валют
     */
    public void consume(CurrencyRateDtoList msg) {
        System.out.println("Получили список курсов валют");

        if (msg.getCurrencyRateDtoList() != null) {
            rateTransformService.setCurrencyEntityToListRateDto(msg.getCurrencyRateDtoList()); //проставим валюты из БД, если валют нет, то добавим их
            var rateList = rateTransformService.getCurrencyRateEntityFromRateDto(msg.getCurrencyRateDtoList()); //трансформируем dto в entity

            System.out.println("rateList = " + rateList);
            rateList.forEach(currencyRateService::save); //сохраняем в БД
        }

    }

    @KafkaListener(topics = "${deals.kafka-topic-name.operation-reval-out}",
                   groupId = "${spring.kafka.consumer.group-id}",
                   containerFactory = "userKafkaListenerContainerFactory")

    /**
     * Слушатель события по запросу расчета переоценки
     */
    public void consume(OperationRevalDtoList msg) throws InterruptedException {
        List<RevalOperation> operationList = new ArrayList<>();
        TimeUnit.SECONDS.sleep(3); // ждем 3 секунды, чтобы адаптер по валютам успел отработать и у нас были все курсы

        if (msg.getOperationList() != null) {
            System.out.println("Получили списиок операций для расчета переоценки в размере: " + msg.getOperationList().size());

            msg.getOperationList().forEach(o -> {
                var reval = revalService.transform(o);
                System.out.println("________________________________");
                System.out.println("Расчитываем по операции: " + reval);
                revalService.calcReval(reval);
                operationList.add(reval);
            });
        }

        if (operationList.size() > 0) {
            var msgOut = RevalOperationList.builder()
                    .revalOperationList(operationList)
                    .build();

            calcRevalOutPublishGateway.publish(msgOut);
        }
    }
}
