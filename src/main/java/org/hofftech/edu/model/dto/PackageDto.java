package org.hofftech.edu.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Data Transfer Object (DTO) для класса Package.
 * Используется для передачи данных о упаковке между слоями приложения.
 */
@Data
@ToString
public class PackageDto {

    private String name;
    private List<String> shape;
    private char symbol;
    private int startPositionX;
    private int startPositionY;
}