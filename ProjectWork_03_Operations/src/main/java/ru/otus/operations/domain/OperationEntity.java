package ru.otus.operations.domain;

import lombok.*;
import ru.otus.operations.statemachine.OperationState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Operation")
@NamedEntityGraph(name = "operation-entity-graph", attributeNodes = {@NamedAttributeNode("securityEntity"), @NamedAttributeNode("currencyCashEntity")})
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OperationID")
    private Long operationId;

    @ManyToOne(targetEntity = SecurityEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "SecurityID")
    private SecurityEntity securityEntity;

    @Column(name = "OperationDate")
    private LocalDate operationDate;

    @Column(name = "PlanDate")
    private LocalDate planDate;

    @Column(name = "Num")
    private int num;

    @ManyToOne(targetEntity = CurrencyCashEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "CurrencyID")
    private CurrencyCashEntity currencyCashEntity;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "State")
    private OperationState state;

    @Column(name = "ActualDate")
    private LocalDateTime actualDate;
}
