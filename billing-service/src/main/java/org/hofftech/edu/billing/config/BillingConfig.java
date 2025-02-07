package org.hofftech.edu.billing.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hofftech.edu.billing.service.BillingConsumer;
import org.hofftech.edu.billing.service.OrderManagerService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hofftech.edu.billing.repository.InboxEventRepository;
import org.springframework.messaging.Message;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.sql.DataSource;

/**
 * Контекст приложения, отвечает за создание и конфигурацию зависимостей.
 * Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Configuration
public class BillingConfig {

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
    public OrderManagerService orderManagerService(){
        return new OrderManagerService();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public BillingConsumer billingConsumer(InboxEventRepository inboxRepository,
                                           OrderManagerService orderManagerService,
                                           ObjectMapper objectMapper) {
        return new BillingConsumer(inboxRepository, orderManagerService, objectMapper);
    }

    @Bean
    public Consumer<String> orderEventConsumer(BillingConsumer billingConsumer) {
        return billingConsumer::handleOrderEvent;
    }

    @Bean
    public Function<Message<String>, Message<String>> reportRequestFunction(BillingConsumer billingConsumer) {
        return billingConsumer::handleReportRequest;
    }




}




