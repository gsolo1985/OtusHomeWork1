package ru.otus.operations.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.integration.OperDateMessage;
import ru.otus.operations.model.BusinessProcessDto;
import ru.otus.operations.model.ProtocolDto;
import ru.otus.operations.repository.BusinessProcessRepository;
import ru.otus.operations.repository.ProtocolRepository;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.operations.constants.BusinessProcessConstants.BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED;
import static ru.otus.operations.constants.BusinessProcessConstants.OPERATIONS_CURRENCY_REVAL_SYS_NAME;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование ProtocolServiceImpl")
class ProtocolServiceImplTest {
    private ProtocolService service;

    @Mock
    private ProtocolRepository repository;
    @Mock
    private BusinessProcessRepository businessProcessRepository;
    @Mock
    private OperDateMessage operDateMessage;

    private BusinessProcessEntity businessProcessEntity;
    private OperDateEntity operDateEntity;
    private LocalDate operDate;
    private ProtocolEntity protocolEntity;

    @BeforeEach
    public void init() {
        service = new ProtocolServiceImpl(repository, businessProcessRepository, operDateMessage);
    }

    @BeforeAll
    public void setUp() {
        operDate = LocalDate.parse("2015-05-05");
        businessProcessEntity = BusinessProcessEntity.builder()
                .businessProcessId(1L)
                .orderType(1)
                .sysName(OPERATIONS_CURRENCY_REVAL_SYS_NAME)
                .build();

        operDateEntity = OperDateEntity.builder()
                .operDateId(1L)
                .operDate(operDate)
                .status(0)
                .build();

        protocolEntity = ProtocolEntity.builder()
                .businessProcessEntity(businessProcessEntity)
                .operDate(operDate)
                .status(BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED)
                .protocolId(1L)
                .build();
    }

    @Test
    @DisplayName("Сохранить протокол по БП на дату")
    void saveByBusinessProcessesAndOperDate() {
        var target = ProtocolEntity.builder()
                .businessProcessEntity(businessProcessEntity)
                .operDate(operDate)
                .status(BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED)
                .protocolId(1L)
                .build();

        Mockito.when(repository.save(any())).thenReturn(target);
        var actual = service.saveByBusinessProcessesAndOperDate(businessProcessEntity, operDateEntity, BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSED);

        assertThat(actual).isEqualTo(target);
    }

    @Test
    @DisplayName("Из entity в dto")
    void entityToDto() {
        var bpDto = BusinessProcessDto.builder()
                .businessProcessId(1L)
                .order(1)
                .sysName(OPERATIONS_CURRENCY_REVAL_SYS_NAME)
                .build();

        var target = ProtocolDto.builder()
                .businessProcessByOperDateId(1L)
                .operDate(operDate)
                .statusName("Обработан")
                .businessProcessDto(bpDto)
                .build();

        var actual = service.entityToDto(protocolEntity);
        assertThat(actual).isEqualTo(target);
    }

    @Test
    @DisplayName("Поиск по дате")
    void findByOperDate() {
        Mockito.when(repository.findByOperDate(operDate)).thenReturn(Collections.singletonList(protocolEntity));
        service.findByOperDate(operDate);
        verify(repository, times(1)).findByOperDate(operDate);
    }
}