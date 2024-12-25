package org.hofftech.edu.controller.processor;

import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

public class ImportCommandProcessor extends BaseFileCommandProcessor {

    public ImportCommandProcessor(FileProcessingService fileProcessingService) {
        super(fileProcessingService);
    }

    @Override
    protected String getCommandPrefix() {
        return "import";
    }

    @Override
    protected void processFile(Path path, boolean useEasyAlgorithm, int maxTrucks, boolean useEvenAlgorithm) throws Exception {
        fileProcessingService.processFile(path, useEasyAlgorithm, false, maxTrucks, useEvenAlgorithm);
    }
}
