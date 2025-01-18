package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления грузовиками и распределения посылок.
 * Отвечает за размещение посылок в грузовиках и их визуализацию.
 */
@Slf4j
@RequiredArgsConstructor
public class TruckService {

    private static final String TRUCK_SIZE_SPLITTER = "x";
    private static final int FIRST_ROW_INDEX = 0;
    private static final int FIRST_PART = 0;
    private static final int SECOND_PART = 1;
    private final PackingService packingService;

    /**
     * Добавляет посылки в несколько грузовиков.
     *
     * @param packageList список посылок
     * @param isEvenAlgorithm     флаг равномерного распределения посылок
     * @param trucksFromArgs размеры грузовиков, переданные аргументом
     * @return список грузовиков с размещёнными посылками
     */
    public List<Truck> addPackagesToMultipleTrucks(List<Package> packageList, Boolean isEvenAlgorithm, List<String> trucksFromArgs) {
        log.info("Начало размещения упаковок. Всего упаковок: {}", packageList.size());
        packageList.sort(null);
        log.info("Упаковки отсортированы по высоте и ширине.");

        List<Truck> trucks = new ArrayList<>();

        if (trucksFromArgs.isEmpty()) {
            throw new IllegalArgumentException("Аргумент с грузовиками пуст, погрузка невозможна");
        } else {
            for (String providedTruckSize : trucksFromArgs) {
                trucks.add(createTruck(providedTruckSize));
            }
            if (!isEvenAlgorithm) {
                placePackages(packageList, trucks);
            } else {
                distributePackagesEvenly(packageList, trucks);
            }
        }

        log.info("Размещение завершено. Всего грузовиков: {}", trucks.size());
        return trucks;
    }

    /**
     * Распределяет посылки равномерно между грузовиками.
     *
     * @param packages список посылок
     * @param trucks   список грузовиков
     * @throws IllegalArgumentException если список грузовиков пуст
     */
    public void distributePackagesEvenly(List<Package> packages, List<Truck> trucks) {
        if (trucks.isEmpty()) {
            throw new IllegalArgumentException("Невозможно распределить посылки: нет грузовиков.");
        }
        int totalPackages = packages.size();
        int numberOfTrucks = trucks.size();
        int currentTruckIndex = 0;

        log.info("Распределяем {} посылок на {} грузовиков.", totalPackages, numberOfTrucks);

        for (Package nextPackage : packages) {
            boolean isPlaced = false;
            for (int i = FIRST_ROW_INDEX; i < numberOfTrucks; i++) {
                Truck currentTruck = trucks.get((currentTruckIndex + i) % numberOfTrucks);
                if (packingService.tryPack(currentTruck, nextPackage)) {
                    log.info("Посылка {} успешно размещена в грузовике {}.", nextPackage.getName(), (currentTruckIndex + i) % numberOfTrucks + 1);
                    isPlaced = true;
                    currentTruckIndex = (currentTruckIndex + i + 1) % numberOfTrucks;
                    break;
                }
            }
            if (!isPlaced) {
                throw new RuntimeException("Не хватает указанных грузовиков для размещения!");
            }
        }
        log.info("Все посылки успешно распределены по грузовикам.");
    }


    private void placePackages(List<Package> packageList, List<Truck> trucks) {
        for (Package providedPackage : packageList) {
            log.info("Пытаемся разместить упаковку с ID {} и именем {}.", providedPackage.getName(), providedPackage.getName());
            boolean isPlaced = false;

            for (Truck truck : trucks) {
                if (packingService.tryPack(truck, providedPackage)) {
                    log.info("Упаковка с ID {} успешно размещена в существующем грузовике.", providedPackage.getName());
                    isPlaced = true;
                    break;
                }
            }
            if (!isPlaced) {
                log.info("Упаковка с ID {} не поместилась. Повторная попытка размещения в существующих грузовиках...", providedPackage.getName());

                for (Truck truck : trucks) {
                    if (packingService.tryPack(truck, providedPackage)) {
                        log.info("Упаковка с ID {} размещена после повторной проверки.", providedPackage.getName());
                        break;
                    }
                }
            }
        }
    }

    private Truck createTruck(String providedTruckSize) {
        String[] splitSize = providedTruckSize.split(TRUCK_SIZE_SPLITTER);
        Truck currentTruck = new Truck(Integer.parseInt(splitSize[FIRST_PART].trim()), Integer.parseInt(splitSize[SECOND_PART].trim()));
        log.info("Создан грузовик размером {}x{}.", splitSize[0].trim(), splitSize[1].trim());
        return currentTruck;
    }


    /**
     * Печатает состояние всех грузовиков.
     *
     * @param trucks список грузовиков
     */
    @SneakyThrows
    public String printTrucks(List<Truck> trucks) {
        log.info("Начало формирования состояния всех грузовиков. Всего грузовиков: {}", trucks.size());
        StringBuilder result = new StringBuilder();
        int truckNumber = 1;

        for (Truck truck : trucks) {
            result.append("Truck ").append(truckNumber).append("\n")
                    .append(truck.getWidth()).append("x").append(truck.getHeight()).append("\n");
            result.append(getTruckRepresentation(truck)).append("\n");
            truckNumber++;
        }

        return result.toString();
    }

    private String getTruckRepresentation(Truck truck) {
        StringBuilder truckRepresentation = new StringBuilder();
        truckRepresentation.append("+").append("+".repeat(truck.getWidth())).append("+\n");

        for (int y = truck.getHeight() - 1; y >= 0; y--) {
            truckRepresentation.append("+");
            for (int x = 0; x < truck.getWidth(); x++) {
                char cell = truck.getGrid()[y][x];
                truckRepresentation.append(cell == '\0' ? ' ' : cell);
            }
            truckRepresentation.append("+\n");
        }
        truckRepresentation.append("+").append("+".repeat(truck.getWidth())).append("+\n");

        return truckRepresentation.toString();
    }

    /**
     * Добавляет каждую посылку в индивидуальный грузовик.
     *
     * @param packages список посылок
     * @param providedTrucks размеры грузовиков, переданные аргументом
     * @return список грузовиков с размещёнными посылками
     */
    public List<Truck> addPackagesToIndividualTrucks(List<Package> packages, List<String> providedTrucks) {
        List<Truck> trucks = new ArrayList<>();
        int truckIndex = 0;

        for (Package pkg : packages) {
            Truck truck = createTruck(providedTrucks.get(truckIndex));
            truckIndex++;
            packingService.tryPack(truck, pkg);
            trucks.add(truck);
        }
        return trucks;
    }
}
