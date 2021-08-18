package ru.otus.reval.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.exception.CurrencyNotValidException;
import ru.otus.reval.model.CurrencyGet;
import ru.otus.reval.repository.CurrencyRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование CurrencyServiceImpl")
class CurrencyServiceImplTest {
    private CurrencyService service;

    @Mock
    private CurrencyRepository repository;

    private static final String USD_NAME = "USD";
    private static final String RUB_NAME = "RUB";

    @BeforeEach
    public void init() {
        service = new CurrencyServiceImpl(repository);
    }

    private CurrencyEntity currencyEntityUSD;
    private CurrencyEntity currencyEntityRUB;

    @BeforeAll
    public void setUp() {
        currencyEntityUSD = CurrencyEntity.builder().id(1L).name(USD_NAME).build();
        currencyEntityRUB = CurrencyEntity.builder().id(2L).name(RUB_NAME).build();
    }

    @Test
    @DisplayName("Получение валюты по id")
    void getById() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(currencyEntityUSD));
        var actual = service.getById(1L);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(currencyEntityUSD);
    }

    @Test
    @DisplayName("Получение валюты по имени")
    void getByName() {
        Mockito.when(repository.findByName(RUB_NAME)).thenReturn(Optional.ofNullable(currencyEntityRUB));
        var actual = service.getByName(RUB_NAME);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(currencyEntityRUB);
    }

    @Test
    @DisplayName("Преобразование валюты в dto")
    void transformToDto() {
        var target = CurrencyGet.builder().id(currencyEntityRUB.getId()).name(currencyEntityRUB.getName()).build();
        var actual = service.transformToDto(currencyEntityRUB);

        assertThat(actual).isEqualTo(target);
    }

    @Test
    @DisplayName("Сохранение валюты - с ошибкой")
    void saveWithError() {
        Throwable thrown = assertThrows(CurrencyNotValidException.class, () -> {
            service.save(CurrencyEntity.builder().build());
        });

        assertThat(thrown.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Сохранение валюты")
    void save() {
        Mockito.when(repository.save(currencyEntityUSD)).thenReturn(currencyEntityUSD);
        service.save(currencyEntityUSD);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Преобразование в entity")
    void transformToEntity() {
        var target = currencyEntityRUB;
        var actual = service.transformToEntity(CurrencyGet.builder().id(currencyEntityRUB.getId()).name(currencyEntityRUB.getName()).build());

        assertThat(actual).isEqualTo(target);
    }
}