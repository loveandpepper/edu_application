package org.hofftech.edu.factory.commandprocessor.impl;


import org.hofftech.edu.exception.OutputFileException;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public record UnloadCommandProcessor(JsonProcessingService jsonProcessingService,
                                     FileSavingService fileSavingService) implements CommandProcessor {

    private static final String OUTPUT_FILE_PATH = "out/in.txt";

    @Override
    public String execute(ParsedCommand command) {
        String inFile = command.getInFile();
        boolean isWithCount = command.isIswithCount();

        if (inFile == null || inFile.isEmpty()) {
            throw new OutputFileException("Путь к JSON-файлу не указан");
        }

        try {
            List<Map<String, Long>> packageCountMap = jsonProcessingService.importPackagesFromJson(inFile, isWithCount);
            fileSavingService.savePackagesToFile(packageCountMap, OUTPUT_FILE_PATH, isWithCount);
            return "Файл успешно импортирован из JSON: " + inFile;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обработке команды importJson: " + e.getMessage());
        }
    }
}
