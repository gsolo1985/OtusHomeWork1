package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.domain.CurrencyCashEntity;
import ru.otus.operations.repository.CurrencyCashRepository;

@Service
@RequiredArgsConstructor
public class CurrencyCashServiceImpl implements CurrencyCashService {
    private final CurrencyCashRepository repository;

    @Override
    public CurrencyCashEntity getRandomCurrency() {
        var curAll = repository.findAll();
        int size = curAll.size();
        int random = RandomUtils.nextInt(0, size - 1);

        if (random >= 0) {
            return curAll.get(random);
        }
        return CurrencyCashEntity.builder().currencyId(-1L).name("RAN").build();
    }

    /**
     * Получить валюту по имени (Если такой валюты нет, то она будет добавлена)
     *
     * @param name - имя
     * @return - валюта
     */
    @Override
    @Transactional
    public CurrencyCashEntity getByName(String name) {
        return repository.findByName(name).orElseGet(() -> repository.save(CurrencyCashEntity.builder()
                .name(name)
                .build()));
    }

    /**
     * Сохранить валюту
     *
     * @param currency - валюта
     * @return - сохраненный объект
     */
    @Override
    @Transactional
    public CurrencyCashEntity save(CurrencyCashEntity currency) {
        return repository.save(currency);
    }
}
