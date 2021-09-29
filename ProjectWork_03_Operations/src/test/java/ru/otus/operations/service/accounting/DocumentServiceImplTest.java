package ru.otus.operations.service.accounting;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.model.DocumentDto;
import ru.otus.operations.repository.DocumentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование DocumentService")
class DocumentServiceImplTest {
    private DocumentService service;

    @Mock
    private DocumentRepository repository;

    private DocumentEntity documentEntity;
    private final LocalDate date = LocalDate.now();

    @BeforeEach
    public void init() {
        service = new DocumentServiceImpl(repository);
    }

    @BeforeAll
    public void setUp() {
        documentEntity = DocumentEntity.builder()
                .amount(BigDecimal.valueOf(123))
                .comment("comment")
                .creditAccountNumber("123")
                .debitAccountNumber("456")
                .docType(DocType.DOC_TYPE_LOAD)
                .operDate(date)
                .documentId(1L)
                .operationEntity(OperationEntity.builder().operationId(13L).build())
                .build();
    }


    @Test
    @DisplayName("Поиск по дате и типу документа")
    void findByOperDateAndDocType() {
        List<DocumentEntity> docs = Collections.singletonList(documentEntity);
        Mockito.when(repository.findByOperDateAndDocType(date, DocType.DOC_TYPE_LOAD)).thenReturn(docs);

        var actual = service.findByOperDateAndDocType(date, DocType.DOC_TYPE_LOAD);
        assertThat(actual).isEqualTo(docs);
    }

    @Test
    @DisplayName("Entity к dto")
    void entityToDto() {
        var target = DocumentDto.builder()
                .amount(documentEntity.getAmount())
                .comment(documentEntity.getComment())
                .creditAccountNumber(documentEntity.getCreditAccountNumber())
                .debitAccountNumber(documentEntity.getDebitAccountNumber())
                .docTypeName(documentEntity.getDocType().getName())
                .operDate(documentEntity.getOperDate())
                .operationId(13L)
                .documentId(1L)
                .build();

        var actual = service.entityToDto(documentEntity);
        assertThat(actual).isEqualTo(target);
    }
}