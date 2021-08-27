package ru.otus.operations.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.operations.domain.InstitutionEntity;
import ru.otus.operations.domain.SecurityEntity;
import ru.otus.operations.repository.SecurityRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Тестрование SecurityServiceImpl")
class SecurityServiceImplTest {
    private SecurityService service;

    @Mock
    private SecurityRepository repository;

    SecurityEntity entity;

    @BeforeEach
    public void init() {
        service = new SecurityServiceImpl(repository);
    }

    @BeforeAll
    public void setUp() {
        entity = SecurityEntity.builder()
                .issuerEntity(InstitutionEntity.builder().name("MMVB").institutionId(1L).build())
                .name("BON2")
                .securityId(1L)
                .type(1)
                .build();
    }

    @Test
    @DisplayName("Получить рандомную ЦБ")
    void getRandomSecurity() {
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        var actual = service.getRandomSecurity();
        assertThat(actual).isEqualTo(entity);
    }

    @Test
    @DisplayName("Получить ЦБ по имени")
    void getByName() {
        Mockito.when(repository.findByName("BON2")).thenReturn(Optional.ofNullable(entity));
        var actual = service.getByName("BON2");
        assertThat(actual).isEqualTo(Optional.of(entity));
    }
}