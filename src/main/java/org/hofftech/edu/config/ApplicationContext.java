package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.handler.ConsoleCommandHandler;
import org.hofftech.edu.service.*;
import org.hofftech.edu.service.FileParsingService;
import org.hofftech.edu.service.packingstategy.PackingStrategyFactory;

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
        CommandParser commandParser = new CommandParser();

        ConsoleCommandHandler consoleHandler = new ConsoleCommandHandler(processorFactory, commandParser);
        this.consoleController = new ConsoleController(consoleHandler);
    }

    private static CommandProcessorFactory getCommandProcessorFactory(ValidatorService validatorService, TruckService truckService) {
        FileParsingService fileParser = new FileParsingService();
        PackingStrategyFactory packingStrategyFactory = new PackingStrategyFactory(truckService);
        JsonProcessingService jsonProcessingService = new JsonProcessingService(validatorService);
        FileProcessingService fileProcessingService = new FileProcessingService(fileParser, validatorService, truckService, jsonProcessingService, packingStrategyFactory);
        return new CommandProcessorFactory(jsonProcessingService, fileProcessingService);
    }
}

