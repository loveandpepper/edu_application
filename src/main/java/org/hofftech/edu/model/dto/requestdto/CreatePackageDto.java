package org.hofftech.edu.model.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreatePackageDto {
    @Schema(description = "Имя посылки", example = "Квадратное колесо")
    private String name;

    @Schema(description = "Форма посылки в виде строки", example = "###,# #,###")
    private String form;

    @Schema(description = "Символ посылки", example = "#")
    private String symbol;
}
