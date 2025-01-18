package org.hofftech.edu.factory.commandprocessor;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.impl.CreateCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.DeleteCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.ExitCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.FindCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.ListCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.LoadCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.StartCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.UnloadCommandProcessor;
import org.hofftech.edu.factory.commandprocessor.impl.UpdateCommandProcessor;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.ValidatorService;


@RequiredArgsConstructor
public class CommandProcessorFactory {

    private final PackageRepository repository;
    private final FileProcessingService fileProcessingService;
    private final JsonProcessingService jsonProcessingService;
    private final FileSavingService fileSavingService;
    private final ValidatorService validatorService;

    public CommandProcessor createProcessor(CommandType commandType) {
        return switch (commandType) {
            case CREATE -> new CreateCommandProcessor(repository, validatorService);
            case FIND -> new FindCommandProcessor(repository);
            case UPDATE -> new UpdateCommandProcessor(repository, validatorService);
            case DELETE -> new DeleteCommandProcessor(repository);
            case LIST -> new ListCommandProcessor(repository);
            case LOAD -> new LoadCommandProcessor(fileProcessingService);
            case UNLOAD -> new UnloadCommandProcessor(jsonProcessingService, fileSavingService);
            case START -> new StartCommandProcessor();
            case EXIT -> new ExitCommandProcessor();
        };
    }
}
