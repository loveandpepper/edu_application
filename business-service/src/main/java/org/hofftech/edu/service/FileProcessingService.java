package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.FileParsingException;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.OrderOperationType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.packingstategy.PackingStrategy;

import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для обработки файлов, содержащих данные об упаковках и грузовиках.
 * Отвечает за чтение, валидацию, разбор и сохранение данных.
 */
@Slf4j
@RequiredArgsConstructor
public class FileProcessingService {

    private final ParsingService fileParser;
    private final ValidatorService validatorService;
    private final TruckService truckService;
    private final JsonProcessingService jsonProcessingService;
    private final PackingStrategyFactory packingStrategyFactory;
    private final OutboxEventService outboxEventService;
    private final Clock clock;

    /**
     * Обрабатывает файл с посылками и параметры командной строки, выполняя упаковку в грузовики.
     */

    public String processFile(Path parcelsFile, String parcelsText, List<String> trucksFromArgs,
                              boolean isEasyAlgorithm, boolean saveToFile, boolean isEvenAlgorithm, String user) {
        List<Package> packages = getPackagesFromFileOrArgs(parcelsFile, parcelsText);
        PackingStrategy strategy = packingStrategyFactory.createStrategy(isEasyAlgorithm);
        List<Truck> trucks = strategy.addPackages(packages, isEasyAlgorithm, isEvenAlgorithm, trucksFromArgs);

        addLoadOrder(trucks, user);

        if (saveToFile) {
            saveTrucksToJson(trucks);
            return "Данные успешно сохранены в файл.";
        }

        return truckService.printTrucks(trucks);
    }


    private void addLoadOrder(List<Truck> trucks, String userId) {
        List<Package> allPackages = new ArrayList<>();
        int truckCount = 0;
        for (Truck truck : trucks) {
            List<Package> packagesFromTruck = truck.getPackages();
            if (!packagesFromTruck.isEmpty()) {
                allPackages.addAll(truck.getPackages());
                truckCount++;
            }
        }

        Order order = new Order(
                userId,
                LocalDate.ofInstant(clock.instant(), clock.getZone()),
                OrderOperationType.LOAD,
                truckCount,
                allPackages
        );

        outboxEventService.saveOrderEvent(order,"ORDER_CREATED");
        log.info("Заказ на погрузку добавлен для пользователя {}", userId);
    }


    private List<Package> getPackagesFromFileOrArgs(Path parcelsFile, String parcelsText) {
        List<Package> packages = new ArrayList<>();
        if (parcelsFile != null) {
            List<String> lines = FileReaderService.readAllLines(parcelsFile);
            validatorService.validateFile(lines);
            packages = parseFileLines(parcelsFile, lines);
        } else if (parcelsText != null && !parcelsText.isEmpty()) {
            packages = fileParser.getPackagesFromArgs(parcelsText);
        }
        if (packages.isEmpty()) {
            throw new IllegalArgumentException("Упаковки не представлены, продолжение работы невозможно");
        }
        return packages;
    }

    protected void saveTrucksToJson(List<Truck> trucks) {
        log.info("Сохраняем данные грузовиков в JSON...");
        String result = jsonProcessingService.saveToJson(trucks);
        log.info("Данные успешно сохранены в JSON: {}", result);

    }

    protected List<Package> parseFileLines(Path filePath, List<String> lines) {
        List<Package> packages = fileParser.parsePackagesFromFile(lines);
        if (packages.isEmpty()) {
            throw new FileParsingException("Не удалось распарсить ни одной упаковки из файла: " + filePath);
        }
        log.info("Успешно распарсено {} упаковок.", packages.size());
        return packages;
    }
}
