package org.hofftech.edu.controller.processor;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.CommandProcessor;
import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

@Slf4j
public abstract class BaseFileCommandProcessor implements CommandProcessor {

    protected final FileProcessingService fileProcessingService;

    public BaseFileCommandProcessor(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public void process(String command) {
        boolean useEasyAlgorithm = command.contains("easyalgorithm");
        boolean useEvenAlgorithm = command.contains("even");
        if (useEvenAlgorithm){
            command = command.replace("even ", "");
        }
        if (useEasyAlgorithm){
            command = command.replace("easyalgorithm ", "");
        }
        int maxTrucks = Integer.MAX_VALUE;
        String filePath = command.replace(getCommandPrefix() + " ", "").trim();

        String[] parts = command.split(" ");
        if (!useEasyAlgorithm && parts.length > 2) {
            try {
                maxTrucks = Integer.parseInt(parts[1]);
                filePath = command.replace(getCommandPrefix() + " " + parts[1], "").trim();
            } catch (NumberFormatException e) {
                log.error("Некорректный формат лимита грузовиков: {}", parts[1]);
                throw new RuntimeException("Неверный формат количества грузовиков");
            }
        }

        try {
            log.info("Обрабатываем файл: {}. Алгоритм: {}, равномерно: {}, команда: {}",
                    filePath, useEasyAlgorithm ? "easyalgorithm" : "multipletrucksalgorithm", useEvenAlgorithm, getCommandPrefix());
            Path path = Path.of(filePath);
            processFile(path, useEasyAlgorithm, maxTrucks, useEvenAlgorithm);
            log.info("Команда {} выполнена", getCommandPrefix());
        } catch (Exception e) {
            log.error("Ошибка при выполнении команды: {}", e.getMessage(), e);
        }
    }

    protected abstract String getCommandPrefix();

    protected abstract void processFile(Path path, boolean useEasyAlgorithm, int maxTrucks, boolean useEvenAlgorithm) throws Exception;
}
