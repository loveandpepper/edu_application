package org.hofftech.service;

import org.hofftech.model.Package;
import org.hofftech.model.Truck;
import org.hofftech.util.FileParserUtil;
import org.hofftech.util.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class FileProcessingService {
    private static final Logger log = LoggerFactory.getLogger(FileProcessingService.class);

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

            // Чтение строк из файла
            List<String> lines = fileReader.readAllLines(filePath);
            if (lines.isEmpty()) {
                log.warn("Файл пуст или не удалось его прочитать: {}", filePath);
            }
            log.info("Файл успешно прочитан. Количество строк: {}", lines.size());

            // Валидация файла
            if (!validatorService.validateFile(lines)) {
                log.warn("Файл не прошел валидацию: {}", filePath);
            }
            log.info("Файл успешно прошел валидацию.");

            // Парсинг строк в упаковки
            List<Package> packages = fileParser.parsePackages(lines);
            if (packages.isEmpty()) {
                log.warn("Не удалось распарсить ни одной упаковки из файла: {}", filePath);
            }
            log.info("Успешно распарсено {} упаковок.", packages.size());

            // Валидация упаковок
            if (!validatorService.validatePackages(packages)) {
                log.warn("Не все упаковки прошли валидацию.");
            }
            log.info("Все упаковки успешно прошли валидацию.");

            // Распределение упаковок по грузовикам
            List<Truck> trucks;
            if (useEasyAlgorithm) {
                trucks = truckService.addPackagesToIndividualTrucks(packages);
            } else {
                trucks = truckService.addPackagesToMultipleTrucks(packages);
            }
            log.info("Упаковки распределены по {} грузовикам.", trucks.size());
            truckService.printTrucks(trucks);

        } catch (Exception e) {
            log.error("Произошла ошибка при обработке файла: {}", filePath, e);
        }
    }
}
