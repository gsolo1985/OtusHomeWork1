package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.domain.SecurityEntity;
import ru.otus.operations.repository.SecurityRepository;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final SecurityRepository repository;

    /**
     * Получить случайную ЦБ из БД
     *
     * @return - ЦБ
     */
    @Override
    @Transactional(readOnly = true)
    public SecurityEntity getRandomSecurity() {
        var secAll = repository.findAll();
        int size = secAll.size();
        int random = RandomUtils.nextInt(0, size - 1);

        if (random >= 0) {
            return secAll.get(random);
        }
        return SecurityEntity.builder().securityId(-1L).name("random").type(Constants.SecurityType.SHARE.ordinal()).build();
    }
}
