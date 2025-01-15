package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.controller.TelegramController;
import org.hofftech.edu.factory.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.factory.packingstategy.PackingStrategyFactory;
import org.hofftech.edu.handler.impl.DefaultCommandHandler;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.telegram.TelegramAppender;
import org.hofftech.edu.telegram.TelegramBotService;
import org.hofftech.edu.util.FileSavingUtil;

/**
  Контекст приложения, отвечает за создание и конфигурацию зависимостей.
  Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Getter
@Slf4j
public class ApplicationContext {
    private final ConsoleController consoleController;
    private final TelegramController telegramController;

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
        CommandParser commandParser = new CommandParser();
        DefaultCommandHandler commandHandler = new DefaultCommandHandler(processorFactory, commandParser);

        this.consoleController = new ConsoleController(commandHandler);
        this.telegramController = new TelegramController(commandHandler);

        initializeTelegram(telegramController, commandHandler);
    }

    private static CommandProcessorFactory getCommandProcessorFactory(
            ValidatorService validatorService, TruckService truckService, PackageRepository packageRepository) {
        ParsingService parsingService = new ParsingService(packageRepository);
        FileSavingUtil fileSavingUtil = new FileSavingUtil();
        PackingStrategyFactory packingStrategyFactory = new PackingStrategyFactory(truckService);
        JsonProcessingService jsonProcessingService = new JsonProcessingService();
        FileProcessingService fileProcessingService = new FileProcessingService(
                parsingService, validatorService, truckService, jsonProcessingService, packingStrategyFactory);
        return new CommandProcessorFactory(packageRepository, fileProcessingService, jsonProcessingService, fileSavingUtil, validatorService);
    }

    private void initializeTelegram(TelegramController telegramController, DefaultCommandHandler commandHandler) {
        try {
            TelegramAppender telegramAppender = new TelegramAppender();
            TelegramBotService telegramBotService = new TelegramBotService(
                    TelegramBotService.TOKEN,
                    commandHandler,
                    telegramAppender,
                    telegramController
            );

            telegramBotService.registerBot();
            telegramAppender.initialize(telegramBotService);
        } catch (Exception e) {
            log.error("Ошибка при запуске Telegram-бота: {}", e.getMessage(), e);
        }
    }
}




