package org.hofftech.edu.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class TruckDto {
    private int truckId;
    private List<PackageDto> packages;
}
