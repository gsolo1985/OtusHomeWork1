package ru.otus.operations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolDto {
    private Long businessProcessByOperDateId;
    private BusinessProcessDto businessProcessDto;
    private LocalDate operDate;
    private String statusName;
}
