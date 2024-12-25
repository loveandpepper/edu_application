package org.hofftech.edu.service.filesaving;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.service.JsonProcessingService;

import java.util.List;

@Slf4j
public class JsonSavingAlgorithm implements FileSavingAlgorithm {

    private final JsonProcessingService jsonProcessingService;

    public JsonSavingAlgorithm(JsonProcessingService jsonProcessingService) {
        this.jsonProcessingService = jsonProcessingService;
    }

    @Override
    public void save(List<Truck> trucks, String outputPath) throws Exception {
        try {
            log.info("Сохраняем данные грузовиков в JSON...");
            jsonProcessingService.saveToJson(trucks);
            log.info("Данные успешно сохранены в JSON: {}", outputPath);
        } catch (Exception e) {
            log.error("Ошибка при сохранении данных в JSON: {}", e.getMessage(), e);
            throw e;
        }
    }
}
