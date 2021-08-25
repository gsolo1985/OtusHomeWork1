package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.BusinessProcessEntity;
import ru.otus.operations.repository.BusinessProcessRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessProcessServiceImpl implements BusinessProcessService {
    private final BusinessProcessRepository repository;

    /**
     * Получить все бизнес-процессы (с учетом сортировки)
     *
     * @param sort - сортировка
     * @return - список бизнес-процессов
     */
    @Override
    @Transactional(readOnly = true)
    public List<BusinessProcessEntity> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
