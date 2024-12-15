package org.hofftech.service;

import org.hofftech.model.Package;
import org.hofftech.model.Truck;
import org.hofftech.model.PackageType;
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
    public void testAddPackagesToMultipleTrucks_Success() {
        List<Package> packages = Arrays.asList(
                new Package(PackageType.TWO, 1),
                new Package(PackageType.TWO, 2),
                new Package(PackageType.NINE, 3),
                new Package(PackageType.EIGHT, 4),
                new Package(PackageType.EIGHT, 5),
                new Package(PackageType.SEVEN, 6)
        );

        List<Truck> trucks = truckService.addPackagesToMultipleTrucks(packages);

        assertEquals(2, trucks.size(), "Должно быть два грузовика");
        assertTrue(trucks.get(0).getPackages().size() > 0, "Первый грузовик должен содержать упаковки");
        assertTrue(trucks.get(1).getPackages().size() > 0, "Второй грузовик должен содержать упаковку");
    }

    @Test
    public void testAddPackagesToIndividualTrucks() {
        List<Package> packages = Arrays.asList(
                new Package(PackageType.ONE, 1), // 1x1
                new Package(PackageType.TWO, 2), // 2x2
                new Package(PackageType.THREE, 3) // 3x2
        );

        List<Truck> trucks = truckService.addPackagesToIndividualTrucks(packages);

        assertEquals(3, trucks.size(), "Каждая упаковка должна быть в отдельном грузовике");
        for (Truck truck : trucks) {
            assertEquals(1, truck.getPackages().size(), "Каждый грузовик должен содержать одну упаковку");
        }
    }
}
