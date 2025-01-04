package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.packingstategy.PackingStrategy;
import org.hofftech.edu.service.packingstategy.PackingStrategyFactory;
import org.hofftech.edu.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.List;


@Slf4j
public class FileProcessingService {
    private final FileParsingService fileParser;
    private final ValidatorService validatorService;
    private final TruckService truckService;
    private final JsonProcessingService jsonProcessingService;
    private final PackingStrategyFactory packingStrategyFactory;

    public FileProcessingService(FileParsingService fileParser,
                                 ValidatorService validatorService, TruckService truckService, JsonProcessingService jsonProcessingService, PackingStrategyFactory packingStrategyFactory) {
        this.fileParser = fileParser;
        this.validatorService = validatorService;
        this.truckService = truckService;
        this.jsonProcessingService = jsonProcessingService;
        this.packingStrategyFactory = packingStrategyFactory;
    }

    public void processFile(Path filePath, boolean useEasyAlg, boolean saveToFile, int maxTrucks, boolean useEvenAlg) {
        List<String> lines = readFile(filePath);
        validateFile(filePath, lines);
        List<Package> packages = parseFileLines(filePath, lines);
        validatePackages(packages);
        PackingStrategy strategy = packingStrategyFactory.getStrategy(useEasyAlg);
        List<Truck> trucks = strategy.addPackages(packages, maxTrucks, useEasyAlg, useEvenAlg);

        if (saveToFile) {
            saveTrucksToJson(trucks);
        } else {
            printTrucks(trucks);
        }
    }

    private List<String> readFile(Path filePath) {
            return FileReaderUtil.readAllLines(filePath);
    }

    protected void saveTrucksToJson(List<Truck> trucks) {
        try {
            log.info("Сохраняем данные грузовиков в JSON...");
            String result = jsonProcessingService.saveToJson(trucks);
            log.info("Данные успешно сохранены в JSON: {}", result);
        } catch (Exception e) {
            log.error("Ошибка при сохранении данных в JSON: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void printTrucks(List<Truck> trucks) {
        truckService.printTrucks(trucks);
    }

    private void validatePackages(List<Package> packages) {
        if (!validatorService.isValidPackages(packages)) {
            log.warn("Не все упаковки прошли валидацию.");
        }
        log.info("Все упаковки успешно прошли валидацию.");
    }

    protected List<Package> parseFileLines(Path filePath, List<String> lines) {
        List<Package> packages = fileParser.parsePackages(lines);
        if (packages.isEmpty()) {
            log.warn("Не удалось распарсить ни одной упаковки из файла: {}", filePath);
        }
        log.info("Успешно распарсено {} упаковок.", packages.size());
        return packages;
    }

    protected void validateFile(Path filePath, List<String> lines) {
        if (!validatorService.isValidFile(lines)) {
            log.error("Файл не прошел валидацию");
            throw new RuntimeException("Файл не прошел валидацию: " + filePath);
        }
        log.info("Файл успешно прошел валидацию.");
    }
}
