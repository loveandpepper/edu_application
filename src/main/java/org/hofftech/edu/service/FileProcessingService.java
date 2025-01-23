package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.OrderOperationType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.packingstategy.PackingStrategy;

import java.nio.file.Path;
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
    private final OrderManagerService orderManagerService;
    /**
     * Обрабатывает файл с посылками и параметры командной строки, выполняя упаковку в грузовики.
     *
     * @param parcelsFile    путь к файлу с посылками (может быть null, если используются данные из строки)
     * @param parcelsText    текстовый ввод с данными о посылках (если файл отсутствует)
     * @param trucksFromArgs список параметров для создания грузовиков
     * @param isEasyAlgorithm     флаг использования простого алгоритма упаковки
     * @param saveToFile     флаг, указывающий, сохранять ли данные грузовиков в файл
     * @param isEvenAlgorithm     флаг использования алгоритма равномерного распределения
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
        } else {
            return truckService.printTrucks(trucks);
        }
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
                LocalDate.now(),
                OrderOperationType.LOAD,
                truckCount,
                allPackages
        );
        orderManagerService.addOrder(order);
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
    /**
     * Сохраняет данные о грузовиках в формате JSON.
     *
     * @param trucks список грузовиков для сохранения
     */
    protected void saveTrucksToJson(List<Truck> trucks) {
        try {
            log.info("Сохраняем данные грузовиков в JSON...");
            String result = jsonProcessingService.saveToJson(trucks);
            log.info("Данные успешно сохранены в JSON: {}", result);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении данных в JSON: " + e.getMessage());
        }
    }
    /**
     * Разбирает строки файла и преобразует их в список упаковок.
     *
     * @param filePath путь к файлу
     * @param lines    строки файла
     * @return список объектов Package
     */
    protected List<Package> parseFileLines(Path filePath, List<String> lines) {
        List<Package> packages = fileParser.parsePackagesFromFile(lines);
        if (packages.isEmpty()) {
            throw new RuntimeException("Не удалось распарсить ни одной упаковки из файла: " + filePath);
        }
        log.info("Успешно распарсено {} упаковок.", packages.size());
        return packages;
    }
}
