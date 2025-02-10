package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackingServiceTest {

    private PackingService packingService;

    @BeforeEach
    void setUp() {
        packingService = new PackingService();
    }

    @Test
    void shouldAddPackageToTruckSuccessfully() {
        Truck truck = new Truck(6, 6);
        Package pkg = new Package("Посылка Тип 1", new String[]{"1"}, '1', 0, 0);

        boolean result = packingService.tryPack(truck, pkg);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldFailToAddPackageDueToLimits() {
        Truck truck = new Truck(6, 6);
        Package pkg = new Package("Посылка Тип 1111", new String[]{"1111111111"}, '1', 0, 0);

        boolean result = packingService.tryPack(truck, pkg);

        Assertions.assertThat(result).isFalse();
    }
}

