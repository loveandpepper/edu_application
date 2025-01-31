package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.model.Truck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

class JsonProcessingServiceTest {

    @Mock
    private OrderManagerService orderManagerService;

    @InjectMocks
    private JsonProcessingService jsonProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveTrucksToJson() {
        List<Truck> trucks = List.of(new Truck(6, 6));
        String result = jsonProcessingService.saveToJson(trucks);

        Assertions.assertThat(result).isNotBlank().contains("trucks");
    }

    @Test
    void shouldImportPackagesFromJson() {
        List<Map<String, Long>> result = jsonProcessingService.importPackagesFromJson("out/trucks.json",
                true, "testUser");

        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionForInvalidJsonFile() {
        String invalidJsonPath = "out/invalid.json";

        Assertions.assertThatThrownBy(() -> jsonProcessingService.importPackagesFromJson(invalidJsonPath,
                        true, "testUser"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Файл не найден");
    }
}

