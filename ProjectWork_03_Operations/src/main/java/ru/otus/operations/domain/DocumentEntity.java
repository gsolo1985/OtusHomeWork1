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
@Table(name = "Document")
@NamedEntityGraph(name = "document-entity-graph", attributeNodes = {@NamedAttributeNode("operationEntity")})
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentID")
    private Long documentID;

    @ManyToOne(targetEntity = OperationEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "ObjectID")
    private OperationEntity operationEntity;

    @Column(name = "OperDate")
    private LocalDate operDate;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "DebitAccountNumber")
    private String debitAccountNumber;

    @Column(name = "CreditAccountNumber")
    private String creditAccountNumber;

    @Column(name = "DocComment")
    private String comment;

}
