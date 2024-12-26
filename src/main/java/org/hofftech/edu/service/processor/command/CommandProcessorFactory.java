package org.hofftech.edu.service.processor.command;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.JsonProcessingService;



@RequiredArgsConstructor
public class CommandProcessorFactory {

    private final JsonProcessingService jsonProcessingService;
    private final FileProcessingService fileProcessingService;

    public CommandProcessor getProcessor(CommandType type) {
        return switch (type) {
            case IMPORT_JSON -> new ImportJsonCommandProcessor(jsonProcessingService);
            case IMPORT -> new ImportCommandProcessor(fileProcessingService);
            case SAVE -> new SaveCommandProcessor(fileProcessingService);
            case EXIT -> new ExitCommandProcessor();

        };
    }
}
