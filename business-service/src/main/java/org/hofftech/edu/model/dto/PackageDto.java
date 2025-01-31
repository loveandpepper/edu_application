package org.hofftech.edu.model.dto;

import lombok.Data;
/**
 * Data Transfer Object (DTO) для класса Package.
 * Используется для передачи данных о упаковке между слоями приложения.
 */
@Data
public class PackageDto {

    private String name;
    private String[] shape;
    private char symbol;
    private int startPositionX;
    private int startPositionY;
}