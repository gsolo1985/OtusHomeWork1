package ru.otus.operations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.operations.constants.Constants;
import ru.otus.operations.domain.OperDateEntity;
import ru.otus.operations.repository.OperDateRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperDateServiceImpl implements OperDateService {
    private final OperDateRepository repository;

    @Value("${OperDates.daysAdd}")
    private int daysAdd;
    @Value("${OperDates.startDate}")
    private String startDateStr;

    /**
     * Заполнить БД операционными датами
     */
    @Override
    @Transactional
    public void fillOperDate() {
        LocalDate startDate = LocalDate.parse(startDateStr);

        if (!repository.existsByOperDateIdGreaterThan(0L)) {
            addDates(LocalDate.parse(startDateStr).minusDays(1));
        } else {
            var operDate = repository.findTop1ByStatusOrderByOperDateDesc(Constants.OperDateStatus.EMPTY.ordinal()); // последний необработанный день

            if (operDate.isEmpty()) {
                var lastCloseDate = repository.findTop1ByStatusOrderByOperDateDesc(Constants.OperDateStatus.CLOSE.ordinal()); // последний закрытый день

                lastCloseDate.ifPresent(date -> {
                    addDates(date.getOperDate());
                });
            }
        }
    }

    /**
     * Открывает новый операционный день
     */
    @Override
    @Transactional
    public OperDateEntity openOperDay() {
        Optional<OperDateEntity> lastCloseDay = repository.findTop1ByStatusOrderByOperDateDesc(Constants.OperDateStatus.CLOSE.ordinal());
        Optional<OperDateEntity> newDate = Optional.empty();

        if (lastCloseDay.isPresent())
            newDate = repository.findTop1ByOperDateGreaterThanOrderByOperDate(lastCloseDay.get().getOperDate());
        else
            newDate = repository.findTop1ByStatusOrderByOperDate(Constants.OperDateStatus.EMPTY.ordinal());

        //если такой даты нет в БД - добавим
        if (newDate.isEmpty() && lastCloseDay.isPresent()) {
            return addNextOperDate(lastCloseDay.get().getOperDate(), Constants.OperDateStatus.OPEN.ordinal());
        }

        if (newDate.isEmpty()) {
            return addNextOperDate(LocalDate.parse(startDateStr), Constants.OperDateStatus.OPEN.ordinal());
        }

        //если есть - апдейтим статус
        var newDateEntity = newDate.get();
        newDateEntity.setStatus(Constants.OperDateStatus.OPEN.ordinal());
        System.out.println("Открываем новый операционный день: " + newDateEntity.getOperDate());

        return repository.save(newDateEntity);
    }

    /**
     * Получить текущий операционный день
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OperDateEntity> getOperDay() {
        return repository.findTop1ByStatusOrderByOperDateDesc(Constants.OperDateStatus.OPEN.ordinal());
    }

    /**
     * Закрыть операционный день
     */
    @Override
    @Transactional
    public void closeOperDay(LocalDate date) {
        repository.findByOperDate(date).ifPresent(this::closeOperDay);
    }

    /**
     * Закрыть операционный день
     */
    @Override
    @Transactional
    public void closeOperDay(OperDateEntity operDateEntity) {
        System.out.println("Закрываем операционный день: " + operDateEntity.getOperDate());
        operDateEntity.setStatus(Constants.OperDateStatus.CLOSE.ordinal());
        repository.save(operDateEntity);
    }

    private OperDateEntity addNextOperDate(LocalDate operDate, int status) {
        LocalDate newDate = operDate.plusDays(1);

        if (newDate.getDayOfWeek().equals(DayOfWeek.SATURDAY))
            newDate = operDate.plusDays(3);

        if (newDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))
            newDate = operDate.plusDays(3);

        System.out.println("Открываем новый операционный день: " + newDate + ".");

        return repository.save(OperDateEntity.builder()
                .operDate(newDate)
                .status(status)
                .build());
    }

    private void addDates(LocalDate date) {
        int day = 0;
        while (day < daysAdd) {
            date = date.plusDays(1);

            if (!date.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                    !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                repository.save(OperDateEntity.builder()
                        .operDate(date)
                        .status(Constants.OperDateStatus.EMPTY.ordinal())
                        .build());
                day++;
            }
        }
    }
}
