package org.hofftech.edu.service.processor.command;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.util.FileSavingUtil;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ImportJsonCommandProcessor implements CommandProcessor {
    private final JsonProcessingService jsonProcessingService;
    private static final String OUTPUT_FILE_PATH = "out/in.txt";

    public ImportJsonCommandProcessor(JsonProcessingService jsonProcessingService) {
        this.jsonProcessingService = jsonProcessingService;
    }

    @Override
    public void execute(String command) {
        String jsonFilePath = command.replace("importjson ", "").trim();
        try {
            List<String> packageTypes = jsonProcessingService.importJson(jsonFilePath);
            FileSavingUtil.savePackagesToFile(packageTypes, OUTPUT_FILE_PATH);
            log.info("Файл успешно импортирован из JSON: {}", jsonFilePath);
        } catch (IOException e) {
            log.error("Ошибка при обработке команды importjson: {}", e.getMessage(), e);
        }
    }
}

