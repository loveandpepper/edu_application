package org.hofftech.edu.service;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.PackageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TruckServiceTest {
    private TruckService truckService;

    @BeforeEach
    public void setUp() {
        PackingService packingService = new PackingService();
        truckService = new TruckService(packingService);
    }

    @Test
    public void givenMultiplePackages_whenAddToTrucks_thenDistributeAcrossTrucks() {
        // Arrange: Создаём список упаковок
        List<Package> packages = Arrays.asList(
                new Package(PackageType.TWO, 1),
                new Package(PackageType.TWO, 2),
                new Package(PackageType.NINE, 3),
                new Package(PackageType.EIGHT, 4),
                new Package(PackageType.EIGHT, 5),
                new Package(PackageType.SEVEN, 6)
        );

        // Act: Распределяем упаковки по грузовикам
        List<Truck> trucks = truckService.addPackagesToMultipleTrucks(packages);

        // Assert: Проверяем результаты
        assertEquals(2, trucks.size(), "Должно быть два грузовика");
        assertFalse(trucks.get(0).getPackages().isEmpty(), "Первый грузовик должен содержать упаковки");
        assertFalse(trucks.get(1).getPackages().isEmpty(), "Второй грузовик должен содержать упаковку");
    }

    @Test
    public void givenMultiplePackages_whenAddToIndividualTrucks_thenEachInSeparateTruck() {
        // Arrange: Создаём список упаковок
        List<Package> packages = Arrays.asList(
                new Package(PackageType.ONE, 1), // 1x1
                new Package(PackageType.TWO, 2), // 2x2
                new Package(PackageType.THREE, 3) // 3x2
        );

        // Act: Каждая упаковка отправляется в свой грузовик
        List<Truck> trucks = truckService.addPackagesToIndividualTrucks(packages);

        // Assert: Проверяем, что каждая упаковка в своём грузовике
        assertEquals(3, trucks.size(), "Каждая упаковка должна быть в отдельном грузовике");
        for (Truck truck : trucks) {
            assertEquals(1, truck.getPackages().size(), "Каждый грузовик должен содержать одну упаковку");
        }
    }
}
