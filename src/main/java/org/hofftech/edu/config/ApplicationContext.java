package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.handler.ConsoleCommandHandler;
import org.hofftech.edu.service.*;
import org.hofftech.edu.util.FileParserUtil;
import org.hofftech.edu.util.FileReaderUtil;

@Slf4j
@Getter
public class ApplicationContext {
    private final ConsoleController consoleController;

    public ApplicationContext() {
        log.info("Создаем зависимости...");
        PackingService packingService = new PackingService();
        TruckService truckService = new TruckService(packingService);
        ValidatorService validatorService = new ValidatorService();
        FileReaderUtil fileReader = new FileReaderUtil();
        FileParserUtil fileParser = new FileParserUtil();
        JsonProcessingService jsonProcessingService = new JsonProcessingService(validatorService);
        FileProcessingService fileProcessingService =
                new FileProcessingService(fileReader, fileParser, validatorService, truckService, jsonProcessingService);
        ConsoleCommandHandler consoleCommandHandler = new ConsoleCommandHandler(fileProcessingService, jsonProcessingService);

        this.consoleController = new ConsoleController(consoleCommandHandler);
    }

}
