package org.hofftech.edu.service.processor.command;

import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

public class SaveCommandProcessor extends BaseFileCommandProcessor {

    public SaveCommandProcessor(FileProcessingService fileProcessingService) {
        super(fileProcessingService);
    }

    @Override
    protected String getCommandPrefix() {
        return "save";
    }

    @Override
    protected void processFile(Path path, boolean useEasyAlgorithm, int maxTrucks, boolean useEvenAlgorithm) {
        fileProcessingService.processFile(path, useEasyAlgorithm, true, maxTrucks, useEvenAlgorithm);
    }
}
