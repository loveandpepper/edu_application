package org.hofftech.edu.config;

import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.mapper.PackageMapper;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.FileProcessingService;
import org.hofftech.edu.service.FileSavingService;
import org.hofftech.edu.service.JsonProcessingService;
import org.hofftech.edu.service.ReportService;
import org.hofftech.edu.service.ValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

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
}

