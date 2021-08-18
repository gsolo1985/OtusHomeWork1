package ru.otus.reval.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.repository.CurrencyRateRepository;
import ru.otus.reval.service.CurrencyRateService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Тестирование контроллера для CurrencyRate")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyRateControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CurrencyRateRepository repository;
    @Autowired
    private CurrencyRateService service;

    private static final String EUR_NAME = "EUR";
    private static final String RUB_NAME = "RUB";
    private static final String RATE_DATE = "2018-09-01";
    private static final long RATE_VALUE = 123L;

    private CurrencyEntity currencyEntityEUR;
    private CurrencyEntity currencyEntityRUB;

    @BeforeAll
    public void setUp() {
        currencyEntityEUR = CurrencyEntity.builder().id(1L).name(EUR_NAME).build();
        currencyEntityRUB = CurrencyEntity.builder().id(2L).name(RUB_NAME).build();
    }

    @Test
    @Transactional
    @DisplayName("вернуть котировку по id")
    void getRateById() throws Exception {
        var rateSaved = service.save(CurrencyRateEntity.builder()
                .date(LocalDate.parse(RATE_DATE))
                .value(BigDecimal.valueOf(RATE_VALUE))
                .currencyBuy(currencyEntityEUR)
                .currencySell(currencyEntityRUB).build());

        long id = rateSaved.getId();

        var expected = service.transformToDto(rateSaved);

        mvc.perform(get("/currencyRates/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(rateSaved)));
    }

    @Test
    void deleteCurrencyRate() {
    }

    @Test
    void getRateByCurrencyPairAndDate() {
    }

    @Test
    void saveCurrencyRate() {
    }
}