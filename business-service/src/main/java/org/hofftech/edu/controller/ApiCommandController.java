package org.hofftech.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.model.dto.PackageDto;
import org.hofftech.edu.model.dto.requestdto.BillingDto;
import org.hofftech.edu.model.dto.requestdto.CreatePackageDto;
import org.hofftech.edu.model.dto.requestdto.LoadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UnloadPackageDto;
import org.hofftech.edu.model.dto.requestdto.UpdatePackageDto;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class ApiCommandController {

    private final CommandProcessorFactory processorFactory;
    private final PackageMapper packageMapper;

    /**
     * Создать новую посылку
     * POST /api/packages
     */
    @Operation(summary = "Создать новую посылку", description = "Создаёт новую посылку с указанными параметрами.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreatePackageDto dto) {
        ParsedCommand command = packageMapper.toParsedCommand(dto);
        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Обновить посылку
     * PUT /api/packages/{name}
     */
    @Operation(summary = "Обновить посылку", description = "Обновляет существующую посылку по её имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    })
    @PutMapping("/{name}")
    public ResponseEntity<String> update(@PathVariable String name,
                                         @RequestBody UpdatePackageDto dto) {
        ParsedCommand command = packageMapper.toParsedCommand(dto);
        command.setName(name);

        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Удалить посылку
     * DELETE /api/packages/{name}
     */
    @Operation(summary = "Удалить посылку", description = "Удаляет посылку по имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Посылка не найдена")
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<String> delete(@PathVariable String name) {
        ParsedCommand command = new ParsedCommand();
        command.setCommandType(CommandType.DELETE);
        command.setName(name);

        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Найти посылку
     * GET /api/packages/{name}
     */
    @Operation(summary = "Найти посылку", description = "Возвращает данные посылки по её имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылка найдена",
                    content = @Content(schema = @Schema(implementation = PackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Посылка не найдена")
    })
    @GetMapping("/{name}")
    public ResponseEntity<PackageDto> find(@PathVariable String name) {
        ParsedCommand command = new ParsedCommand();
        command.setCommandType(CommandType.FIND);
        command.setName(name);

        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        PackageDto result = (PackageDto) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Получить список посылок
     * GET /api/packages
     */
    @Operation(summary = "Получить список посылок", description = "Возвращает список всех посылок.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список успешно получен",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<PackageDto>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        ParsedCommand command = new ParsedCommand();
        command.setCommandType(CommandType.LIST);
        command.setPage(page);
        command.setSize(size);

        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        List<PackageDto> result = (List<PackageDto>) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Погрузка
     * POST /api/packages/load
     */
    @Operation(summary = "Погрузка", description = "Погружает посылки в грузовики с указанными параметрами.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылки успешно погружены"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    })
    @PostMapping("/load")
    public ResponseEntity<String> load(@RequestBody LoadPackageDto dto) {
        ParsedCommand command = packageMapper.toParsedCommand(dto);
        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Разгрузка
     * POST /api/packages/unload
     */
    @Operation(summary = "Разгрузка", description = "Выгружает посылки из грузовиков в указанный файл.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посылки успешно выгружены"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    })
    @PostMapping("/unload")
    public ResponseEntity<String> unload(@RequestBody UnloadPackageDto dto) {
        ParsedCommand command = packageMapper.toParsedCommand(dto);
        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Рассчитать стоимость
     * POST /api/packages/billing
     */
    @Operation(summary = "Рассчитать стоимость", description = "Выполняет расчёт стоимости для пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расчёт успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    })
    @PostMapping("/billing")
    public ResponseEntity<String> billing(@RequestBody BillingDto dto) {
        ParsedCommand command = packageMapper.toParsedCommand(dto);
        CommandProcessor processor = processorFactory.createProcessor(command.getCommandType());
        String result = (String) processor.execute(command);
        return ResponseEntity.ok(result);
    }
}
