package ru.otus.reval.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.reval.consumer.reval.OperationRevalDto;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.model.RevalOperation;
import ru.otus.reval.repository.CurrencyRateRepository;
import ru.otus.reval.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционный тест RevalService")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevalServiceImplTest {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CurrencyRateRepository currencyRateRepository;
    @Autowired
    RevalService service;

    @Test
    @DisplayName("Расчет валютной котировки")
    void calcReval() {
        LocalDate date = LocalDate.parse("2018-10-01");
        var currRUB = currencyRepository.findByName("RUB");
        var currUSD = currencyRepository.findByName("USD");
        var currEUR = currencyRepository.findByName("EUR");

        assertThat(currRUB).isNotEmpty();
        assertThat(currUSD).isNotEmpty();
        assertThat(currEUR).isNotEmpty();

        currencyRateRepository.save(CurrencyRateEntity.builder()
                .currencyBuy(currUSD.get())
                .currencySell(currRUB.get())
                .date(date)
                .value(BigDecimal.valueOf(66.4032))
                .build());

        currencyRateRepository.save(CurrencyRateEntity.builder()
                .currencyBuy(currUSD.get())
                .currencySell(currEUR.get())
                .date(date)
                .value(BigDecimal.valueOf(0.8713))
                .build());

        var operReval = RevalOperation.builder()
                .currencyName("EUR")
                .operationId(1L)
                .revalDate(date)
                .amount(BigDecimal.valueOf(1000))
                .build();

        service.calcReval(operReval);
        assertThat(BigDecimal.valueOf(76211.63779)).isEqualTo(operReval.getRevalAmount());

        var operReval2 = RevalOperation.builder()
                .currencyName("USD")
                .operationId(2L)
                .revalDate(date)
                .amount(BigDecimal.valueOf(1000))
                .build();

        service.calcReval(operReval2);
        assertThat(BigDecimal.valueOf(66403.2)).isEqualTo(operReval2.getRevalAmount().setScale(1, RoundingMode.CEILING));
    }

    @DisplayName("Преобразование из dto")
    @Test
    void transform() {
        var target = RevalOperation.builder()
                .amount(BigDecimal.valueOf(123.456))
                .revalDate(LocalDate.parse("2015-05-05"))
                .operationId(13L)
                .currencyName("RUB")
                .build();

        var actual = service.transform(OperationRevalDto.builder()
                .operationId(13L)
                .revalDate("2015-05-05")
                .amount(BigDecimal.valueOf(123.456))
                .currencyName("RUB")
                .build());

        assertThat(actual).isEqualTo(target);
    }
}