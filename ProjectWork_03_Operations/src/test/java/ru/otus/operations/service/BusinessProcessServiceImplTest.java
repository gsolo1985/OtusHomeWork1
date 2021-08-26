package ru.otus.operations.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.model.BusinessProcessDto;
import ru.otus.operations.repository.BusinessProcessRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.operations.constants.BusinessProcessConstants.OPERATIONS_CREATE_SYS_NAME;
import static ru.otus.operations.constants.BusinessProcessConstants.OPERATIONS_EXECUTION_SYS_NAME;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование BusinessProcessService")
class BusinessProcessServiceImplTest {
    private BusinessProcessServiceImpl service;

    @Mock
    private BusinessProcessRepository repository;

    private BusinessProcessEntity loadOper;
    private BusinessProcessEntity execOper;

    @BeforeEach
    public void init() {
        service = new BusinessProcessServiceImpl(repository);
    }

    @BeforeAll
    public void setUp() {
        loadOper = BusinessProcessEntity.builder().businessProcessId(1L).orderType(1).sysName(OPERATIONS_CREATE_SYS_NAME).build();
        execOper = BusinessProcessEntity.builder().businessProcessId(2L).orderType(2).sysName(OPERATIONS_EXECUTION_SYS_NAME).build();
    }


    @Test
    @DisplayName("Поиск всех БП")
    void findAll() {
        List<BusinessProcessEntity> bpList = Arrays.asList(loadOper, execOper);
        Mockito.when(repository.findAll(any())).thenReturn(bpList);
        service.findAll(Sort.by("OrderType"));
        verify(repository, times(1)).findAll(any());
    }

    @Test
    @DisplayName("Entity в dto")
    void entityToDto() {
        var target = BusinessProcessDto.builder().businessProcessId(1L).order(1).sysName(OPERATIONS_CREATE_SYS_NAME).build();
        var entity = BusinessProcessEntity.builder().businessProcessId(1L).orderType(1).sysName(OPERATIONS_CREATE_SYS_NAME).build();

        assertThat(service.entityToDto(entity)).isEqualTo(target);
    }

    @Test
    @DisplayName("Поиск по имени")
    void findBySysName() {
        Mockito.when(repository.findBySysName(OPERATIONS_EXECUTION_SYS_NAME)).thenReturn(Optional.of(execOper));
        service.findBySysName(OPERATIONS_EXECUTION_SYS_NAME);
        verify(repository, times(1)).findBySysName(OPERATIONS_EXECUTION_SYS_NAME);
    }
}