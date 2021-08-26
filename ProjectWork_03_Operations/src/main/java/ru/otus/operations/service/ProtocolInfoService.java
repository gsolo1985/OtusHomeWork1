package ru.otus.operations.service;

import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.model.ProtocolInfoDto;

public interface ProtocolInfoService {

    /**
     * Получить протокол объектов, участвующих или являющимися результатом работы бизнес-процесса за операционную дату
     *
     * @param businessProcessByOperDateEntity - БП за дату
     * @return - протокол
     */
    ProtocolInfoDto getByBusinessProcessByOperDate(ProtocolEntity businessProcessByOperDateEntity);
}
