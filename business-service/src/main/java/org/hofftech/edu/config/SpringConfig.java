package org.hofftech.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.repository.OutboxEventRepository;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.OutboxEventService;
import org.hofftech.edu.service.OutboxOrderService;
import org.hofftech.edu.service.ReportService;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.messaging.Message;

import javax.sql.DataSource;
import java.time.Clock;
import java.util.function.Consumer;

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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public OutboxEventService outboxEventService(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        return new OutboxEventService(outboxEventRepository, objectMapper);
    }

    @Bean
    public JsonProcessingService jsonProcessingService(ObjectMapper objectMapper,
                                                        Clock clock, OutboxEventService outboxEventService) {
        return new JsonProcessingService(objectMapper, clock, outboxEventService);
    }

    @Bean
    public FileProcessingService fileProcessingService(
            ParsingService parsingService,
            ValidatorService validatorService,
            TruckService truckService,
            JsonProcessingService jsonProcessingService,
            PackingStrategyFactory packingStrategyFactory,
            OutboxEventService outboxEventService,
            Clock clock) {
        return new FileProcessingService(
                parsingService, validatorService, truckService,
                jsonProcessingService, packingStrategyFactory, outboxEventService, clock);
    }

    @Bean
    public FileSavingService fileSavingService() {
        return new FileSavingService();
    }

    @Bean
    public ReportService kafkaService(StreamBridge streamBridge, ObjectMapper objectMapper) {return new ReportService(streamBridge, objectMapper);}

    @Bean
    public CommandProcessorFactory commandProcessorFactory(
            PackageRepository packageRepository,
            FileProcessingService fileProcessingService,
            JsonProcessingService jsonProcessingService,
            FileSavingService fileSavingService,
            ValidatorService validatorService,
            PackageMapper packageMapper,
            ReportService reportService
    ) {
        return new CommandProcessorFactory(
                packageRepository,
                fileProcessingService,
                jsonProcessingService,
                fileSavingService,
                validatorService,
                packageMapper,
                reportService);
    }

    @Bean
    public ReportService reportService(StreamBridge streamBridge, ObjectMapper objectMapper) {
        return new ReportService(streamBridge, objectMapper);
    }

    @Bean
    public OutboxOrderService outboxOrderPublisher(OutboxEventRepository outboxEventRepository, StreamBridge streamBridge) {
        return new OutboxOrderService(outboxEventRepository, streamBridge);
    }

    @Bean
    public Consumer<Message<String>> reportResponseConsumer(ReportService reportService) {
        return reportService.getReportResponseConsumer();
    }

}




