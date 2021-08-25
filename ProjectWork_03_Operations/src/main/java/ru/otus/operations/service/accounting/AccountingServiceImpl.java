package ru.otus.operations.service.accounting;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.DocumentEntity;
import ru.otus.operations.domain.DocumentTemplateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.service.RevalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService {
    private final DocumentTemplateService documentTemplateService;
    private final RevalService revalService;
    private final DocumentService documentService;

    @Value(value = "${nationalCurrency.name}")
    private String nationalCurrencyName;

    /**
     * Выполнить учет по операции на дату
     *
     * @param operationEntity - операция
     * @param operDate        - дата
     * @return - список документов
     */
    @Override
    public List<DocumentEntity> execAccountingByOperationOnDate(OperationEntity operationEntity, LocalDate operDate) {
        var templates = documentTemplateService.getDocTemplateForOperation(operationEntity);
        List<DocumentEntity> result = new ArrayList<>();

        List<DocumentEntity> documents = getDocsForSave(operationEntity, operDate, templates);

        documents.forEach(d -> {
            result.add(documentService.save(d));
        });

        return result;
    }

    private List<DocumentEntity> getDocsForSave(OperationEntity operationEntity, LocalDate operDate, List<DocumentTemplateEntity> templates) {
        List<DocumentEntity> result = new ArrayList<>();

        templates.forEach(t -> {
            BigDecimal amount = getAmountForDocument(t.getDocType(), operationEntity, operDate);

            if (amount != null) {
                result.add(DocumentEntity.builder()
                        .amount(amount)
                        .comment(t.getComment())
                        .creditAccountNumber(getRandomAccount())
                        .debitAccountNumber(getRandomAccount())
                        .operationEntity(operationEntity)
                        .operDate(operDate)
                        .build());
            }

        });

        return result;
    }

    private String getRandomAccount() {
        return "47426810" + RandomUtils.nextInt(100000, 999999);
    }

    private BigDecimal getAmountForDocument(DocType docType, OperationEntity operationEntity, LocalDate operDate) {
        if (docType.equals(DocType.DOC_TYPE_LOAD))
            return operationEntity.getAmount();

        if (docType.equals(DocType.DOC_TYPE_CANCEL))
            return operationEntity.getAmount();

        if (docType.equals(DocType.DOC_TYPE_REVAL)) {
            var reval = revalService.getRevalByOperationAndDate(operationEntity, operDate);

            if (reval.isPresent()) {
                return reval.get().getRevalValue();
            }
            else {
                return operationEntity.getCurrencyCashEntity().getName().equals(nationalCurrencyName) ? BigDecimal.ZERO : operationEntity.getAmount();
            }
        }

        return null;
    }
}
