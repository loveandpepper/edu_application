package org.hofftech.edu.service.commandprocessor;

import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

public class SaveCommandProcessor extends BaseFileCommandProcessor {

    public SaveCommandProcessor(FileProcessingService fileProcessingService) {
        super(fileProcessingService);
    }

    @Override
    protected void processFile(String filePath, int maxTrucks, boolean useEasyAlgorithm, boolean useEvenAlgorithm) {
        fileProcessingService.processFile(Path.of(filePath), useEasyAlgorithm, true, maxTrucks, useEvenAlgorithm);
    }
}

