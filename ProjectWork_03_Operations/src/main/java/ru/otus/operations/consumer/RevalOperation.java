package ru.otus.operations.consumer;

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
public class RevalOperation {
    private long operationId;
    private String currencyName;
    private BigDecimal amount;
    private LocalDate revalDate;
    private BigDecimal revalAmount;
}
