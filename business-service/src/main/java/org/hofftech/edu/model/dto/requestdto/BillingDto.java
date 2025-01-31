package org.hofftech.edu.model.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BillingDto {
    @Schema(description = "Имя юзера", example = "test")
    private String user;

    @Schema(description = "Дата от", example = "27-01-2025")
    private String from;

    @Schema(description = "Дата до", example = "27-01-2025")
    private String to;
}
