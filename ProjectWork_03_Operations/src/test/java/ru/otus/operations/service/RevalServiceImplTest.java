package ru.otus.operations.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.domain.CurrencyCashEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.RevalEntity;
import ru.otus.operations.domain.SecurityEntity;
import ru.otus.operations.model.RevalDto;
import ru.otus.operations.repository.RevalRepository;
import ru.otus.operations.statemachine.OperationState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование RevalServiceImpl")
class RevalServiceImplTest {
    private RevalService service;

    @Mock
    private CurrencyCashService currencyCashService;
    @Mock
    private OperationService operationService;
    @Mock
    private RevalRepository repository;

    private RevalEntity entity;
    private LocalDate operDate;
    private RevalDto dto;
    private OperationEntity operationEntity;

    @BeforeEach
    public void init() {
        service = new RevalServiceImpl(currencyCashService, operationService, repository);
    }

    @BeforeAll
    public void setUp() {
        operDate = LocalDate.parse("2015-05-05");
        operationEntity = OperationEntity.builder()
                .operationId(1L)
                .currencyCashEntity(CurrencyCashEntity.builder().name("RUB").currencyId(1L).build())
                .num(1)
                .amount(BigDecimal.valueOf(13))
                .operationDate(operDate)
                .planDate(operDate)
                .state(OperationState.LOADED)
                .securityEntity(SecurityEntity.builder().securityId(1L).name("BON4").build())
                .build();

        entity = RevalEntity.builder()
                .currencyEntity(CurrencyCashEntity.builder().name("RUB").currencyId(1L).build())
                .operationEntity(operationEntity)
                .currencyRevalEntity(CurrencyCashEntity.builder().name("USD").currencyId(2L).build())
                .operDate(operDate)
                .revalValue(BigDecimal.valueOf(13))
                .revalId(1L)
                .build();

        dto = RevalDto.builder()
                .currencyName("RUB")
                .currencyRevalName("USD")
                .operationId(1L)
                .operDate(operDate)
                .revalId(1L)
                .revalValue(BigDecimal.valueOf(13))
                .build();
    }


    @Test
    @DisplayName("Dto в Entity")
    void dtoTransformToEntity() {
        var actual = service.entityToDto(entity);
        assertThat(actual).isEqualTo(dto);
    }

    @Test
    @DisplayName("Поиск по дате")
    void findByOperDate() {
        Mockito.when(repository.findByOperDate(operDate)).thenReturn(Collections.singletonList(entity));

        var actual = service.findByOperDate(operDate);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(entity);
    }

    @Test
    @DisplayName("Поиск по операции")
    void findByOperationEntity() {
        Mockito.when(repository.findByOperationEntity(operationEntity)).thenReturn(Collections.singletonList(entity));

        var actual = service.findByOperationEntity(operationEntity);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(entity);
    }
}