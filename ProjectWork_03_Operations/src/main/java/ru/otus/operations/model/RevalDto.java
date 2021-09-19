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
public class RevalDto {
    private Long revalId;
    private long operationId;
    private LocalDate operDate;
    private BigDecimal revalValue;
    private String currencyName;
    private String currencyRevalName;

    @Override
    public String toString() {
        return "revalId=" + revalId +
                ", operationId=" + operationId +
                ", operDate=" + operDate +
                ", revalValue=" + revalValue +
                ", currencyName='" + currencyName + '\'' +
                ", currencyRevalName='" + currencyRevalName + '\'';
    }
}
