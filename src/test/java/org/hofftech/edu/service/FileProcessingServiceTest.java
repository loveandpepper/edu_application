package org.hofftech.edu.service;

import lombok.SneakyThrows;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.util.FileParserUtil;
import org.hofftech.edu.util.FileReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;



class FileProcessingServiceTest {

    private FileReaderUtil fileReader;
    private FileParserUtil fileParser;
    private ValidatorService validatorService;
    private TruckService truckService;
    private JsonProcessingService jsonProcessingService;
    private FileProcessingService fileProcessingService;

    @BeforeEach
    void setUp() {
        fileReader = mock(FileReaderUtil.class);
        fileParser = mock(FileParserUtil.class);
        validatorService = mock(ValidatorService.class);
        truckService = mock(TruckService.class);
        jsonProcessingService = mock(JsonProcessingService.class);

        fileProcessingService = new FileProcessingService(
                fileReader, fileParser, validatorService, truckService, jsonProcessingService
        );
    }

    @SneakyThrows
    @Test
    void processFile_givenValidInput_shouldProcessSuccessfully() {
        // Arrange
        Path filePath = Path.of("valid-file.txt");
        List<String> fileLines = List.of("line1", "line2");
        Package testPackage = new Package(PackageType.TWO, 1, new PackageStartPosition(0, 0));
        List<Package> packages = List.of(testPackage);
        Truck truck = new Truck();
        List<Truck> trucks = List.of(truck);

        when(fileReader.readAllLines(filePath)).thenReturn(fileLines);
        when(validatorService.isValidFile(fileLines)).thenReturn(true);
        when(fileParser.parsePackages(fileLines)).thenReturn(packages);
        when(validatorService.isValidPackages(packages)).thenReturn(true);
        when(truckService.addPackagesToMultipleTrucks(eq(packages), anyInt(), anyBoolean())).thenReturn(trucks);

        // Act
        fileProcessingService.processFile(filePath, false, true, 10, false);

        // Assert
        verify(fileReader).readAllLines(filePath);
        verify(validatorService).isValidFile(fileLines);
        verify(fileParser).parsePackages(fileLines);
        verify(validatorService).isValidPackages(packages);
        verify(truckService).addPackagesToMultipleTrucks(packages, 10, false);
        verify(jsonProcessingService).saveToJson(trucks);
    }

    @SneakyThrows
    @Test
    void processFile_givenInvalidFile_shouldThrowException() {
        // Arrange
        Path filePath = Path.of("111");
        List<String> fileLines = List.of("invalid line");

        when(fileReader.readAllLines(filePath)).thenReturn(fileLines);
        when(validatorService.isValidFile(fileLines)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> fileProcessingService.processFile(filePath, false, false, 1, false))
                .isInstanceOf(RuntimeException.class);

        verify(fileReader).readAllLines(filePath);
        verify(validatorService).isValidFile(fileLines);
        verifyNoInteractions(fileParser, truckService, jsonProcessingService);
    }

    @Test
    void addPackages_givenUseEasyAlgorithm_shouldCallCorrectMethod() {
        // Arrange
        Package testPackage = new Package(PackageType.TWO, 1, new PackageStartPosition(0, 0));
        List<Package> packages = List.of(testPackage);
        Truck truck = new Truck();
        List<Truck> trucks = List.of(truck);

        when(truckService.addPackagesToIndividualTrucks(packages)).thenReturn(trucks);

        // Act
        List<Truck> result = fileProcessingService.addPackages(true, packages, 0, false);

        // Assert
        assertThat(result).isNotEmpty().hasSize(1);
        verify(truckService).addPackagesToIndividualTrucks(packages);
        verifyNoInteractions(jsonProcessingService);
    }

    @Test
    void saveTrucksToFile_shouldCallJsonProcessingService() {
        // Arrange
        Truck truck = new Truck();
        List<Truck> trucks = List.of(truck);

        // Act
        fileProcessingService.saveTrucksToFile(trucks);

        // Assert
        verify(jsonProcessingService).saveToJson(trucks);
    }

    @Test
    void parseFileLines_givenEmptyLines_shouldLogWarning() {
        // Arrange
        Path filePath = Path.of("empty.txt");
        List<String> fileLines = List.of();

        when(fileParser.parsePackages(fileLines)).thenReturn(List.of());

        // Act
        List<Package> result = fileProcessingService.parseFileLines(filePath, fileLines);

        // Assert
        assertThat(result).isEmpty();
        verify(fileParser).parsePackages(fileLines);
    }
}
