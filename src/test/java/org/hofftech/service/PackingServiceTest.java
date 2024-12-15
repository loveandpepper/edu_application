package org.hofftech.service;

import org.hofftech.model.Truck;
import org.hofftech.model.Package;
import org.hofftech.model.PackageType;
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
    public void testAddPackageSuccess() {
        Package pkg = new Package(PackageType.TWO, 1); // Упаковка 2x2

        boolean result = packingService.addPackage(truck, pkg);

        assertTrue(result, "Упаковка должна успешно добавиться в пустой грузовик");
    }

    @Test
    public void testAddPackageAtEdge() {
        Package pkg = new Package(PackageType.TWO, 1); // Упаковка 2x2

        // Добавляем упаковку в крайние координаты (максимально правый нижний угол)
        boolean result = packingService.canAddPackage(truck, pkg, Truck.getWIDTH() - 2, Truck.getHEIGHT() - 2);

        assertTrue(result, "Упаковка должна успешно добавляться на край грузовика.");
    }

    @Test
    public void testAddTwoPackagesSideBySide() {
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

