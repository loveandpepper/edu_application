package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class TruckServiceTest {

    private PackingService packingServiceMock;
    private TruckService truckService;

    @BeforeEach
    void setUp() {
        packingServiceMock = Mockito.mock(PackingService.class);
        truckService = new TruckService(packingServiceMock);
    }


    @Disabled
    @Test
    void shouldAddPackagesToMultipleTrucks() {
        List<Package> packages = List.of(new Package("Посылка Тип 1", null, '1', 0, 0),
                new Package("Посылка Тип 2", new String[]{"22"}, '2', 0, 0));
        List<String> trucksFromArgs = List.of("6x6", "6x5");

        Mockito.when(packingServiceMock.tryPack(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        List<Truck> result = truckService.addPackagesToMultipleTrucks(packages, false, trucksFromArgs);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1);
    }


    @Disabled
    @Test
    void shouldThrowExceptionForEmptyTrucksList() {
        List<Package> packages = List.of(new Package("Посылка Тип 1", null, '1', 0, 0),
                new Package("Посылка Тип 3", null, '3', 0, 0));

        Assertions.assertThatThrownBy(() -> truckService.addPackagesToMultipleTrucks(packages, false, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Аргумент с грузовиками пуст, погрузка невозможна");
    }

    @Test
    void shouldDistributePackagesEvenly() {
        List<Package> packages = List.of(new Package("Посылка Тип 1", null, '1', 0, 0),
                new Package("Посылка Тип 2", null, '2', 0, 0));
        List<Truck> trucks = List.of(new Truck(6, 6), new Truck(6, 6));

        Mockito.when(packingServiceMock.tryPack(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        Assertions.assertThatCode(() -> truckService.distributePackagesEvenly(packages, trucks))
                .doesNotThrowAnyException();
    }


    @Test
    void shouldThrowExceptionWhenDistributingWithoutTrucks() {
        List<Package> packages = List.of(new Package("Посылка Тип 1", null, '1', 0, 0));

        Assertions.assertThatThrownBy(() -> truckService.distributePackagesEvenly(packages, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Невозможно распределить посылки: нет грузовиков.");
    }
}

