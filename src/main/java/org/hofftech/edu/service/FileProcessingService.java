package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.util.FileParserUtil;
import org.hofftech.edu.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.List;

@Slf4j
public class FileProcessingService {
    private final FileReaderUtil fileReader;
    private final FileParserUtil fileParser;
    private final ValidatorService validatorService;
    private final TruckService truckService;

    public FileProcessingService(FileReaderUtil fileReader, FileParserUtil fileParser,
                                 ValidatorService validatorService, TruckService truckService) {
        this.fileReader = fileReader;
        this.fileParser = fileParser;
        this.validatorService = validatorService;
        this.truckService = truckService;
    }

    public void processFile(Path filePath, boolean useEasyAlgorithm) {
        try {
            log.info("Начало обработки файла: {}. Используем простой алгоритм: {}", filePath, useEasyAlgorithm);

            List<String> lines = fileReader.readAllLines(filePath);
            validateFile(filePath, lines);
            List<Package> packages = parseFileLines(filePath, lines);
            validatePackages(packages);
            List<Truck> trucks = addPackages(useEasyAlgorithm, packages);

            log.info("Упаковки распределены по {} грузовикам.", trucks.size());
            truckService.printTrucks(trucks);
        } catch (Exception e) {
            log.error("Произошла ошибка при обработке файла: {}", filePath, e);
        }
    }

    private List<Truck> addPackages(boolean useEasyAlgorithm, List<Package> packages) {
        // Распределение упаковок по грузовикам
        List<Truck> trucks;
        if (useEasyAlgorithm) {
            trucks = truckService.addPackagesToIndividualTrucks(packages);
        } else {
            trucks = truckService.addPackagesToMultipleTrucks(packages);
        }
        return trucks;
    }

    private void validatePackages(List<Package> packages) {
        // Валидация упаковок
        if (!validatorService.isValidPackages(packages)) {
            log.warn("Не все упаковки прошли валидацию.");
        }
        log.info("Все упаковки успешно прошли валидацию.");
    }

    private List<Package> parseFileLines(Path filePath, List<String> lines) {
        // Парсинг строк в упаковки
        List<Package> packages = fileParser.parsePackages(lines);
        if (packages.isEmpty()) {
            log.warn("Не удалось распарсить ни одной упаковки из файла: {}", filePath);
        }
        log.info("Успешно распарсено {} упаковок.", packages.size());
        return packages;
    }

    private void validateFile(Path filePath, List<String> lines) {
        // Валидация файла
        if (!validatorService.isValidFile(lines)) {
            log.warn("Файл не прошел валидацию: {}", filePath);
        }
        log.info("Файл успешно прошел валидацию.");
    }
}
