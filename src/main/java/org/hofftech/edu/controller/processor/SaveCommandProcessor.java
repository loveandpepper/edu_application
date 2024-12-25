package org.hofftech.edu.controller.processor;

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
    protected void processFile(Path path, boolean useEasyAlgorithm, int maxTrucks, boolean useEvenAlgorithm) throws Exception {
        fileProcessingService.processFile(path, useEasyAlgorithm, true, maxTrucks, useEvenAlgorithm);
    }
}
