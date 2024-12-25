package org.hofftech.edu.controller;

import org.hofftech.edu.controller.processor.ImportCommandProcessor;
import org.hofftech.edu.controller.processor.ImportJsonCommandProcessor;
import org.hofftech.edu.controller.processor.SaveCommandProcessor;
import org.hofftech.edu.controller.type.CommandType;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.JsonProcessingService;

import java.util.HashMap;
import java.util.Map;

public class CommandProcessorFactory {
    private final Map<CommandType, CommandProcessor> processors = new HashMap<>();

    public CommandProcessorFactory(JsonProcessingService jsonProcessingService, FileProcessingService fileProcessingService) {
        processors.put(CommandType.IMPORT_JSON, new ImportJsonCommandProcessor(jsonProcessingService));
        processors.put(CommandType.IMPORT, new ImportCommandProcessor(fileProcessingService));
        processors.put(CommandType.SAVE, new SaveCommandProcessor(fileProcessingService));
    }

    public CommandProcessor getProcessor(CommandType type) {
        return processors.get(type);
    }
}
