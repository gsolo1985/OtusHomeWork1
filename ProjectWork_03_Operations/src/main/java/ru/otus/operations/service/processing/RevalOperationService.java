package ru.otus.operations.service.processing;

import ru.otus.operations.consumer.RevalOperationList;
import ru.otus.operations.domain.OperDateEntity;

public interface RevalOperationService {
    /**
     * Запуск бизнес-процесса с системным именем "OPERATIONS_CURRENCY_REVAL_SYS_NAME"
     * Отбирает операции, для которых нужна вал. переоценка и отправляет по кафке в сервис для расчета
     *
     * @param operDateEntity - дата
     */
    void exec(OperDateEntity operDateEntity);

    /**
     * Сохранить посчитанную другим сервисом валютную переоценку и поменить процесс, как выполненный
     *
     * @param calcRevalList - посчитанная переоценка
     */
    void saveCalcReval(RevalOperationList calcRevalList);
}
