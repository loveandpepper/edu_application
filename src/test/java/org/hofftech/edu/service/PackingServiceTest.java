package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PackingServiceTest {

    private PackingService packingService;

    @BeforeEach
    void setUp() {
        packingService = new PackingService();
    }

    @Test
    void shouldAddPackageToTruckSuccessfully() {
        Truck truck = new Truck(6, 6);
        Package pkg = new Package("Посылка Тип 1", List.of("1"), '1', null);

        boolean result = packingService.tryPack(truck, pkg);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void shouldFailToAddPackageDueToLimits() {
        Truck truck = new Truck(6, 6);
        Package pkg = new Package("Посылка Тип 1111", List.of("1111111111"), '1', null);

        boolean result = packingService.tryPack(truck, pkg);

        Assertions.assertThat(result).isFalse();
    }
}

