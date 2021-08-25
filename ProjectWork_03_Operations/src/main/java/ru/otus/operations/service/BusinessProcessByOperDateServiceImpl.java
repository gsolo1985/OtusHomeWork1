package ru.otus.operations.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.domain.BusinessProcessByOperDateEntity;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.integration.OperDateMessage;
import ru.otus.operations.repository.BusinessProcessByOperDateRepository;
import ru.otus.operations.repository.BusinessProcessRepository;

import java.util.stream.Collectors;

import static ru.otus.operations.constants.BusinessProcessConstants.*;

@Service
@RequiredArgsConstructor
public class BusinessProcessByOperDateServiceImpl implements BusinessProcessByOperDateService {
    private final BusinessProcessByOperDateRepository repository;
    private final BusinessProcessRepository businessProcessRepository;
    private final OperDateMessage operDateMessage;

    @Value(value = "${app_mode}")
    private int appMode;

    /**
     * Добавить все бизнес-процессы в обработку за дату
     *
     * @param operDateEntity - операционная дата
     */
    @Override
    @Transactional
    public void addBusinessProcessesByOperDate(OperDateEntity operDateEntity) {
        var businessProcesses = businessProcessRepository.findAll();

        businessProcesses.forEach(bp -> {
            repository.save(BusinessProcessByOperDateEntity.builder()
                    .status(BUSINESS_PROCESS_BY_DATE_STATUS_PROCESSING)
                    .businessProcessEntity(bp)
                    .operDate(operDateEntity.getOperDate())
                    .build());
        });
    }

    /**
     * Установить статус у БП за дату
     *
     * @param operDateEntity               - дата
     * @param businessProcessEntitySysName - системное имя БП
     * @param status                       - статус
     */
    @Override
    @Transactional
    public void setBusinessProcessesByOperDateStatus(OperDateEntity operDateEntity, String businessProcessEntitySysName, int status) {
        var businessProcess = businessProcessRepository.findBySysName(businessProcessEntitySysName);

        businessProcess.ifPresent(bp -> {
            var businessProcessByDate = repository.findByBusinessProcessEntityAndOperDate(bp, operDateEntity.getOperDate());

            businessProcessByDate.ifPresent(bpbd -> {
                bpbd.setStatus(status);
                repository.save(bpbd);
            });
        });

        // проверяем все ли БП завершены, если все - то при appMode = 0, отправляем событие, чтобы закрыть опер. день
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
     * Проверить, все ли настроенные бизнес-процессы завершены за дату (за исключением бизнес-процессов "закрытие операционного дня" и "Подготовка к открытию операционного дня")
     *
     * @param operDateEntity - дата
     * @return - true, если все бизнес-процессы завершены
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkProcessChainEndByOperDate(OperDateEntity operDateEntity) {
        boolean result = true;
        var businessProcesses = Lists.newArrayList(businessProcessRepository.findAll())
                .stream()
                .filter(p -> !p.getSysName().equals(CLOSE_OPER_DATE_SYS_NAME))
                .collect(Collectors.toList());

        var processList = repository.findByOperDate(operDateEntity.getOperDate());

        for (var bp : businessProcesses) {
            var processByDate = processList.stream().filter(p -> p.getBusinessProcessEntity().getBusinessProcessId().equals(bp.getBusinessProcessId())).findFirst().orElse(null);

            if (processByDate == null) // процесс еще начался
                return false;

            if (processByDate.getStatus() == Constants.BusinessProcessByDateStatus.PROCESSING.ordinal()) // процесс начался, но не завершился
                return false;
        }

        return true;
    }
}