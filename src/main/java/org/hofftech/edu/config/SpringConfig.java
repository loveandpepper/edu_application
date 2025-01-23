package org.hofftech.edu.config;

import org.hofftech.edu.controller.TelegramBotController;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.CommandTypeService;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.OrderManagerService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Контекст приложения, отвечает за создание и конфигурацию зависимостей.
 * Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Configuration
public class SpringConfig {

    @Bean
    public PackageRepository packageRepository() {
        PackageRepository repository = new PackageRepository();
        repository.loadDefaultPackages();
        return repository;
    }

    @Bean
    public OrderManagerService orderManagerService() {
        return new OrderManagerService();
    }

    @Bean
    public PackingService packingService() {
        return new PackingService();
    }

    @Bean
    public TruckService truckService(PackingService packingService) {
        return new TruckService(packingService);
    }

    @Bean
    public ValidatorService validatorService() {
        return new ValidatorService();
    }

    @Bean
    public ParsingService parsingService(PackageRepository packageRepository) {
        return new ParsingService(packageRepository);
    }

    @Bean
    public PackingStrategyFactory packingStrategyFactory(TruckService truckService) {
        return new PackingStrategyFactory(truckService);
    }

    @Bean
    public JsonProcessingService jsonProcessingService(OrderManagerService orderManagerService) {
        return new JsonProcessingService(orderManagerService);
    }

    @Bean
    public FileProcessingService fileProcessingService(
            ParsingService parsingService,
            ValidatorService validatorService,
            TruckService truckService,
            JsonProcessingService jsonProcessingService,
            PackingStrategyFactory packingStrategyFactory,
            OrderManagerService orderManagerService) {
        return new FileProcessingService(
                parsingService, validatorService, truckService,
                jsonProcessingService, packingStrategyFactory, orderManagerService);
    }

    @Bean
    public FileSavingService fileSavingService() {
        return new FileSavingService();
    }

    @Bean
    public CommandProcessorFactory commandProcessorFactory(
            PackageRepository packageRepository,
            FileProcessingService fileProcessingService,
            JsonProcessingService jsonProcessingService,
            FileSavingService fileSavingService,
            ValidatorService validatorService,
            OrderManagerService orderManagerService) {
        return new CommandProcessorFactory(
                packageRepository, fileProcessingService, jsonProcessingService,
                fileSavingService, validatorService, orderManagerService);
    }

    @Bean
    public CommandTypeService commandTypeService() {
        return new CommandTypeService();
    }

    @Bean
    public CommandParser commandParser(CommandTypeService commandTypeService) {
        return new CommandParser(commandTypeService);
    }

    @Bean
    public TelegramBotController telegramBotController(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.name}") String botName,
            CommandProcessorFactory processorFactory,
            CommandParser commandParser) {
        TelegramBotController botController = new TelegramBotController(token, botName, processorFactory, commandParser);
        try {
            botController.registerBot();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка регистрации Telegram-бота", e);
        }
        return botController;
    }
}




