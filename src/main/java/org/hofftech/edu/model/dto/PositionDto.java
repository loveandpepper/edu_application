package org.hofftech.edu.model.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) для класса PositionDto.
 * Используется для передачи данных о стартовом положении (нижнем левом угле посылки) в грузовике
 */
@Data
public class PositionDto {
    private int x;
    private int y;
}
