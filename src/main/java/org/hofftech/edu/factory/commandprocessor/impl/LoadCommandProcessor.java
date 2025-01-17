package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record LoadCommandProcessor(FileProcessingService fileProcessingService) implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        String parcelsText = command.getParcelsText();
        String parcelsFile = command.getParcelsFile();
        String trucksText = command.getTrucks();
        boolean useEasyAlgorithm = command.isUseEasyAlgorithm();
        boolean useEvenAlgorithm = command.isUseEvenAlgorithm();
        boolean saveToFile = command.isSaveToFile();

        try {
            List<String> trucksFromArgs = trucksText != null && !trucksText.isEmpty()
                    ? new ArrayList<>(List.of(trucksText.split(",")))
                    : new ArrayList<>();

            if (parcelsText != null && !parcelsText.isEmpty()) {
                return fileProcessingService.processFile(
                        null, parcelsText, trucksFromArgs, useEasyAlgorithm, saveToFile, useEvenAlgorithm
                );
            } else if (parcelsFile != null && !parcelsFile.isBlank()) {
                return fileProcessingService.processFile(
                        Path.of(parcelsFile), null, trucksFromArgs, useEasyAlgorithm, saveToFile, useEvenAlgorithm
                );
            } else {
                throw new RuntimeException("Укажите источник посылок (текст или файл)");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при погрузке: " + e.getMessage());
        }
    }
}

