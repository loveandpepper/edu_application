package org.hofftech;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.controller.ConsoleController;
import org.hofftech.handler.ImportCommandHandler;
import org.hofftech.model.Package;
import org.hofftech.model.PackageType;
import org.hofftech.model.Truck;
import org.hofftech.service.FileProcessingService;
import org.hofftech.service.PackingService;
import org.hofftech.service.TruckService;
import org.hofftech.service.ValidatorService;
import org.hofftech.util.FileParserUtil;
import org.hofftech.util.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(Main.class);
        log.info("Приложение запускается...");

        // Создаем все зависимости
        PackingService packingService = new PackingService();
        TruckService truckService = new TruckService(packingService);
        ValidatorService validatorService = new ValidatorService();
        FileReaderUtil fileReader = new FileReaderUtil();
        FileParserUtil fileParser = new FileParserUtil();
        FileProcessingService fileProcessingService = new FileProcessingService(fileReader, fileParser, validatorService, truckService);
        ImportCommandHandler importCommandHandler = new ImportCommandHandler(fileProcessingService);

        // Создаем и запускаем ConsoleController
        ConsoleController consoleController = new ConsoleController(importCommandHandler);
        consoleController.listen();

        log.info("Приложение завершило работу.");
    }
}


