package ru.otus.operations.service.accounting;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentTemplateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.service.RevalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование AccountingService")
class AccountingServiceImplTest {
    private AccountingService service;

    @Mock
    private DocumentTemplateService documentTemplateService;

    @Mock
    private RevalService revalService;

    @Mock
    private DocumentService documentService;

    private OperationEntity operationEntity;

    @BeforeEach
    public void init() {
        service = new AccountingServiceImpl(documentTemplateService, revalService, documentService);
    }

    @BeforeAll
    public void setUp() {
        operationEntity = OperationEntity.builder()
                .operationId(1L)
                .amount(BigDecimal.valueOf(1123))
                .build();
    }

    @Test
    @DisplayName("Выполнение учета")
    void execAccountingByOperationOnDate() {
        List<DocumentTemplateEntity> templates = Collections.singletonList(DocumentTemplateEntity.builder().docType(DocType.DOC_TYPE_LOAD).build());
        Mockito.when(documentTemplateService.getDocTemplateForOperation(operationEntity)).thenReturn(templates);
        Mockito.when(documentService.save(any())).thenReturn(any());
        service.execAccountingByOperationOnDate(operationEntity, LocalDate.now());
    }
}