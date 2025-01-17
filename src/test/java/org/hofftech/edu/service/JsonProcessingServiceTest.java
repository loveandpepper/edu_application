package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class JsonProcessingServiceTest {

    private JsonProcessingService jsonProcessingService;

    @BeforeEach
    void setUp() {
        jsonProcessingService = new JsonProcessingService();
    }

    @Test
    void shouldSaveTrucksToJson() {
        List<Truck> trucks = List.of(new Truck(6, 6));
        String result = jsonProcessingService.saveToJson(trucks);

        Assertions.assertThat(result).isNotBlank();
        Assertions.assertThat(result).contains("trucks");
    }

    @Test
    void shouldImportPackagesFromJson() {
        List<Map<String, Long>> result = jsonProcessingService.importPackagesFromJson("out/trucks.json", true);

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionForInvalidJsonFile() {
        String invalidJsonPath = "invalid.json";

        Assertions.assertThatThrownBy(() -> jsonProcessingService.importPackagesFromJson(invalidJsonPath, true))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Файл не найден");
    }
}

