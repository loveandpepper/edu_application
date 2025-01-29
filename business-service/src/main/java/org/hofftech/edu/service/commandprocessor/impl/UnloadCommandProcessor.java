package org.hofftech.edu.service.commandprocessor.impl;


import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.OutputFileException;
import org.hofftech.edu.exception.UnloadException;
import org.hofftech.edu.exception.UserNotProvidedException;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UnloadCommandProcessor implements CommandProcessor {

    private final JsonProcessingService jsonProcessingService;
    private final FileSavingService fileSavingService;

    private static final String OUTPUT_FILE_PATH = "out/in.txt";

    @Override
    public String execute(ParsedCommand command) {
        String inFile = command.getInFile();
        boolean isWithCount = command.isWithCount();
        String user = command.getUser();

        if (inFile == null || inFile.isEmpty()) {
            throw new OutputFileException("Путь к JSON-файлу не указан");
        }

        if (user == null || user.isEmpty()) {
            throw new UserNotProvidedException("Пользователь должен быть передан для комынды UNLOAD");
        }

        try {
            List<Map<String, Long>> packageCountMap = jsonProcessingService.importPackagesFromJson(inFile,
                    isWithCount, user);
            fileSavingService.savePackagesToFile(packageCountMap, OUTPUT_FILE_PATH, isWithCount);
            return "Файл успешно импортирован из JSON: " + inFile;
        } catch (IOException e) {
            throw new UnloadException("Ошибка при обработке команды importJson: " + e.getMessage());
        }
    }
}
