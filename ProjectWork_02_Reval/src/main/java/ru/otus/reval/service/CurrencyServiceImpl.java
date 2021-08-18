package ru.otus.reval.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.reval.domain.CurrencyEntity;
import ru.otus.reval.exception.CurrencyNotValidException;
import ru.otus.reval.model.CurrencyGet;
import ru.otus.reval.repository.CurrencyRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository repository;

    private static final String INSERT_ERROR = "New currency insert error: ";

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyEntity> getById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyEntity> getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public CurrencyGet transformToDto(CurrencyEntity currencyEntity) {
        if (currencyEntity != null) {
            return CurrencyGet.builder()
                    .id(currencyEntity.getId())
                    .name(currencyEntity.getName())
                    .build();
        }

        return new CurrencyGet();
    }

    @Override
    public CurrencyEntity transformToEntity(CurrencyGet dto) {
        if (dto != null) {
            return CurrencyEntity.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .build();
        }

        return new CurrencyEntity();
    }

    @Override
    @Transactional
    public CurrencyEntity save(CurrencyEntity currencyEntity) {
        String name = currencyEntity.getName();
        Long id = currencyEntity.getId();

        if (name == null) {
            throw new CurrencyNotValidException(INSERT_ERROR + "you must set a name.");
        }

        if (name.length() != 3) {
            throw new CurrencyNotValidException(INSERT_ERROR + "incorrect currency name.");
        }

        if (id != null) { // update
            var currencyExists = repository.findById(id);

            currencyExists.ifPresent(cur ->{
                currencyEntity.setName(name);
            });

            return repository.save(currencyEntity);
        }

        var currencyExists = repository.findByName(name);

        currencyExists.ifPresent(cur ->{
            currencyEntity.setId(cur.getId());
        });

        return repository.save(currencyEntity);
    }
}
