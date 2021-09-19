package ru.otus.reval.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CurrencyRate")
@NamedEntityGraph(name = "currency-rate-graph",
        attributeNodes = {@NamedAttributeNode("currencyBuy"),
                @NamedAttributeNode("currencySell")})
public class CurrencyRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = CurrencyEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrencyBuyID")
    private CurrencyEntity currencyBuy;

    @ManyToOne(targetEntity = CurrencyEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrencySellID")
    private CurrencyEntity currencySell;

    @Column(name = "RateValue")
    private BigDecimal value;

    @Column(name = "RateDate")
    private LocalDate date;
}
