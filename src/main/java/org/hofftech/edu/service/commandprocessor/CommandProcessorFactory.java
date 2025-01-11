package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.JsonProcessingService;


@RequiredArgsConstructor
public class CommandProcessorFactory {
    private final PackageRepository repository;
    private final FileProcessingService fileProcessingService;
    private final JsonProcessingService jsonProcessingService;

    public CommandProcessor getProcessor(CommandType commandType) {
        return switch (commandType) {
            case CREATE -> new CreateCommandProcessor(repository);
            case FIND -> new FindCommandProcessor(repository);
            case UPDATE -> new UpdateCommandProcessor(repository);
            case DELETE -> new DeleteCommandProcessor(repository);
            case LIST -> new ListCommandProcessor(repository);
            case LOAD -> new LoadCommandProcessor(fileProcessingService);
            case UNLOAD -> new UnloadCommandProcessor(jsonProcessingService);
            case START -> new StartCommandProcessor();
            case EXIT -> new ExitCommandProcessor();
        };
    }
}
