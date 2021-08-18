package ru.otus.operations.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BusinessProcess")
public class BusinessProcessByOperDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BusinessProcessByOperDateID")
    private Long businessProcessByOperDateID;

    @NaturalId
    @Column(name = "OperDate")
    private LocalDate operDate;

    @NaturalId
    @ManyToOne(targetEntity = BusinessProcessEntity.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessProcessID")
    @ToString.Exclude
    protected BusinessProcessEntity businessProcessEntity;

}
