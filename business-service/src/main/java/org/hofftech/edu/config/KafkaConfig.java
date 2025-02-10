package org.hofftech.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hofftech.edu.repository.OutboxEventRepository;
import org.hofftech.edu.service.OutboxEventService;
import org.hofftech.edu.service.OutboxOrderService;
import org.hofftech.edu.service.ReportService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class KafkaConfig {

    @Bean
    public OutboxEventService outboxEventService(OutboxEventRepository outboxEventRepository,
                                                 ObjectMapper objectMapper) {
        return new OutboxEventService(outboxEventRepository, objectMapper);
    }

    @Bean
    public OutboxOrderService outboxOrderPublisher(OutboxEventRepository outboxEventRepository,
                                                   StreamBridge streamBridge, ObjectMapper objectMapper) {
        return new OutboxOrderService(outboxEventRepository, streamBridge, objectMapper);
    }

    @Bean
    public Consumer<Message<String>> reportResponseConsumer(ReportService reportService) {
        return reportService.getReportResponseConsumer();
    }
}
