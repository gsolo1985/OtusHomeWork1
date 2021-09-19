package ru.otus.reval.consumer.reval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OperationRevalDto {
    private long operationId;
    private String currencyName;
    private BigDecimal amount;
    private String revalDate;
}
