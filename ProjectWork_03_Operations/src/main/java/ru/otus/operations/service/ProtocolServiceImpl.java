package ru.otus.operations.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.domain.ProtocolEntity;
import ru.otus.operations.integration.OperDateMessage;
import ru.otus.operations.model.BusinessProcessDto;
import ru.otus.operations.model.ProtocolDto;
import ru.otus.operations.repository.BusinessProcessRepository;
import ru.otus.operations.repository.ProtocolRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class ProtocolServiceImpl implements ProtocolService {
    private final ProtocolRepository repository;
    private final BusinessProcessRepository businessProcessRepository;
    private final OperDateMessage operDateMessage;

    @Value(value = "${app_mode}")
    private int appMode;

    /**
     * Сохранить протокол по бизнес-процессу на дату
     *
     * @param businessProcessEntity - БП
     * @param operDateEntity        - дата
     * @param status                - статус
     * @return - протокол
     */
    @Override
    @Transactional
    public ProtocolEntity saveByBusinessProcessesAndOperDate(BusinessProcessEntity businessProcessEntity, OperDateEntity operDateEntity, int status) {
        var protocolExists = repository.findByBusinessProcessEntityAndOperDate(businessProcessEntity, operDateEntity.getOperDate());

        if (protocolExists.isPresent()) {
            var protocolSave = protocolExists.get();
            protocolSave.setStatus(status);
            return repository.save(protocolSave);
        }

        var saved =  repository.save(ProtocolEntity.builder()
                .businessProcessEntity(businessProcessEntity)
                .operDate(operDateEntity.getOperDate())
                .status(status)
                .build());

        // проверяем все ли БП завершены, если все - то при appMode = 0, отправляем событие, чтобы закрыть опер. день
        checkEndDay(operDateEntity, businessProcessEntity.getSysName());

        return saved;
    }

    /**
     * Установить статус у протокола
     *
     * @param operDateEntity               - дата
     * @param businessProcessEntitySysName - системное имя БП
     * @param status                       - статус
     */
    @Override
    @Transactional
    public void setProtocolStatus(OperDateEntity operDateEntity, String businessProcessEntitySysName, int status) {
        var businessProcess = businessProcessRepository.findBySysName(businessProcessEntitySysName);

        businessProcess.ifPresent(bp -> {
            var protocols = repository.findByBusinessProcessEntityAndOperDate(bp, operDateEntity.getOperDate());

            protocols.ifPresent(protocol -> {
                protocol.setStatus(status);
                repository.save(protocol);
            });
        });

        // проверяем все ли БП завершены, если все - то при appMode = 0, отправляем событие, чтобы закрыть опер. день
        checkEndDay(operDateEntity, businessProcessEntitySysName);

    }

    private void checkEndDay(OperDateEntity operDateEntity, String businessProcessEntitySysName) {
        if (!businessProcessEntitySysName.equals(CLOSE_OPER_DATE_SYS_NAME)) {
            boolean checkAllProcessEnd = checkProcessChainEndByOperDate(operDateEntity);

            if (checkAllProcessEnd && appMode == 0) {
                System.out.println(BUSINESS_PROCESS_END_BP_INFO);
                operDateMessage.businessProcessExec(CLOSE_OPER_DATE_SYS_NAME);
            }

            if (checkAllProcessEnd && appMode == 1) {
                System.out.println(BUSINESS_PROCESS_END_BP_INFO);
            }
        }
    }

    /**
     * Проверить, все ли настроенные бизнес-процессы завершены за дату (за исключением бизнес-процессов "закрытие операционного дня")
     *
     * @param operDateEntity - дата
     * @return - true, если все бизнес-процессы завершены
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkProcessChainEndByOperDate(OperDateEntity operDateEntity) {
        var businessProcesses = Lists.newArrayList(businessProcessRepository.findAll())
                .stream()
                .filter(p -> !p.getSysName().equals(CLOSE_OPER_DATE_SYS_NAME) && p.getIsOn() != 0)
                .collect(Collectors.toList());

        var protocolList = repository.findByOperDate(operDateEntity.getOperDate());

        if (businessProcesses.size() > protocolList.size())
            return false;

        if (protocolList.size() == 0)
            return false;

        var processing = protocolList.stream().filter(p -> p.getStatus() == BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSING).findFirst().orElse(null);

        return processing == null;
    }

    /**
     * Преобразование объекта entity в объект dto
     *
     * @param entity - entity-объект
     * @return - Dto-объект
     */
    @Override
    public ProtocolDto entityToDto(ProtocolEntity entity) {
        return ProtocolDto.builder()
                .businessProcessByOperDateId(entity.getProtocolId())
                .businessProcessDto(BusinessProcessDto.builder()
                        .businessProcessId(entity.getBusinessProcessEntity().getBusinessProcessId())
                        .sysName(entity.getBusinessProcessEntity().getSysName())
                        .order(entity.getBusinessProcessEntity().getOrderType())
                        .isOn(entity.getBusinessProcessEntity().getIsOn())
                        .build())
                .operDate(entity.getOperDate())
                .statusName(Constants.ProtocolStatus.values()[entity.getStatus()].getName())
                .build();
    }

    /**
     * Получить список выполнения бизнес-процессов за дату
     *
     * @param operDate - дата
     * @return - результат
     */
    @Override
    public List<ProtocolEntity> findByOperDate(LocalDate operDate) {
        return repository.findByOperDate(operDate);
    }
}
