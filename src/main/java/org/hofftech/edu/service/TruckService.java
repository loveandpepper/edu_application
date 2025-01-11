package org.hofftech.edu.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Сервис для управления грузовиками и распределения посылок.
 * Отвечает за размещение посылок в грузовиках и их визуализацию.
 */
@Slf4j
@RequiredArgsConstructor
public class TruckService {
    private static final String TRUCK_STANDARD_SIZE = "6x6";
    private final PackingService packingService;

    /**
     * Добавляет посылки в несколько грузовиков.
     *
     * @param packageList список посылок
     * @param evenAlg     флаг равномерного распределения посылок
     * @param trucksFromArgs размеры грузовиков, переданные аргументом
     * @return список грузовиков с размещёнными посылками
     */
    public List<Truck> addPackagesToMultipleTrucks(List<Package> packageList, Boolean evenAlg, List<String> trucksFromArgs) {
        log.info("Начало размещения упаковок. Всего упаковок: {}", packageList.size());
        sortPackages(packageList);

        List<Truck> trucks = new ArrayList<>();

        if (trucksFromArgs.isEmpty()) {
            log.info("Массив грузовиков пуст. Используем стандартные размеры 6x6 и плотное размещение.");
            placePackagesInStandardTrucks(packageList, trucks);
        } else {
            for (String providedTruckSize : trucksFromArgs) {
                trucks.add(createTruck(providedTruckSize));
            }
            if (!evenAlg) {
                placePackages(packageList, trucks);
            } else {
                distributePackagesEvenly(packageList, trucks);
            }
        }

        log.info("Размещение завершено. Всего грузовиков: {}", trucks.size());
        return trucks;
    }

    private void placePackagesInStandardTrucks(List<Package> packageList, List<Truck> trucks) {
        while (!packageList.isEmpty()) {
            Truck truck = createTruck(TRUCK_STANDARD_SIZE);
            Iterator<Package> iterator = packageList.iterator();

            while (iterator.hasNext()) {
                Package pkg = iterator.next();
                if (packingService.addPackage(truck, pkg)) {
                    iterator.remove();
                    log.info("Упаковка {} успешно размещена в грузовике с размерами 6x6.", pkg.getName());
                } else {
                    log.info("Упаковка {} не помещается в текущий грузовик. Создаем новый.", pkg.getName());
                }
            }
            trucks.add(truck);
        }
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
            log.error("Количество грузовиков не может быть нулевым.");
            throw new IllegalArgumentException("Невозможно распределить посылки: нет грузовиков.");
        }
        int totalPackages = packages.size();
        int numberOfTrucks = trucks.size();
        int currentTruckIndex = 0;

        log.info("Распределяем {} посылок на {} грузовиков.", totalPackages, numberOfTrucks);

        for (Package pkg : packages) {
            boolean placed = false;
            for (int i = 0; i < numberOfTrucks; i++) {
                Truck currentTruck = trucks.get((currentTruckIndex + i) % numberOfTrucks);
                if (packingService.addPackage(currentTruck, pkg)) {
                    log.info("Посылка {} успешно размещена в грузовике {}.", pkg.getName(), (currentTruckIndex + i) % numberOfTrucks + 1);
                    placed = true;
                    currentTruckIndex = (currentTruckIndex + i + 1) % numberOfTrucks;
                    break;
                }
            }
            if (!placed) {
                log.error("Не удалось разместить посылку {} в любом из грузовиков.", pkg.getName());
                throw new RuntimeException("Не хватает указанных грузовиков для размещения!");
            }
        }
        log.info("Все посылки успешно распределены по грузовикам.");
    }


    private void placePackages(List<Package> packageList, List<Truck> trucks) {
        int nextTruckIndex = trucks.size();
        for (Package pkg : packageList) {
            log.info("Пытаемся разместить упаковку с ID {} и именем {}.", pkg.getName(), pkg.getName());
            boolean placed = false;

            for (Truck truck : trucks) {
                if (packingService.addPackage(truck, pkg)) {
                    log.info("Упаковка с ID {} успешно размещена в существующем грузовике.", pkg.getName());
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                log.info("Упаковка с ID {} не поместилась. Повторная попытка размещения в существующих грузовиках...", pkg.getName());

                for (Truck truck : trucks) {
                    if (packingService.addPackage(truck, pkg)) {
                        log.info("Упаковка с ID {} размещена после повторной проверки.", pkg.getName());
                        placed = true;
                        break;
                    }
                }
            }
            if (!placed) {
                if (nextTruckIndex < trucks.size()) {
                    Truck newTruck = trucks.get(nextTruckIndex);
                    nextTruckIndex++;

                    if (packingService.addPackage(newTruck, pkg)) {
                        log.info("Упаковка с ID {} размещена в новом грузовике.", pkg.getName());
                    } else {
                        log.error("Ошибка: упаковка с ID {} не может быть размещена даже в новом грузовике.", pkg.getName());
                    }
                } else {
                    log.error("Все грузовики уже использованы. Упаковка с ID {} не может быть размещена.", pkg.getName());
                    throw new RuntimeException("Все доступные грузовики использованы, упаковка не может быть размещена.");
                }
            }
        }
    }

    private static Truck createTruck(String providedTruckSize) {
        if (providedTruckSize == null || providedTruckSize.isEmpty()) {
            providedTruckSize = TRUCK_STANDARD_SIZE;
        }
        String[] splitSize = providedTruckSize.split("x");
        Truck currentTruck = new Truck(Integer.parseInt(splitSize[0].trim()), Integer.parseInt(splitSize[1].trim()));
        log.info("Создан грузовик размером {}x{}.", splitSize[0].trim(), splitSize[1].trim());
        return currentTruck;
    }

    private static void sortPackages(List<Package> packageList) {
        packageList.sort((a, b) -> {
            int heightDiff = Integer.compare(b.getHeight(), a.getHeight());
            if (heightDiff == 0) {
                return Integer.compare(b.getWidth(), a.getWidth());
            }
            return heightDiff;
        });
        log.info("Упаковки отсортированы по высоте и ширине.");
    }

    /**
     * Печатает состояние всех грузовиков.
     *
     * @param trucks список грузовиков
     */
    @SneakyThrows
    public void printTrucks(List<Truck> trucks) {
        log.info("Начало вывода состояния всех грузовиков. Всего грузовиков: {}", trucks.size());
        int truckNumber = 1;
        for (Truck truck : trucks) {
            System.out.println("Truck " + truckNumber + "\n" + truck.getWidth() + "x" + truck.getHeight());
            printTruck(truck);
            truckNumber++;
        }
    }

    private void printTruck(Truck truck) {
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
        System.out.println(String.valueOf(truckRepresentation));
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
            if (truckIndex >= providedTrucks.size()) {
                log.info("Массив переданных грузовиков пуст, будет создан стандартный грузовик");
                providedTrucks.add(TRUCK_STANDARD_SIZE);
            }
            Truck truck = createTruck(providedTrucks.get(truckIndex));
            truckIndex++;
            packingService.addPackage(truck, pkg);
            trucks.add(truck);
        }
        return trucks;
    }
}
