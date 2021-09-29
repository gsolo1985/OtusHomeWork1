package ru.otus.reval.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.reval.consumer.currencyRate.CurrencyRateDto;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование RateTransformService")
class RateTransformServiceImplTest {
    private RateTransformService service;

    private static final String USD_NAME = "USD";
    private static final String RUB_NAME = "RUB";

    @Mock
    private CurrencyRepository currencyRepository;

    private CurrencyEntity currencyEntityUSD;
    private CurrencyEntity currencyEntityRUB;

    @BeforeAll
    public void setUp() {
        currencyEntityUSD = CurrencyEntity.builder().id(1L).name(USD_NAME).build();
        currencyEntityRUB = CurrencyEntity.builder().id(2L).name(RUB_NAME).build();
    }

    @BeforeEach
    public void init() {
        service = new RateTransformServiceImpl(currencyRepository);
    }

    @Test
    @DisplayName("Поиск и установка валюты")
    void setCurrencyEntityToListRateDto() {
        Mockito.when(currencyRepository.findByName(USD_NAME)).thenReturn(Optional.of(currencyEntityUSD));
        Mockito.when(currencyRepository.findByName(RUB_NAME)).thenReturn(Optional.of(currencyEntityRUB));
        var currDtoList = Collections.singletonList(CurrencyRateDto.builder().currencyPair("USDRUB").build());

        service.setCurrencyEntityToListRateDto(currDtoList);

        assertThat(currDtoList.get(0).getBuyCurrency()).usingRecursiveComparison().isEqualTo(currencyEntityUSD);
        assertThat(currDtoList.get(0).getSellCurrency()).usingRecursiveComparison().isEqualTo(currencyEntityRUB);
    }

    @Test
    @DisplayName("Получение CurrencyRateEntity из CurrencyRateDto")
    void getCurrencyRateEntityFromRateDto() {
        var currDtoList = Collections.singletonList(CurrencyRateDto.builder()
                .currencyPair("USDRUB")
                .buyCurrency(currencyEntityUSD)
                .sellCurrency(currencyEntityRUB)
                .date("2018-09-01")
                .rate(BigDecimal.valueOf(123.456))
                .build());

        var target = CurrencyRateEntity.builder()
                .currencyBuy(currencyEntityUSD)
                .currencySell(currencyEntityRUB)
                .date(LocalDate.parse("2018-09-01"))
                .value(BigDecimal.valueOf(123.456))
                .build();

        var currencyRateEntityList = service.getCurrencyRateEntityFromRateDto(currDtoList);
        assertThat(currencyRateEntityList.get(0)).usingRecursiveComparison().isEqualTo(target);
    }
}