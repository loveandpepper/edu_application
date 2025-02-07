package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.repository.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class ParsingServiceTest {

    private PackageRepository packageRepositoryMock;
    private ParsingService parsingService;

    @BeforeEach
    void setUp() {
        packageRepositoryMock = Mockito.mock(PackageRepository.class);
        parsingService = new ParsingService(packageRepositoryMock);
    }

    @Test
    void shouldParsePackagesFromFile() {
        List<String> lines = List.of("Посылка Тип 1", "Посылка Тип 2");

        Mockito.when(packageRepositoryMock.findById("Посылка Тип 1"))
                .thenReturn(Optional.of(new Package("Посылка Тип 1", null, '1', 0, 0)));
        Mockito.when(packageRepositoryMock.findById("Посылка Тип 2"))
                .thenReturn(Optional.of(new Package("Посылка Тип 2", null, '2', 0, 0)));

        List<Package> result = parsingService.parsePackagesFromFile(lines);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .extracting(Package::getName)
                .containsExactly("Посылка Тип 1", "Посылка Тип 2");
    }

    @Test
    void shouldThrowExceptionForMissingPackage() {
        List<String> lines = List.of("package1");

        Mockito.when(packageRepositoryMock.findById("package1"))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> parsingService.parsePackagesFromFile(lines))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Посылка package1 не найдена!");
    }

    @Test
    void shouldParsePackagesFromArgs() {
        String parcelsText = "Посылка Тип 1, Посылка Тип 2";

        Mockito.when(packageRepositoryMock.findById("Посылка Тип 1"))
                .thenReturn(Optional.of(new Package("Посылка Тип 1", null, '1', 0, 0)));
        Mockito.when(packageRepositoryMock.findById("Посылка Тип 2"))
                .thenReturn(Optional.of(new Package("Посылка Тип 2", null, '2', 0, 0)));

        List<Package> result = parsingService.getPackagesFromArgs(parcelsText);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .extracting(Package::getName)
                .containsExactly("Посылка Тип 1", "Посылка Тип 2");
    }

    @Test
    void shouldThrowExceptionForEmptyArgs() {
        Assertions.assertThatThrownBy(() -> parsingService.getPackagesFromArgs(null))
                .isInstanceOf(PackageArgumentException.class)
                .hasMessageContaining("Аргумент с посылками пуст");
    }
}

