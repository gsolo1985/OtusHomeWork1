package ru.otus.operations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BusinessProcess")
public class BusinessProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BusinessProcessID")
    private Long businessProcessId;

    @Column(name = "SysName")
    private String sysName;

    @Column(name = "OrderType")
    private int orderType;
}
