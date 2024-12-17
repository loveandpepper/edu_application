package org.hofftech.edu.handler;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

@Slf4j
public class ImportCommandHandler implements CommandHandler {
    private static final String EXIT_COMMAND = "exit";
    private final FileProcessingService fileProcessingService;

    public ImportCommandHandler(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public void handle(String command) {
        if (EXIT_COMMAND.equalsIgnoreCase(command)) {
            log.info("Приложение завершает работу по команде пользователя.");
            return;
        }
        String filePath;
        String algorithm;
        boolean useEasyAlgorithm;

        if (command.startsWith("import ")) {
            useEasyAlgorithm = command.contains("easyalgorithm");
            if (useEasyAlgorithm) {algorithm = "easyalgorithm";}
            else {algorithm = "multipletrucksalgorithm";}
            filePath = command.replace("easyalgorithm", "").replace("import", "").trim();
        } else {
            log.warn("Неизвестная команда: {}", command);
            System.out.println("Неизвестная команда. Попробуйте снова.");
            return;
        }
        try {
            log.info("Начинаем обработку файла: {}. Используем алгоритм: {}", filePath, algorithm);
            Path path = Path.of(filePath);
            fileProcessingService.processFile(path, useEasyAlgorithm);
        } catch (Exception e) {
            log.error("Произошла ошибка при обработке файла: {}", filePath, e);
        }
    }
}
