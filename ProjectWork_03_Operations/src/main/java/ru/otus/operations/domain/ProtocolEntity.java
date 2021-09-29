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
@Table(name = "Protocol")
public class ProtocolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProtocolID")
    private Long protocolId;

    @NaturalId
    @Column(name = "OperDate")
    private LocalDate operDate;

    @NaturalId
    @ManyToOne(targetEntity = BusinessProcessEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "BusinessProcessID")
    @ToString.Exclude
    private BusinessProcessEntity businessProcessEntity;

    @Column(name = "Status")
    private int status;

}
