package org.hofftech.edu.service;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorServiceTest {

    private ValidatorService validatorService;

    @BeforeEach
    void setUp() {
        validatorService = new ValidatorService();
    }

    @Test
    void isValidFile_shouldReturnFalse_whenFileIsEmpty() {
        // Arrange
        List<String> lines = List.of();

        // Act
        boolean result = validatorService.isValidFile(lines);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void isValidFile_shouldReturnTrue_whenFileHasLines() {
        // Arrange
        List<String> lines = List.of("Line 1", "Line 2");

        // Act
        boolean result = validatorService.isValidFile(lines);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void isValidPackages_shouldReturnTrue_whenAllPackagesAreValid() {
        // Arrange
        Package pkg1 = new Package(PackageType.TWO, 1, null);
        Package pkg2 = new Package(PackageType.THREE, 2, null);
        List<Package> packages = List.of(pkg1, pkg2);

        // Act
        boolean result = validatorService.isValidPackages(packages);

        // Assert
        assertThat(result).isTrue();
    }

    @Disabled
    @Test
    void isValidJsonStructure_shouldReturnTrue_whenStructureIsValid() {
        // Arrange
        Map<String, Object> jsonData = Map.of(
                "trucks", List.of(
                        Map.of("packages", List.of(
                                Map.of("type", "TWO"),
                                Map.of("type", "THREE")
                        ))
                )
        );

        // Act
        boolean result = validatorService.isValidJsonStructure(jsonData);

        // Assert
        assertThat(result).isTrue();
    }

    @Disabled
    @Test
    void isValidJsonStructure_shouldReturnFalse_whenTrucksKeyIsMissing() {
        // Arrange
        Map<String, Object> jsonData = Map.of(
                "vehicles", List.of() // Некорректный ключ
        );

        // Act
        boolean result = validatorService.isValidJsonStructure(jsonData);

        // Assert
        assertThat(result).isFalse();
    }

    @Disabled
    @Test
    void isValidJsonStructure_shouldReturnFalse_whenPackageTypeKeyIsMissing() {
        // Arrange
        Map<String, Object> jsonData = Map.of(
                "trucks", List.of(
                        Map.of("packages", List.of(
                                Map.of("weight", 100) // Нет ключа "type"
                        ))
                )
        );

        // Act
        boolean result = validatorService.isValidJsonStructure(jsonData);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void isFileExists_shouldReturnTrue_whenFileExists() {
        // Arrange
        File file = new File("test-file.txt");
        file.delete(); // Убедимся, что файл отсутствует
        try {
            file.createNewFile(); // Создаем файл
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать файл для теста.");
        }

        // Act
        boolean result = validatorService.isFileExists(file);

        // Assert
        assertThat(result).isTrue();

        // Cleanup
        file.delete();
    }

    @Test
    void isFileExists_shouldReturnFalse_whenFileDoesNotExist() {
        // Arrange
        File file = new File("non-existent-file.txt");

        // Act
        boolean result = validatorService.isFileExists(file);

        // Assert
        assertThat(result).isFalse();
    }
}
