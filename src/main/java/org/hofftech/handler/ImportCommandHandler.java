package org.hofftech.handler;

import org.hofftech.service.FileProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ImportCommandHandler implements CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(ImportCommandHandler.class);
    private final FileProcessingService fileProcessingService;

    public ImportCommandHandler(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public void handle(String filePath, boolean useEasyAlgorithm) {
        try {
            log.info("Начинаем обработку файла: {}. Используем простой алгоритм: {}", filePath, useEasyAlgorithm);

            Path path = Path.of(filePath);
            fileProcessingService.processFile(path, useEasyAlgorithm);
        } catch (Exception e) {
            log.error("Произошла ошибка при обработке файла: {}", filePath, e);
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
