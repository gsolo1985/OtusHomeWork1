package ru.otus.operations.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.domain.CurrencyCashEntity;
import ru.otus.operations.repository.CurrencyCashRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование CurrencyCashService")
class CurrencyCashServiceImplTest {
    private CurrencyCashService service;
    @Mock
    private CurrencyCashRepository repository;

    @BeforeEach
    public void init() {
        service = new CurrencyCashServiceImpl(repository);
    }


    @Test
    @DisplayName("Получение случайной валюты")
    void getRandomCurrency() {
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(new CurrencyCashEntity()));
        var currency = service.getRandomCurrency();
        assertThat(currency).isNotNull();
    }

    @Test
    @DisplayName("Получение валюты по имени")
    void getByName() {
        Mockito.when(repository.findByName("RUB")).thenReturn(Optional.of(CurrencyCashEntity.builder().currencyId(1L).name("RUB").build()));
        var currency = service.getByName("RUB");

        assertThat(currency.getName()).isEqualTo("RUB");
    }

}