package org.hofftech.edu.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.ConsoleController;
import org.hofftech.edu.handler.CommandHandler;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TelegramBotService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.hofftech.edu.service.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.service.packingstategy.PackingStrategyFactory;
import org.hofftech.edu.util.telegram.TelegramAppender;
import org.hofftech.edu.util.telegram.TelegramPrintStream;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
  Контекст приложения, отвечает за создание и конфигурацию зависимостей.
  Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Getter
@Slf4j
public class ApplicationContext {
    /**
     * Создает и возвращает экземпляр ConsoleController.
     *
     * @return ConsoleController для обработки пользовательских команд
     */
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
        CommandParser commandParser = new CommandParser();
        CommandHandler commandHandler = new CommandHandler(processorFactory, commandParser);
        this.consoleController = new ConsoleController(commandHandler);
        initializeTelegram(commandHandler);
    }

    private static void initializeTelegram(CommandHandler commandHandler) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramAppender telegramAppender = new TelegramAppender();
            TelegramPrintStream telegramPrintStream = new TelegramPrintStream(System.out, null);
            TelegramBotService telegramBotService = new TelegramBotService(
                    TelegramBotService.TOKEN,
                    commandHandler,
                    telegramAppender,
                    telegramPrintStream
            );
            botsApi.registerBot(telegramBotService);
            TelegramAppender.initialize(telegramBotService);
            telegramPrintStream.setBot(telegramBotService);
            System.setOut(telegramPrintStream);
            Runtime.getRuntime().addShutdownHook(new Thread(telegramPrintStream::shutdown));
        } catch (Exception e) {
            log.error("Ошибка при запуске Telegram-бота: {}", e.getMessage(), e);
        }
    }

    private static CommandProcessorFactory getCommandProcessorFactory(
            ValidatorService validatorService, TruckService truckService, PackageRepository packageRepository) {
        ParsingService parsingService = new ParsingService(packageRepository);
        PackingStrategyFactory packingStrategyFactory = new PackingStrategyFactory(truckService);
        JsonProcessingService jsonProcessingService = new JsonProcessingService();
        FileProcessingService fileProcessingService = new FileProcessingService(
                parsingService, validatorService, truckService, jsonProcessingService, packingStrategyFactory);
        return new CommandProcessorFactory(packageRepository, fileProcessingService, jsonProcessingService);
    }
}



