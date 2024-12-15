package org.hofftech.service;

import org.hofftech.model.Truck;
import org.hofftech.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TruckService {
    private static final Logger log = LoggerFactory.getLogger(TruckService.class);
    private final PackingService packingService;

    public TruckService(PackingService packingService) {
        this.packingService = packingService;
    }

    public List<Truck> addPackagesToMultipleTrucks(List<Package> packageList) {
        log.info("Начало размещения упаковок. Всего упаковок: {}", packageList.size());

        // Сортировка упаковок: сначала по высоте, затем по ширине
        packageList.sort((a, b) -> {
            int heightDiff = Integer.compare(b.getType().getHeight(), a.getType().getHeight());
            if (heightDiff == 0) {
                return Integer.compare(b.getType().getWidth(), a.getType().getWidth());
            }
            return heightDiff;
        });
        log.info("Упаковки отсортированы по высоте и ширине.");

        List<Truck> trucks = new ArrayList<>();
        Truck currentTruck = new Truck();
        trucks.add(currentTruck);
        log.info("Создан первый грузовик.");

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

        log.info("Размещение завершено. Всего грузовиков: {}", trucks.size());
        return trucks;
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
        for (int y = 0; y < Truck.getHEIGHT(); y++) {
            System.out.print("+");
            for (int x = 0; x < Truck.getWIDTH(); x++) {
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
