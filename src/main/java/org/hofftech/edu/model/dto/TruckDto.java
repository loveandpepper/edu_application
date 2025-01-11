package org.hofftech.edu.model.dto;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) для класса Truck.
 * Используется для передачи данных о грузовике между слоями приложения.
 */
@Data
public class TruckDto {
    private int truckId;
    private String truckSize;
    private List<PackageDto> packages;
}
