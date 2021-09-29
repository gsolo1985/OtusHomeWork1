package ru.otus.operations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessProcessDto {
    private Long businessProcessId;
    private String sysName;
    private int order;
    private int isOn;
}
