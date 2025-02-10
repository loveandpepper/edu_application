package org.hofftech.edu.billing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.dto.ReportRequestDto;
import org.hofftech.edu.billing.repository.InboxEventRepository;
import org.hofftech.edu.billing.service.BillingConsumer;
import org.hofftech.edu.billing.service.OrderManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public BillingConsumer billingConsumer(InboxEventRepository inboxRepository,
                                           OrderManagerService orderManagerService,
                                           ObjectMapper objectMapper) {
        return new BillingConsumer(inboxRepository, orderManagerService, objectMapper);
    }

    @Bean
    public Consumer<Message<Order>> orderEventConsumer(BillingConsumer billingConsumer) {
        return billingConsumer::handleOrderEvent;
    }

    @Bean
    public Function<Message<ReportRequestDto>, Message<String>> reportRequestFunction(BillingConsumer billingConsumer) {
        return billingConsumer::handleReportRequest;
    }
}
