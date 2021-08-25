package ru.otus.operations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.statemachine.OperationState;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DocumentTemplate")
public class DocumentTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DocumentTemplateID")
    private Long documentTemplateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DocType")
    private DocType docType;

    @Column(name = "DocComment")
    private String comment;

    @Column(name = "OperState")
    private String operStateName;
}
