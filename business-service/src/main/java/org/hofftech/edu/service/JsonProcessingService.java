package org.hofftech.edu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.InputFileException;
import org.hofftech.edu.exception.JsonLoadException;
import org.hofftech.edu.exception.JsonUnloadException;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.OrderOperationType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.dto.TruckDto;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Сервис для обработки данных в формате JSON.
 * Отвечает за сохранение и загрузку данных о грузовиках и посылках в формате JSON.
 */
@Slf4j
@RequiredArgsConstructor
public class JsonProcessingService {

    private static final String OUTPUT_DIRECTORY = "out";
    private static final String FILE_NAME = "trucks.json";
    private static final String TRUCKS_ARRAY = "trucks";
    private static final int ADJUSTING_FOR_START_POSITION = 1;
    private static final int TRUCK_NAME_INDEX = 1;
    private static final String TRUCK_SIZE_SPLITTER = "x";
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final OrderManagerService orderManagerService;
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
            String jsonString = objectMapper.writeValueAsString(Map.of(TRUCKS_ARRAY, trucksData));
            objectMapper.writeValue(outputFile, Map.of(TRUCKS_ARRAY, trucksData));
            log.info("JSON файл успешно создан: {}", outputFile.getAbsolutePath());
            return jsonString;
        } catch (IOException e) {
            throw new JsonLoadException("Ошибка при сохранении в Json");
        }
    }

    /**
     * Импортирует данные о посылках из JSON-файла.
     *
     * @param jsonFilePath путь к JSON-файлу
     * @param isWithCount    если true, возвращает количество каждой посылки; если false, возвращает список уникальных посылок
     * @return карта, где ключ — имя посылки, а значение — количество (или 1, если isWithCount == false)
     */
    @SneakyThrows
    public List<Map<String, Long>> importPackagesFromJson(String jsonFilePath, boolean isWithCount, String user) {
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            throw new InputFileException("Файл не найден: " + jsonFilePath);
        }
        Map<String, List<TruckDto>> jsonData;
        try {
            jsonData = objectMapper.readValue(
                    jsonFile,
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new JsonUnloadException("Ошибка маппинга: " + e.getMessage());
        }
        List<Package> packages = new ArrayList<>();
        List<TruckDto> trucks = jsonData.get(TRUCKS_ARRAY);
        for (TruckDto truck : trucks) {
            extractPackagesFromTruck(truck, packages);
        }

        addUnloadOrder(trucks, packages, user);

        if (isWithCount) {
            return groupPackagesWithCount(packages);
        } else {
            return getIndividualPackages(packages);
        }
    }

    public void addUnloadOrder(List<TruckDto> trucks, List<Package> packages, String userId) {
        Order order = new Order(
                userId,
                LocalDate.ofInstant(clock.instant(), clock.getZone()),
                OrderOperationType.UNLOAD,
                trucks.size(),
                packages
        );

        orderManagerService.addOrder(order);
        log.info("Добавлен заказ на рагрузку для {}", userId);
    }

    private List<Map<String, Long>> getIndividualPackages(List<Package> packages) {
        return packages.stream()
                .flatMap(pkg -> pkg.getName().repeat(1).lines())
                .map(name -> Map.of(name, 1L))
                .toList();
    }

    private List<Map<String, Long>> groupPackagesWithCount(List<Package> packages) {
        return packages.stream()
                .collect(Collectors.groupingBy(
                        Package::getName,
                        LinkedHashMap::new,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> Map.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void extractPackagesFromTruck(TruckDto truck, List<Package> packages) {
        for (PackageDto packageDto : truck.getPackages()) {
            Package pkg = new Package(
                    packageDto.getName(),
                    packageDto.getShape(),
                    packageDto.getSymbol(),
                    packageDto.getStartPositionX(),
                    packageDto.getStartPositionY()
            );
            packages.add(pkg);
        }
    }

    private TruckDto convertToTruckDto(Truck truck, int truckIndex) {
        List<PackageDto> packageDtos = new ArrayList<>();
        for (Package pkg : truck.getPackages()) {
            packageDtos.add(convertToPackageDto(pkg));
        }
        return new TruckDto(truckIndex + TRUCK_NAME_INDEX, truck.getWidth() + TRUCK_SIZE_SPLITTER + truck.getHeight(), packageDtos);
    }

    private PackageDto convertToPackageDto(Package providedPackage) {
        PackageDto packageDto = new PackageDto();
        packageDto.setName(providedPackage.getName());
        packageDto.setShape(providedPackage.getReversedShape());
        packageDto.setSymbol(providedPackage.getSymbol());
        packageDto.setStartPositionX(providedPackage.getStartPositionX() + ADJUSTING_FOR_START_POSITION);
        packageDto.setStartPositionY(providedPackage.getStartPositionY() + ADJUSTING_FOR_START_POSITION);

        return packageDto;
    }

    private File createFile() {
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new JsonLoadException("Не удалось создать папку для файла Json");
        }
        return new File(outputDir, FILE_NAME);
    }
}
