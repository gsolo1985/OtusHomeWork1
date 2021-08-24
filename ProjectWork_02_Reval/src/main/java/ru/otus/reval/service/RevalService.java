package ru.otus.reval.service;

import ru.otus.reval.consumer.reval.OperationRevalDto;
import ru.otus.reval.model.RevalOperation;

public interface RevalService {

    /**Расчитать переоценку по операции
     *
     * @param revalOperation - объект по которому нужен расчет
     */
    void calcReval(RevalOperation revalOperation);

    /**Преобразование объекта OperationRevalDto в RevalOperation
     *
     * @param operationRevalDto - объект для преобразования
     * @return - преобразованный объект
     */
    RevalOperation transform (OperationRevalDto operationRevalDto);
}
