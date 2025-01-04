package org.hofftech.edu.service.commandprocessor;

import org.hofftech.edu.service.FileProcessingService;

import java.nio.file.Path;

public class ImportCommandProcessor extends BaseFileCommandProcessor {

    public ImportCommandProcessor(FileProcessingService fileProcessingService) {
        super(fileProcessingService);
    }

    @Override
    protected void processFile(String filePath, int maxTrucks, boolean useEasyAlgorithm, boolean useEvenAlgorithm) {
        fileProcessingService.processFile(Path.of(filePath), useEasyAlgorithm, false, maxTrucks, useEvenAlgorithm);
    }
}


