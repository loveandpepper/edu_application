package org.hofftech.edu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.PackageType;
import org.hofftech.edu.model.Truck;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hofftech.edu.model.dto.TruckDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonProcessingService {
    private static final String OUTPUT_DIRECTORY = "out";
    private static final String FILE_NAME = "trucks.json";
    private final ObjectMapper objectMapper;
    private final ValidatorService validatorService;

    public JsonProcessingService(ValidatorService validatorService) {
        this.validatorService = validatorService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

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

    private static File createFile() {
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            log.error("Не удалось создать папку для файла Json");
            throw new RuntimeException("Не удалось создать папку для файла Json");
        }

        File outputFile = new File(outputDir, FILE_NAME);
        return outputFile;
    }


    public List<String> importJson(String jsonFilePath) throws IOException {
        File jsonFile = new File(jsonFilePath);
        if (!validatorService.isFileExists(jsonFile)) {
            log.error("Файл не найден: {}", jsonFile.getAbsolutePath());
            throw new IOException("Файл не найден: " + jsonFilePath);
        }

        Map<String, List<TruckDto>> jsonData = objectMapper.readValue(
                jsonFile,
                new TypeReference<>() {
                }
        );

        if (!validatorService.isValidJsonStructure(jsonData)) {
            log.error("Структура Json некорректна!");
            throw new IOException("Структура Json некорректна!");
        }

        List<String> packagesOutput = new ArrayList<>();
        List<TruckDto> trucks = jsonData.get("trucks");
        for (TruckDto truck : trucks) {
            getPackagesShapeFromTruck(truck, packagesOutput);
        }
        return packagesOutput;
    }

    private static void getPackagesShapeFromTruck(TruckDto truck, List<String> packagesOutput) {
        for (PackageDto pkg : truck.getPackages()) {
            String type = pkg.getType();
            List<String> shape = PackageType.valueOf(type).getShape();
            packagesOutput.addAll(shape);
            packagesOutput.add("");
        }
    }


    private TruckDto convertToTruckDto(Truck truck, int truckIndex) {
        TruckDto truckDto = new TruckDto();
        truckDto.setTruckId(truckIndex + 1);

        List<PackageDto> packageDtos = new ArrayList<>();
        for (Package pkg : truck.getPackages()) {
            packageDtos.add(convertToPackageDto(pkg));
        }
        truckDto.setPackages(packageDtos);

        return truckDto;
    }

    private PackageDto convertToPackageDto(Package pkg) {
        PackageDto packageDto = new PackageDto();
        packageDto.setId(pkg.getId());
        packageDto.setType(pkg.getType().name());

        PackageStartPosition position = pkg.getPackageStartPosition();
        if (position != null) {
            PackageDto.PositionDto positionDto = new PackageDto.PositionDto();
            positionDto.setX(position.getX() + 1);
            positionDto.setY(position.getY() + 1);
            packageDto.setPosition(positionDto);
        } else {
            log.warn("У упаковки с ID {} отсутствует стартовая позиция", pkg.getId());
        }

        return packageDto;
    }

}
