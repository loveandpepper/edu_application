package org.hofftech.edu.model.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePackageDto {
    @Schema(description = "Старое имя посылки", example = "Квадратное колесо")
    private String oldName;

    @Schema(description = "Новое имя посылки", example = "Большая Посылка")
    private String name;

    @Schema(description = "Новая форма посылки", example = "#####,#####,#####,#####,#####")
    private String form;

    @Schema(description = "Новый символ посылки", example = "o")
    private String symbol;
}
