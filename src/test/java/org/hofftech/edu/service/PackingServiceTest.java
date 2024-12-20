package org.hofftech.edu.service;

import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PackingServiceTest {
    private PackingService packingService;
    private Truck truck;

    @BeforeEach
    public void setUp() {
        packingService = new PackingService();
        truck = new Truck();
    }

    @Test
    public void givenEmptyTruck_whenAddPackage_thenPackageIsAdded() {
        Package pkg = new Package(PackageType.TWO, 1); // Упаковка 2x2

        boolean result = packingService.addPackage(truck, pkg);

        assertTrue(result, "Упаковка должна успешно добавиться в пустой грузовик.");
    }

    @Test
    public void givenTruckEdgeSpace_whenAddPackage_thenFitsSuccessfully() {
        Package pkg = new Package(PackageType.TWO, 1); // Упаковка 2x2

        // Добавляем упаковку на крайние координаты
        boolean result = packingService.canAddPackage(truck, pkg, truck.getWIDTH() - 2, truck.getHEIGHT() - 2);

        assertTrue(result, "Упаковка должна успешно добавляться на край грузовика.");
    }

    @Test
    public void givenEmptyTruck_whenAddTwoPackagesSideBySide_thenBothFit() {
        Package pkg1 = new Package(PackageType.ONE, 1); // Упаковка 1x1
        Package pkg2 = new Package(PackageType.ONE, 2);

        // Добавляем первую упаковку в начало координат
        boolean firstResult = packingService.addPackage(truck, pkg1);

        // Добавляем вторую упаковку рядом с первой
        boolean secondResult = packingService.canAddPackage(truck, pkg2, 1, 0);

        assertTrue(firstResult, "Первая упаковка должна добавиться.");
        assertTrue(secondResult, "Вторая упаковка должна размещаться рядом с первой.");
    }

}

