package org.hofftech.edu.service;

import org.hofftech.edu.model.Package;

import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PackingServiceTest {

    private PackingService packingService;

    @BeforeEach
    void setUp() {
        packingService = new PackingService();
    }

    @Test
    void canAddPackage_shouldReturnTrue_whenPackageFits() {
        // Arrange
        Truck truck = new Truck();
        Package pkg = new Package(PackageType.TWO, 1, null); // Упаковка типа TWO

        // Act
        boolean result = packingService.canAddPackage(truck, pkg, 0, 0);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void canAddPackage_shouldReturnFalse_whenPackageExceedsTruckBounds() {
        // Arrange
        Truck truck = new Truck();
        Package pkg = new Package(PackageType.FOUR, 1, null); // Упаковка слишком большая

        // Act
        boolean result = packingService.canAddPackage(truck, pkg, 4, 4); // Выходит за границы

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void canAddPackage_shouldReturnFalse_whenPackageOverlaps() {
        // Arrange
        Truck truck = new Truck();
        Package pkg1 = new Package(PackageType.TWO, 1, null);
        Package pkg2 = new Package(PackageType.TWO, 2, null);

        // Устанавливаем первую упаковку в грузовик
        packingService.placePackage(truck, pkg1, 0, 0);

        // Act
        boolean result = packingService.canAddPackage(truck, pkg2, 0, 0); // Пересекается с первой

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void addPackage_shouldReturnTrue_whenPackageIsAdded() {
        // Arrange
        Truck truck = new Truck();
        Package pkg = new Package(PackageType.TWO, 1, null);

        // Act
        boolean result = packingService.addPackage(truck, pkg);

        // Assert
        assertThat(result).isTrue();
        assertThat(pkg.getPackageStartPosition()).isNotNull();
        assertThat(truck.getPackages()).contains(pkg);
    }

    @Test
    void canAddPackage_shouldReturnFalse_whenPackageCannotBeAddedDueToOverlap() {
        // Arrange
        Truck truck = new Truck();
        Package pkg1 = new Package(PackageType.SIX, 1, null); // Занимает много места
        Package pkg2 = new Package(PackageType.SIX, 2, null); // Перекрывает первую упаковку

        // Устанавливаем первую упаковку на место
        packingService.placePackage(truck, pkg1, 1, 1);

        // Act
        boolean result = packingService.canAddPackage(truck, pkg2, 2, 1); // Проверяем пересечение

        // Assert
        assertThat(result).isFalse();
    }


    @Test
    void placePackage_shouldCorrectlyPlacePackageOnTruck() {
        // Arrange
        Truck truck = new Truck();
        Package pkg = new Package(PackageType.TWO, 1, null);

        // Act
        packingService.placePackage(truck, pkg, 1, 1);

        // Assert
        assertThat(truck.getGrid()[1][1]).isEqualTo('2'); // Проверяем, что символ упаковки размещен
        assertThat(pkg.getPackageStartPosition()).isNotNull();
        assertThat(pkg.getPackageStartPosition().getX()).isEqualTo(1);
        assertThat(pkg.getPackageStartPosition().getY()).isEqualTo(1);
        assertThat(truck.getPackages()).contains(pkg);
    }
}
