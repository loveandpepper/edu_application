package org.hofftech.edu.model.dto.requestdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoadPackageDto {

    @Schema(description = "Список посылок для погрузки", example = "Посылка Тип 3, Посылка Тип 7, Посылка Тип 4")
    private String parcelsText;

    @Schema(description = "Список грузовиков (размеры)", example = "6x6, 6x7")
    private String trucks;

    @Schema(description = "Имя пользователя", example = "test")
    private String user;

    @Schema(description = "Флаг равномерной погрузки", example = "true")
    private boolean even;

    @Schema(description = "Флаг простого алгоритма", example = "true")
    private boolean easy;

    @Schema(description = "Флаг сохранения результата", example = "true")
    private boolean save;
}

