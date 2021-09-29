package ru.otus.operations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long documentId;
    private Long operationId;
    private LocalDate operDate;
    private BigDecimal amount;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private String comment;
    private String docTypeName;
}
