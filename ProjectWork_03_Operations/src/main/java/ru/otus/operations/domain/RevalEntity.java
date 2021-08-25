package ru.otus.operations.domain;

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
@Table(name = "Reval")
@NamedEntityGraph(name = "reval-entity-graph", attributeNodes = {@NamedAttributeNode("operationEntity"), @NamedAttributeNode("currencyEntity"), @NamedAttributeNode("currencyRevalEntity")})
public class RevalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RevalID")
    private Long revalId;

    @ManyToOne(targetEntity = OperationEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "OperationID")
    private OperationEntity operationEntity;

    @Column(name = "OperDate")
    private LocalDate operDate;

    @Column(name = "RevalValue")
    private BigDecimal revalValue;

    @ManyToOne(targetEntity = CurrencyCashEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrencyID")
    private CurrencyCashEntity currencyEntity;

    @ManyToOne(targetEntity = CurrencyCashEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrencyRevalID")
    private CurrencyCashEntity currencyRevalEntity;

}
