package ru.otus.reval.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.domain.CurrencyRateEntity;
import ru.otus.reval.exception.CurrencyRateNotValidException;
import ru.otus.reval.model.CurrencyRateGet;
import ru.otus.reval.repository.CurrencyRateRepository;
import ru.otus.reval.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование CurrencyRateServiceImpl")
class CurrencyRateServiceImplTest {
    private CurrencyRateService service;

    @Mock
    private CurrencyRateRepository repository;
    @Mock
    private CurrencyRepository currencyRepository;

    private static final String USD_NAME = "USD";
    private static final String RUB_NAME = "RUB";

    @BeforeEach
    public void init() {
        service = new CurrencyRateServiceImpl(repository, currencyRepository);
    }

    private CurrencyEntity currencyEntityUSD;
    private CurrencyEntity currencyEntityRUB;

    @BeforeAll
    public void setUp() {
        currencyEntityUSD = CurrencyEntity.builder().id(1L).name(USD_NAME).build();
        currencyEntityRUB = CurrencyEntity.builder().id(2L).name(RUB_NAME).build();
    }

    @Test
    @DisplayName("Сохранение котировки")
    void save() {
        var rate = CurrencyRateEntity.builder()
                .date(LocalDate.parse("2018-09-01"))
                .value(BigDecimal.valueOf(123))
                .currencyBuy(currencyEntityUSD)
                .currencySell(currencyEntityRUB)
                .build();

        Mockito.when(repository.save(rate)).thenReturn(rate);
        Mockito.when(repository.findByDateAndCurrencyBuyAndCurrencySell(LocalDate.parse("2018-09-01"), currencyEntityUSD, currencyEntityRUB)).thenReturn(Optional.empty());
        service.save(rate);

        verify(repository, times(1)).save(any());
    }

    @DisplayName("Возвращать ошибку при сохранении если не указать какие-то параметры")
    @Test
    void saveWithError() {
        Throwable thrown = assertThrows(CurrencyRateNotValidException.class, () -> {
            service.save(CurrencyRateEntity.builder().build());
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @DisplayName("Получить котировку по id")
    @Test
    void getById() {
        CurrencyRateEntity currencyRateEntity = CurrencyRateEntity.builder()
                .date(LocalDate.parse("2018-09-01"))
                .value(BigDecimal.valueOf(123))
                .currencyBuy(currencyEntityUSD)
                .currencySell(currencyEntityRUB)
                .id(1L)
                .build();

        var target = CurrencyRateGet.builder()
                .buyCurrencyId(1L)
                .buyCurrencyName(USD_NAME)
                .sellCurrencyId(2L)
                .sellCurrencyName(RUB_NAME)
                .date(LocalDate.parse("2018-09-01"))
                .rate(BigDecimal.valueOf(123))
                .id(1L)
                .build();

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(currencyRateEntity));
        var actual = service.getById(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(target);
    }

    @Test
    @DisplayName("Получение котировки по валютной паре и дате")
    void getByCurrencyPairAndDate() {
        LocalDate date = LocalDate.parse("2018-09-01");
        CurrencyRateEntity currencyRateEntity = CurrencyRateEntity.builder()
                .date(date)
                .value(BigDecimal.valueOf(123))
                .currencyBuy(currencyEntityUSD)
                .currencySell(currencyEntityRUB)
                .id(1L)
                .build();

        Mockito.when(currencyRepository.findByName(USD_NAME)).thenReturn(Optional.ofNullable(currencyEntityUSD));
        Mockito.when(currencyRepository.findByName(RUB_NAME)).thenReturn(Optional.ofNullable(currencyEntityRUB));
        Mockito.when(repository.findByDateAndCurrencyBuyAndCurrencySell(any(), any(), any())).thenReturn(Optional.ofNullable(currencyRateEntity));

        var target = CurrencyRateGet.builder()
                .buyCurrencyId(1L)
                .buyCurrencyName(USD_NAME)
                .sellCurrencyId(2L)
                .sellCurrencyName(RUB_NAME)
                .date(date)
                .rate(BigDecimal.valueOf(123))
                .id(1L)
                .build();

        var actual = service.getByCurrencyPairAndDate("USDRUB", date);

        assertThat(actual).usingRecursiveComparison().isEqualTo(target);
    }

    @Test
    @DisplayName("Преобразавание в dto")
    void transformToDto() {
        var target = CurrencyRateGet.builder()
                .rate(BigDecimal.valueOf(123))
                .date(LocalDate.parse("2018-09-01"))
                .buyCurrencyName(USD_NAME)
                .sellCurrencyName(RUB_NAME)
                .buyCurrencyId(1L)
                .sellCurrencyId(2L)
                .id(1L)
                .build();

        var actual = service.transformToDto(CurrencyRateEntity.builder()
                .id(1L)
                .currencyBuy(currencyEntityUSD)
                .currencySell(currencyEntityRUB)
                .value(BigDecimal.valueOf(123))
                .date(LocalDate.parse("2018-09-01"))
                .build());

        assertThat(actual).usingRecursiveComparison().isEqualTo(target);
    }
}