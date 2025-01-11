package org.hofftech.edu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.dto.TruckDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
/**
 * Сервис для обработки данных в формате JSON.
 * Отвечает за сохранение и загрузку данных о грузовиках и посылках в формате JSON.
 */
@Slf4j
public class JsonProcessingService {
    private static final String OUTPUT_DIRECTORY = "out";
    private static final String FILE_NAME = "trucks.json";
    private final ObjectMapper objectMapper;

    public JsonProcessingService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    /**
     * Сохраняет данные о грузовиках в JSON-файл и возвращает строковое представление JSON.
     *
     * @param trucks список грузовиков, которые нужно сохранить
     * @return строковое представление JSON с данными о грузовиках
     * @throws RuntimeException если возникает ошибка при записи JSON
     */
    public String saveToJson(List<Truck> trucks) {
        File outputFile = createFile();
        List<TruckDto> trucksData = new ArrayList<>();
        for (int i = 0; i < trucks.size(); i++) {
            trucksData.add(convertToTruckDto(trucks.get(i), i));
        }

        try {
            String jsonString = objectMapper.writeValueAsString(Map.of("trucks", trucksData));
            objectMapper.writeValue(outputFile, Map.of("trucks", trucksData));
            log.info("JSON файл успешно создан: {}", outputFile.getAbsolutePath());
            return jsonString;
        } catch (IOException e) {
            log.error("Ошибка при записи JSON: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    /**
     * Импортирует данные о посылках из JSON-файла.
     *
     * @param jsonFilePath путь к JSON-файлу
     * @param withCount    если true, возвращает количество каждой посылки; если false, возвращает список уникальных посылок
     * @return карта, где ключ — имя посылки, а значение — количество (или 1, если withCount == false)
     * @throws IOException если файл не найден или возникает ошибка при чтении
     */
    public List<Map.Entry<String, Long>> importPackagesFromJson(String jsonFilePath, boolean withCount) throws IOException {
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            log.error("Файл не найден: {}", jsonFile.getAbsolutePath());
            throw new IOException("Файл не найден: " + jsonFilePath);
        }

        Map<String, List<TruckDto>> jsonData = objectMapper.readValue(
                jsonFile,
                new TypeReference<>() {}
        );
        List<Package> packages = new ArrayList<>();
        List<TruckDto> trucks = jsonData.get("trucks");
        for (TruckDto truck : trucks) {
            extractPackagesFromTruck(truck, packages);
        }

        if (withCount) {
            return new ArrayList<>(packages.stream()
                    .collect(Collectors.groupingBy(
                            Package::getName,
                            LinkedHashMap::new,
                            Collectors.counting()
                    ))
                    .entrySet());
        } else {
            return packages.stream()
                    .map(pkg -> Map.entry(pkg.getName(), 1L))
                    .collect(Collectors.toList());
        }
    }


    private void extractPackagesFromTruck(TruckDto truck, List<Package> packages) {
        for (PackageDto pkgDto : truck.getPackages()) {
            PackageStartPosition position = null;
            if (pkgDto.getStartPosition() != null) {
                position = new PackageStartPosition(
                        pkgDto.getStartPosition().getX(),
                        pkgDto.getStartPosition().getY()
                );
            }
            Package pkg = new Package(
                    pkgDto.getName(),
                    pkgDto.getShape(),
                    pkgDto.getSymbol(),
                    position
            );
            packages.add(pkg);
        }
    }

    private TruckDto convertToTruckDto(Truck truck, int truckIndex) {
        TruckDto truckDto = new TruckDto();
        truckDto.setTruckId(truckIndex + 1);
        truckDto.setTruckSize(truck.getWidth() + "x" + truck.getHeight());

        List<PackageDto> packageDtos = new ArrayList<>();
        for (Package pkg : truck.getPackages()) {
            packageDtos.add(convertToPackageDto(pkg));
        }
        truckDto.setPackages(packageDtos);

        return truckDto;
    }

    private PackageDto convertToPackageDto(Package pkg) {
        PackageDto packageDto = new PackageDto();
        packageDto.setName(pkg.getName());
        packageDto.setShape(pkg.getUniqueShape());
        packageDto.setSymbol(pkg.getSymbol());

        PackageStartPosition position = pkg.getPackageStartPosition();
        if (position != null) {
            PackageDto.PositionDto positionDto = new PackageDto.PositionDto();
            positionDto.setX(position.getX() + 1);
            positionDto.setY(position.getY() + 1);
            packageDto.setStartPosition(positionDto);
        } else {
            log.warn("У посылки с именем '{}' отсутствует стартовая позиция", pkg.getName());
        }

        return packageDto;
    }

    private static File createFile() {
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            log.error("Не удалось создать папку для файла Json");
            throw new RuntimeException("Не удалось создать папку для файла Json");
        }
        return new File(outputDir, FILE_NAME);
    }
}
