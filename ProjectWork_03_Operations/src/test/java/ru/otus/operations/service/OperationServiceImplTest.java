package ru.otus.operations.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.domain.CurrencyCashEntity;
import ru.otus.operations.domain.InstitutionEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.SecurityEntity;
import ru.otus.operations.model.OperationDto;
import ru.otus.operations.repository.OperationRepository;
import ru.otus.operations.statemachine.OperationState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование OperationServiceImpl")
class OperationServiceImplTest {
    private OperationService service;
    @Mock
    private OperationRepository repository;

    private LocalDate operDate;
    private LocalDate planDate;
    private OperationEntity operation;

    @BeforeEach
    public void init() {
        service = new OperationServiceImpl(repository);
    }

    @BeforeAll
    public void setUp() {
        operDate = LocalDate.parse("2015-05-05");
        planDate = LocalDate.parse("2016-06-06");
        operation = OperationEntity.builder()
                .amount(BigDecimal.valueOf(123))
                .num(123)
                .operationDate(operDate)
                .planDate(planDate)
                .state(OperationState.LOADED)
                .currencyCashEntity(CurrencyCashEntity.builder().currencyId(1L).name("USD").build())
                .securityEntity(SecurityEntity.builder().name("GAZPROM share").securityId(1L).type(1).issuerEntity(InstitutionEntity.builder().institutionId(1L).name("ОАО ГАЗПРОМ").build()).build())
                .operationId(1L)
                .build();
    }

    @Test
    @DisplayName("Поиск по id")
    void findById() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(operation));
        var actual = service.findById(1L);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(operation);
    }

    @Test
    @DisplayName("Поиск по плановой дате и состоянии")
    void findByPlanDateAndState() {
        Mockito.when(repository.findByPlanDateAndState(planDate, OperationState.LOADED)).thenReturn(Collections.singletonList(operation));
        var actual = service.findByPlanDateAndState(planDate, OperationState.LOADED);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(operation);
    }

    @Test
    @DisplayName("Из entity в dto")
    void entityToDto() {
        var target = OperationDto.builder()
                .amount(BigDecimal.valueOf(123))
                .num(123)
                .operationDate(operDate)
                .operationId(1L)
                .planDate(planDate)
                .stateName("Загружена")
                .currencyName("USD")
                .securityName("GAZPROM share")
                .securityTypeName("Акция")
                .build();

        var actual = service.entityToDto(operation);

        assertThat(actual).isEqualTo(target);
    }

    @Test
    @DisplayName("Удаление по id")
    void deleteById() {
        service.deleteById(2L);
        verify(repository, times(1)).deleteById(2L);
    }
}