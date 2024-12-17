package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.Package;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TruckService {
    private final PackingService packingService;

    public TruckService(PackingService packingService) {
        this.packingService = packingService;
    }

    public List<Truck> addPackagesToMultipleTrucks(List<Package> packageList) {
        log.info("Начало размещения упаковок. Всего упаковок: {}", packageList.size());

        sortPackages(packageList);
        List<Truck> trucks = createTruck();
        placePackages(packageList, trucks);

        log.info("Размещение завершено. Всего грузовиков: {}", trucks.size());
        return trucks;
    }

    private void placePackages(List<Package> packageList, List<Truck> trucks) {
        for (Package pkg : packageList) {
            log.info("Пытаемся разместить упаковку {} с ID {}", pkg.getType(), pkg.getId());
            boolean placed = false;
            for (Truck truck : trucks) {
                if (packingService.addPackage(truck, pkg)) {
                    log.info("Упаковка {} с ID {} успешно размещена в существующем грузовике.", pkg.getType(), pkg.getId());
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                log.info("Упаковка {} с ID {} не поместилась. Создаём новый грузовик.", pkg.getType(), pkg.getId());
                Truck newTruck = new Truck();
                if (packingService.addPackage(newTruck, pkg)) {
                    trucks.add(newTruck);
                    log.info("Упаковка {} с ID {} размещена в новом грузовике.", pkg.getType(), pkg.getId());
                } else {
                    log.error("Ошибка: упаковка {} с ID {} не может быть размещена даже в новом грузовике.", pkg.getType(), pkg.getId());
                }
            }
        }
    }

    private static List<Truck> createTruck() {
        List<Truck> trucks = new ArrayList<>();
        Truck currentTruck = new Truck();
        trucks.add(currentTruck);
        log.info("Создан первый грузовик.");
        return trucks;
    }

    private static void sortPackages(List<Package> packageList) {
        // Сортировка упаковок: сначала по высоте, затем по ширине
        packageList.sort((a, b) -> {
            int heightDiff = Integer.compare(b.getType().getHeight(), a.getType().getHeight());
            if (heightDiff == 0) {
                return Integer.compare(b.getType().getWidth(), a.getType().getWidth());
            }
            return heightDiff;
        });
        log.info("Упаковки отсортированы по высоте и ширине.");
    }

    public void printTrucks(List<Truck> trucks) {
        log.info("Начало вывода состояния всех грузовиков. Всего грузовиков: {}", trucks.size());
        int truckNumber = 1;
        for (Truck truck : trucks) {
            System.out.printf("Truck %d%n", truckNumber);
            printTruck(truck);
            truckNumber++;
        }
        log.info("Вывод завершён.");
    }

    private void printTruck(Truck truck) {
        for (int y = 0; y < truck.getHEIGHT(); y++) {
            System.out.print("+");
            for (int x = 0; x < truck.getWIDTH(); x++) {
                System.out.print(truck.getGrid()[y][x]);
            }
            System.out.println("+");
        }
        System.out.println("++++++++" + "\n");
    }

    public List<Truck> addPackagesToIndividualTrucks(List<Package> packages) {
        List<Truck> trucks = new ArrayList<>();
        for (Package pkg : packages) {
            Truck truck = new Truck();
            packingService.addPackage(truck, pkg); // Каждая упаковка размещается в отдельном грузовике
            trucks.add(truck);
            log.info("Упаковка {} добавлена в новый грузовик.", pkg.getId());
        }
        return trucks;
    }
}
