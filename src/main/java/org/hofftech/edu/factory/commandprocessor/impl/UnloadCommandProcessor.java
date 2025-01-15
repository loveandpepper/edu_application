package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.util.FileSavingUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UnloadCommandProcessor implements CommandProcessor {
    private final JsonProcessingService jsonProcessingService;
    private final FileSavingUtil fileSavingUtil;
    private static final String OUTPUT_FILE_PATH = "out/in.txt";
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        String inFile = command.getInFile();
        boolean withCount = command.isWithCount();

        if (inFile == null || inFile.isEmpty()) {
            throw new RuntimeException("Ошибка: Путь к JSON-файлу не указан.");
        }

        try {
            List<Map<String, Long>> packageNamesAndCount = jsonProcessingService.importPackagesFromJson(inFile, withCount);
            fileSavingUtil.savePackagesToFile(packageNamesAndCount, OUTPUT_FILE_PATH, withCount);
            result = "Файл успешно импортирован из JSON: " + inFile;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обработке команды importJson: " + e.getMessage());
        }
    }
}
