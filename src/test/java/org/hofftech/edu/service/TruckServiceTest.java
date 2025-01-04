package org.hofftech.edu.service;

import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TruckServiceTest {

    private PackingService packingService;
    private TruckService truckService;

    @BeforeEach
    void setUp() {
        packingService = mock(PackingService.class);
        truckService = new TruckService(packingService);
    }

    @Test
    void addPackagesToMultipleTrucks_shouldDistributePackagesAcrossMultipleTrucks() {
        // Arrange
        List<Package> packages = new ArrayList<>(List.of(
                new Package(PackageType.TWO, 1, null),
                new Package(PackageType.TWO, 2, null),
                new Package(PackageType.TWO, 3, null)
        ));

        when(packingService.addPackage(any(Truck.class), any(Package.class))).thenReturn(true);

        // Act
        List<Truck> trucks = truckService.addPackagesToMultipleTrucks(packages, 2, true);

        // Assert
        assertThat(trucks).hasSizeGreaterThan(1); //проверили, что больше 1го грузовика при even
        verify(packingService, times(packages.size())).addPackage(any(Truck.class), any(Package.class));
    }


    @Test
    void addPackagesToMultipleTrucks_shouldThrowException_whenLimitExceeded() {
        // Arrange
        List<Package> packages = List.of(
                new Package(PackageType.SIX, 1, null),
                new Package(PackageType.SIX, 2, null),
                new Package(PackageType.SIX, 3, null)
        );

        when(packingService.addPackage(any(Truck.class), any(Package.class))).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> truckService.addPackagesToMultipleTrucks(packages, 1, false))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void distributePackagesEvenly_shouldDistributePackagesCorrectly() {
        // Arrange
        List<Truck> trucks = List.of(new Truck(), new Truck());
        List<Package> packages = List.of(
                new Package(PackageType.TWO, 1, null),
                new Package(PackageType.TWO, 2, null),
                new Package(PackageType.TWO, 3, null),
                new Package(PackageType.TWO, 4, null)
        );

        when(packingService.addPackage(any(Truck.class), any(Package.class))).thenReturn(true);

        // Act
        truckService.distributePackagesEvenly(packages, trucks);

        // Assert
        verify(packingService, times(packages.size())).addPackage(any(Truck.class), any(Package.class));
        assertThat(trucks).hasSize(2);
    }

    @Test
    void distributePackagesEvenly_shouldThrowException_whenNoTrucksProvided() {
        // Arrange
        List<Truck> trucks = List.of();
        List<Package> packages = List.of(new Package(PackageType.TWO, 1, null));

        // Act & Assert
        assertThatThrownBy(() -> truckService.distributePackagesEvenly(packages, trucks))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("нет грузовиков");
    }

    @Test
    void addPackagesToIndividualTrucks_shouldCreateOneTruckPerPackage() {
        // Arrange
        List<Package> packages = List.of(
                new Package(PackageType.TWO, 1, null),
                new Package(PackageType.TWO, 2, null)
        );

        when(packingService.addPackage(any(Truck.class), any(Package.class))).thenReturn(true);

        // Act
        List<Truck> trucks = truckService.addPackagesToIndividualTrucks(packages);

        // Assert
        assertThat(trucks).hasSize(packages.size());
        verify(packingService, times(packages.size())).addPackage(any(Truck.class), any(Package.class));
    }

    @Test
    void printTrucks_shouldPrintTruckDetails() {
        // Arrange
        Truck truck1 = mock(Truck.class);
        Truck truck2 = mock(Truck.class);
        List<Truck> trucks = List.of(truck1, truck2);

        // Act
        truckService.printTrucks(trucks);

        // Assert
        verify(truck1, times(0)).getGrid();
        verify(truck2, times(0)).getGrid();
    }
}
