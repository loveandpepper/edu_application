package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.CommandProcessorFactory;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.handler.ConsoleCommandHandler;
import org.hofftech.edu.service.*;
import org.hofftech.edu.service.FileParsingService;
import org.hofftech.edu.service.filesaving.FileSavingAlgorithmFactory;

@Slf4j
@Getter
public class ApplicationContext {
    private final ConsoleController consoleController;

    public ApplicationContext() {
        log.info("Создаем зависимости...");
        PackingService packingService = new PackingService();
        TruckService truckService = new TruckService(packingService);
        ValidatorService validatorService = new ValidatorService();
        CommandProcessorFactory processorFactory = getCommandProcessorFactory(validatorService, truckService);
        ConsoleCommandHandler consoleCommandHandler = new ConsoleCommandHandler(processorFactory);

        this.consoleController = new ConsoleController(consoleCommandHandler);
    }

    private static CommandProcessorFactory getCommandProcessorFactory(ValidatorService validatorService, TruckService truckService) {
        FileParsingService fileParser = new FileParsingService();
        JsonProcessingService jsonProcessingService = new JsonProcessingService(validatorService);
        FileSavingAlgorithmFactory fileSavingAlgorithmFactory = new FileSavingAlgorithmFactory(jsonProcessingService);
        FileProcessingService fileProcessingService =
                new FileProcessingService(fileParser, validatorService, truckService, fileSavingAlgorithmFactory);
        return new CommandProcessorFactory(jsonProcessingService, fileProcessingService);
    }

}
