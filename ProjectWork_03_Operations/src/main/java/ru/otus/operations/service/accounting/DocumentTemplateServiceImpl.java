package ru.otus.operations.service.accounting;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.DocumentTemplateEntity;
import ru.otus.operations.domain.OperationEntity;
import ru.otus.operations.repository.DocumentTemplateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTemplateServiceImpl implements DocumentTemplateService {
    private final DocumentTemplateRepository repository;

    /**
     * Получить список подходящих шаблонов документов по сделке
     *
     * @param operationEntity - операция
     * @return - список шаблонов
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentTemplateEntity> getDocTemplateForOperation(OperationEntity operationEntity) {
        return Lists.newArrayList(repository.findAll()).stream().filter(t -> t.getOperState().equals(operationEntity.getState())).collect(Collectors.toList());
    }
}
