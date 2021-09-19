package ru.otus.operations.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DocType implements Constants.ConstantsValue{
    DOC_TYPE_CANCEL("Документ на отмену операции"),
    DOC_TYPE_REVAL("Документ на валютную переоценку"),
    DOC_TYPE_LOAD("Документ о постановке на учет");

    private String name;
}