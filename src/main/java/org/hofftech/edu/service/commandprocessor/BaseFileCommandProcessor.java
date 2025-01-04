package org.hofftech.edu.service.commandprocessor;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.FileProcessingService;

@Slf4j
public abstract class BaseFileCommandProcessor implements CommandProcessor {
    protected final FileProcessingService fileProcessingService;

    public BaseFileCommandProcessor(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @Override
    public void execute(ParsedCommand command) {
        String filePath = command.getFilePath();
        int maxTrucks = command.getMaxTrucks();
        boolean useEasyAlgorithm = command.isUseEasyAlgorithm();
        boolean useEvenAlgorithm = command.isUseEvenAlgorithm();

        if (filePath == null || filePath.isEmpty()) {
            log.error("Путь к файлу не указан.");
            return;
        }

        processFile(filePath, maxTrucks, useEasyAlgorithm, useEvenAlgorithm);
    }

    protected abstract void processFile(String filePath, int maxTrucks, boolean useEasyAlgorithm, boolean useEvenAlgorithm);
}
