package org.hofftech.edu.factory;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.ProcessorException;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.OrderManagerService;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.BillingCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.CreateCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.DeleteCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.ExitCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.FindCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.ListCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.LoadCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.StartCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.UnloadCommandProcessor;
import org.hofftech.edu.service.commandprocessor.impl.UpdateCommandProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandProcessorFactory {

    private final PackageRepository repository;
    private final FileProcessingService fileProcessingService;
    private final JsonProcessingService jsonProcessingService;
    private final FileSavingService fileSavingService;
    private final ValidatorService validatorService;
    private final OrderManagerService orderManagerService;
    private final PackageMapper packageMapper;

    public CommandProcessor createProcessor(CommandType commandType) {
        return switch (commandType) {
            case CREATE -> new CreateCommandProcessor(repository, validatorService);
            case FIND -> new FindCommandProcessor(repository, packageMapper);
            case UPDATE -> new UpdateCommandProcessor(repository, validatorService);
            case DELETE -> new DeleteCommandProcessor(repository);
            case LIST -> new ListCommandProcessor(repository, packageMapper);
            case LOAD -> new LoadCommandProcessor(fileProcessingService);
            case UNLOAD -> new UnloadCommandProcessor(jsonProcessingService, fileSavingService);
            case BILLING -> new BillingCommandProcessor(orderManagerService);
            case START -> new StartCommandProcessor();
            case EXIT -> new ExitCommandProcessor();
            case null, default -> throw new ProcessorException("Процессор для команды не найден");
        };
    }
}
