package org.hofftech.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.OutboxEventService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class FileProcessingConfig {

    @Bean
    public FileSavingService fileSavingService() {
        return new FileSavingService();
    }

    @Bean
    public JsonProcessingService jsonProcessingService(ObjectMapper objectMapper, Clock clock, OutboxEventService outboxEventService) {
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
}

