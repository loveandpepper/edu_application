package org.hofftech.edu.model.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnloadPackageDto {

    @Schema(description = "Имя файла для выгрузки", example = "out/trucks.json")
    private String inFile;

    @Schema(description = "Флаг подсчёта количества посылок", example = "true")
    private boolean withCount;

    @Schema(description = "Имя пользователя", example = "test")
    private String user;
}
