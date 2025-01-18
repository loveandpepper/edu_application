package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.controller.TelegramBotController;
import org.hofftech.edu.factory.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.factory.packingstategy.PackingStrategyFactory;
import org.hofftech.edu.handler.CommandHandler;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.CommandTypeService;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;

/**
  Контекст приложения, отвечает за создание и конфигурацию зависимостей.
  Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Getter
@Slf4j
public class ApplicationContext {

    private final ConsoleController consoleController;

    public ApplicationContext() {
        log.info("Создаем зависимости...");
        PackageRepository packageRepository = new PackageRepository();
        packageRepository.loadDefaultPackages();
        PackingService packingService = new PackingService();
        TruckService truckService = new TruckService(packingService);
        ValidatorService validatorService = new ValidatorService();
        CommandProcessorFactory processorFactory = getCommandProcessorFactory(
                validatorService, truckService, packageRepository
        );
        CommandTypeService commandTypeService = new CommandTypeService();
        CommandParser commandParser = new CommandParser(commandTypeService);
        CommandHandler commandHandler = new CommandHandler(processorFactory, commandParser);

        this.consoleController = new ConsoleController(commandHandler);

        initializeTelegram(commandHandler);
    }

    private CommandProcessorFactory getCommandProcessorFactory(
            ValidatorService validatorService, TruckService truckService, PackageRepository packageRepository) {
        ParsingService parsingService = new ParsingService(packageRepository);
        FileSavingService fileSavingService = new FileSavingService();
        PackingStrategyFactory packingStrategyFactory = new PackingStrategyFactory(truckService);
        JsonProcessingService jsonProcessingService = new JsonProcessingService();
        FileProcessingService fileProcessingService = new FileProcessingService(
                parsingService, validatorService, truckService, jsonProcessingService, packingStrategyFactory);
        return new CommandProcessorFactory(packageRepository, fileProcessingService, jsonProcessingService, fileSavingService, validatorService);
    }

    private void initializeTelegram(CommandHandler commandHandler) {
        try {
            TelegramBotController telegramBotController = new TelegramBotController(
                    TelegramBotController.TOKEN,
                    commandHandler
            );
            telegramBotController.registerBot();
        } catch (Exception e) {
            log.error("Ошибка при запуске Telegram-бота: {}", e.getMessage(), e);
        }
    }
}




