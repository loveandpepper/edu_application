package org.hofftech.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hofftech.edu.factory.PackingStrategyFactory;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.PackingService;
import org.hofftech.edu.service.ParsingService;
import org.hofftech.edu.service.ReportService;
import org.hofftech.edu.service.TruckService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

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
    public ReportService reportService(StreamBridge streamBridge) {
        return new ReportService(streamBridge);
    }
}

