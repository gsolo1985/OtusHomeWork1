package ru.otus.operations.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Security")
@NamedEntityGraph(name = "security-entity-graph", attributeNodes = {@NamedAttributeNode("issuerEntity")})
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SecurityID")
    private Long securityId;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private int type;

    @ManyToOne(targetEntity = InstitutionEntity.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "InstitutionID")
    @ToString.Exclude
    private InstitutionEntity issuerEntity;
}
