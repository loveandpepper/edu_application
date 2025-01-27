package org.hofftech.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hofftech.edu.controller.ApiCommandController;
//import org.hofftech.edu.controller.TelegramBotController;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.OrderManagerService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.Clock;

/**
 * Контекст приложения, отвечает за создание и конфигурацию зависимостей.
 * Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Configuration
public class SpringConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PackageRepository packageRepository(JdbcTemplate jdbcTemplate) {
        return new PackageRepository(jdbcTemplate);
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
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Bean
    public JsonProcessingService jsonProcessingService(ObjectMapper objectMapper,
                                                       OrderManagerService orderManagerService, Clock clock) {
        return new JsonProcessingService(objectMapper, clock, orderManagerService);
    }

    @Bean
    public FileProcessingService fileProcessingService(
            ParsingService parsingService,
            ValidatorService validatorService,
            TruckService truckService,
            JsonProcessingService jsonProcessingService,
            PackingStrategyFactory packingStrategyFactory,
            OrderManagerService orderManagerService,
            Clock clock) {
        return new FileProcessingService(
                parsingService, validatorService, truckService,
                jsonProcessingService, packingStrategyFactory, orderManagerService, clock);
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
    public ApiCommandController apiCommandController(CommandProcessorFactory processorFactory) {
        return new ApiCommandController(processorFactory);
    }

}




