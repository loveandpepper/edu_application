package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.filesaving.FileSavingAlgorithm;
import org.hofftech.edu.service.filesaving.FileSavingAlgorithmFactory;
import org.hofftech.edu.util.FileReaderUtil;

import java.nio.file.Path;
import java.util.List;

@Slf4j
public class FileProcessingService {
    private final FileParsingService fileParser;
    private final ValidatorService validatorService;
    private final TruckService truckService;
    private final FileSavingAlgorithmFactory savingAlgorithmFactory;

    public FileProcessingService(FileParsingService fileParser,
                                 ValidatorService validatorService, TruckService truckService, FileSavingAlgorithmFactory savingAlgorithmFactory) {
        this.fileParser = fileParser;
        this.validatorService = validatorService;
        this.truckService = truckService;
        this.savingAlgorithmFactory = savingAlgorithmFactory;
    }

    public void processFile(Path filePath, boolean useEasyAlgorithm, boolean saveToFile, int maxTrucks, boolean lazyAlg) {
        List<String> lines = readFile(filePath);
        validateFile(filePath, lines);
        List<Package> packages = parseFileLines(filePath, lines);
        validatePackages(packages);
        List<Truck> trucks = addPackages(useEasyAlgorithm, packages, maxTrucks, lazyAlg);

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
            FileSavingAlgorithm algorithm = savingAlgorithmFactory.getAlgorithm("json");
            algorithm.save(trucks, "out/trucks.json");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения грузовиков в JSON", e);
        }
    }

    private void printTrucks(List<Truck> trucks) {
        truckService.printTrucks(trucks);
    }


    protected List<Truck> addPackages(boolean useEasyAlgorithm, List<Package> packages, int maxTrucks, Boolean lazyAlg) {
        List<Truck> trucks;
        if (useEasyAlgorithm) {
            trucks = truckService.addPackagesToIndividualTrucks(packages);
        } else {
            trucks = truckService.addPackagesToMultipleTrucks(packages, maxTrucks, lazyAlg);
        }
        return trucks;
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
