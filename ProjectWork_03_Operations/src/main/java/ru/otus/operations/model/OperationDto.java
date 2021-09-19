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
public class OperationDto {
    private Long operationId;
    private String securityName;
    private String securityTypeName;
    private LocalDate operationDate;
    private LocalDate planDate;
    private int num;
    private String currencyName;
    private BigDecimal amount;
    private String stateName;

    @Override
    public String toString() {
        return "operationId=" + operationId +
                ", operationDate=" + operationDate +
                ", planDate=" + planDate +
                ", currencyName='" + currencyName + '\'' +
                ", amount=" + amount +
                ", securityName='" + securityName + '\'' +
                ", securityTypeName='" + securityTypeName + '\'' +
                ", stateName='" + stateName + '\'';
    }
}
