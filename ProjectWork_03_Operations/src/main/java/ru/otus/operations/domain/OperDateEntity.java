package ru.otus.operations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OperDate")
public class OperDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OperDateID")
    private Long operDateId;

    @Column(name = "OperDate")
    private LocalDate operDate;

    @Column(name = "Status")
    private int status;
}
