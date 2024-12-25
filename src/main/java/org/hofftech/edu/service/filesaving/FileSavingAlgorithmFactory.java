package org.hofftech.edu.service.filesaving;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.service.JsonProcessingService;

@Slf4j
public class FileSavingAlgorithmFactory {

    private final JsonProcessingService jsonProcessingService;

    public FileSavingAlgorithmFactory(JsonProcessingService jsonProcessingService) {
        this.jsonProcessingService = jsonProcessingService;
    }

    public FileSavingAlgorithm getAlgorithm(String type) {
        if ("json".equalsIgnoreCase(type)) {
            return new JsonSavingAlgorithm(jsonProcessingService);
        }
        log.error("Неизвестный алгоритм: {}", type);
        throw new RuntimeException("Неизвестный тип алгоритма: " + type);
    }
}
