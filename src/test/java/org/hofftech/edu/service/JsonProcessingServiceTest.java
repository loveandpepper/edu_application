package org.hofftech.edu.service;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JsonProcessingServiceTest {

    private ValidatorService validatorService;
    private JsonProcessingService jsonProcessingService;

    @BeforeEach
    void setUp() {
        validatorService = mock(ValidatorService.class);
        jsonProcessingService = new JsonProcessingService(validatorService);
    }

    @Test
    void saveToJson_shouldCreateJsonFile_whenValidTrucksProvided() throws IOException {
        // Arrange
        PackageStartPosition position = new PackageStartPosition(0, 0);
        Package pkg = new Package(PackageType.TWO, 1, position);
        Truck truck = new Truck();
        truck.getPackages().add(pkg);
        List<Truck> trucks = List.of(truck);

        File outputFile = new File("out/trucks.json");
        if (outputFile.exists()) {
            outputFile.delete();
        }

        // Act
        jsonProcessingService.saveToJson(trucks);

        // Assert
        assertThat(outputFile).exists();
        String jsonContent = Files.readString(outputFile.toPath());
        assertThat(jsonContent).contains("truck_id", "packages", "position");
    }

    @Test
    void saveToJson_shouldLogWarning_whenPackagePositionIsNull() {
        // Arrange
        Package pkg = new Package(PackageType.TWO, 1, null); // Нет позиции
        Truck truck = new Truck();
        truck.getPackages().add(pkg);
        List<Truck> trucks = List.of(truck);

        // Act
        jsonProcessingService.saveToJson(trucks);

        // Assert
        verifyNoInteractions(validatorService);
    }

    @Test
    void saveToJson_shouldLogError_whenOutputDirectoryCannotBeCreated() {
        // Arrange
        String invalidDirPath = "out/invalid-dir"; // Указываем недопустимую директорию
        File outputDir = new File(invalidDirPath);
        outputDir.setWritable(false); // Делаем директорию недоступной для записи

        Package pkg = new Package(PackageType.TWO, 1, new PackageStartPosition(0, 0));
        Truck truck = new Truck();
        truck.getPackages().add(pkg);
        List<Truck> trucks = List.of(truck);

        // Act
        jsonProcessingService.saveToJson(trucks);

        // Assert
        outputDir.setWritable(true);
    }


    @Test
    void importJson_shouldReturnPackagesOutput_whenValidJsonProvided() throws IOException {
        // Arrange
        String validJsonContent = """
                {
                  "trucks": [
                    {
                      "truck_id": 1,
                      "packages": [
                        {
                          "id": 6,
                          "type": "NINE",
                          "position": {
                            "x": 1,
                            "y": 1
                          }
                        },
                        {
                          "id": 8,
                          "type": "NINE",
                          "position": {
                            "x": 4,
                            "y": 1
                          }
                        }
                      ]
                    },
                    {
                      "truck_id": 2,
                      "packages": [
                        {
                          "id": 7,
                          "type": "EIGHT",
                          "position": {
                            "x": 1,
                            "y": 1
                          }
                        }
                      ]
                    }
                  ]
                }
                """;

        File tempFile = File.createTempFile("test-valid", ".json");
        Files.writeString(tempFile.toPath(), validJsonContent);

        when(validatorService.isFileExists(tempFile)).thenReturn(true);
        when(validatorService.isValidJsonStructure(any())).thenReturn(true);

        // Act
        List<String> result = jsonProcessingService.importJson(tempFile.getAbsolutePath());

        // Assert
        assertThat(result).containsExactly(
                "999", "999", "999", "", // Shape of type NINE
                "999", "999", "999", "", // Shape of type NINE
                "8888", "8888", ""  // Shape of type EIGHT
        );
    }


    @Test
    void importJson_shouldThrowException_whenFileDoesNotExist() {
        // Arrange
        String filePath = "non-existent.json";
        when(validatorService.isFileExists(any(File.class))).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> jsonProcessingService.importJson(filePath))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Файл не найден");
    }
}
