package org.hofftech.edu.service;

import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.dto.TruckDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ValidatorService {
    public boolean isValidFile(List<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            log.error("Файл пустой или не содержит данных.");
            return false;
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
        return true;
    }

    public boolean isValidPackages(List<Package> packages) {
        List<Package> invalidPackages = new ArrayList<>();
        for (Package pkg : packages) {
            if (!isValidPackage(pkg)) {
                invalidPackages.add(pkg);
            }
        }
        if (!invalidPackages.isEmpty()) {
            for (Package invalidPkg : invalidPackages) {
                log.error("Упаковка с ID {} имеет некорректную форму: {}",
                        invalidPkg.getId(), invalidPkg.getType().getShape());
            }
            return false;
        }

        log.info("Все упаковки успешно проверены.");
        return true;
    }

    private boolean isValidPackage(Package pkg) {
        PackageType type = pkg.getType();
        List<String> shape = type.getShape();

        int maxWidth = 0;
        for (String row : shape) {
            maxWidth = Math.max(maxWidth, row.length());
        }

        for (String row : shape) {
            if (row.length() < maxWidth) {
                for (int x = row.length(); x < maxWidth; x++) {
                    if (row.length() > x) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isValidJsonStructure(Map<String, List<TruckDto>> jsonData) {
        if (!jsonData.containsKey("trucks")) {
            log.error("Ошибка: JSON не содержит ключ 'trucks'.");
            return false;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType truckListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, TruckDto.class);

        try {
            List<TruckDto> trucks = objectMapper.convertValue(jsonData.get("trucks"), truckListType);

            for (TruckDto truck : trucks) {
                if (truck.getPackages() == null || truck.getPackages().isEmpty()) {
                    log.error("Ошибка: У грузовика ID {} отсутствуют упаковки.", truck.getTruckId());
                    return false;
                }

                for (PackageDto pkg : truck.getPackages()) {
                    if (pkg.getType() == null || pkg.getType().isBlank()) {
                        log.error("Ошибка: У одной из посылок отсутствует тип.");
                        return false;
                    }
                }
            }

            log.info("JSON успешно провалидирован.");
            return true;
        } catch (Exception e) {
            log.error("Ошибка при валидации структуры JSON: {}", e.getMessage());
            return false;
        }
    }


    public boolean isFileExists(File jsonFile) {
        return jsonFile.exists();
    }


}
