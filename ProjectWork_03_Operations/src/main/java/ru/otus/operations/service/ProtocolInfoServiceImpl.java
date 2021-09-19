package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.operations.constants.DocType;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.model.DocumentDto;
import ru.otus.operations.model.OperationDto;
import ru.otus.operations.model.ProtocolInfoDto;
import ru.otus.operations.model.RevalDto;
import ru.otus.operations.service.accounting.DocumentService;
import ru.otus.operations.statemachine.OperationState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class ProtocolInfoServiceImpl implements ProtocolInfoService {
    private final ProtocolService businessProcessByOperDateService;
    private final OperationService operationService;
    private final RevalService revalService;
    private final DocumentService documentService;

    /**
     * Получить протокол объектов, участвующих или являющимися результатом работы бизнес-процесса за операционную дату
     *
     * @param businessProcessByOperDateEntity - БП за дату
     * @return - протокол
     */
    @Override
    public ProtocolInfoDto getByBusinessProcessByOperDate(ProtocolEntity businessProcessByOperDateEntity) {
        ProtocolInfoDto protocol = ProtocolInfoDto.builder()
                .protocolDto(businessProcessByOperDateService.entityToDto(businessProcessByOperDateEntity))
                .build();

        var bp = businessProcessByOperDateEntity.getBusinessProcessEntity();
        LocalDate operDate = businessProcessByOperDateEntity.getOperDate();

        if (bp != null && operDate != null) {
            String sysName = bp.getSysName();


            if (sysName.equals(OPERATIONS_CREATE_SYS_NAME)) {
                setOperList(operationService.findByOperationDate(operDate), protocol);

                setDocumentList(protocol, operDate, DocType.DOC_TYPE_LOAD);
            }

            if (sysName.equals(OPERATIONS_CANCEL_SYS_NAME)) {
                setOperList(operationService.findByPlanDateAndState(operDate, OperationState.CANCELED), protocol);

                setDocumentList(protocol, operDate, DocType.DOC_TYPE_CANCEL);
            }

            if (sysName.equals(OPERATIONS_EXECUTION_SYS_NAME)) {
                setOperList(operationService.findByPlanDateAndState(operDate, OperationState.EXEC), protocol);

                setDocumentList(protocol, operDate, DocType.DOC_TYPE_REVAL);
            }

            if ((sysName.equals(OPERATIONS_CURRENCY_REVAL_SYS_NAME))) {
                var revalList = revalService.findByOperDate(operDate);
                List<RevalDto> revalDtoList = new ArrayList<>();

                revalList.forEach(r -> revalDtoList.add(revalService.entityToDto(r)));

                protocol.setRevalDtoList(revalDtoList);
            }

        }
        return protocol;
    }

    private void setDocumentList(ProtocolInfoDto protocol, LocalDate operDate, DocType docType) {
        var docs = documentService.findByOperDateAndDocType(operDate, docType);
        List<DocumentDto> documentDtoList = new ArrayList<>();

        docs.forEach(d -> documentDtoList.add(documentService.entityToDto(d)));
        protocol.setDocumentDtoList(documentDtoList);
    }

    private void setOperList(List<OperationEntity> operations, ProtocolInfoDto protocol) {
        List<OperationDto> operationDtoList = new ArrayList<>();

        operations.forEach(oper -> {
            operationDtoList.add(operationService.entityToDto(oper));
        });

        protocol.setOperationDtoList(operationDtoList);
    }

}


