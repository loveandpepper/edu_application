package org.hofftech.edu.model.dto;

import lombok.Data;

@Data
public class PackageDto {
    private long id;
    private String type;
    private PositionDto position;

    @Data
    public static class PositionDto {
        private int x;
        private int y;
    }
}