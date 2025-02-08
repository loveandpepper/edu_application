package org.hofftech.edu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.KafkaException;
import org.hofftech.edu.model.dto.ReportRequestDto;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final StreamBridge streamBridge;

    private final Map<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    /**
     * Отправляет запрос на отчёт и ожидает ответа в течение заданного таймаута.
     *
     * @param user     идентификатор пользователя
     * @param dateFrom дата начала отчёта
     * @param dateTo   дата окончания отчёта
     * @return сгенерированный отчёт (JSON-строка)
     */
    public String generateReport(String user, String dateFrom, String dateTo) {
        try {
            String correlationId = UUID.randomUUID().toString();

            ReportRequestDto request = new ReportRequestDto(user, dateFrom, dateTo);

            Message<ReportRequestDto> message = MessageBuilder
                    .withPayload(request)
                    .setHeader("correlationId", correlationId)
                    .build();

            CompletableFuture<String> future = new CompletableFuture<>();
            pendingRequests.put(correlationId, future);

            boolean sent = streamBridge.send("reportRequest-out-0", message);
            if (!sent) {
                pendingRequests.remove(correlationId);
                throw new KafkaException("Не удалось отправить запрос отчёта");
            }

            log.info("Запрос на отчёт отправлен с correlationId: {}", correlationId);

            String reply = future.get(30, TimeUnit.SECONDS);
            pendingRequests.remove(correlationId);
            log.info("Получен ответ для correlationId {}: {}", correlationId, reply);
            return reply;
        } catch (Exception e) {
            throw new KafkaException("Ошибка при ожидании ответа на запрос отчёта " + e.getMessage());
        }
    }

    /**
     * Consumer для обработки входящих ответов из топика report-response-topic.
     */
    public Consumer<Message<String>> getReportResponseConsumer() {
        return message -> {
            String correlationId = message.getHeaders().get("correlationId", String.class);
            if (correlationId != null) {
                CompletableFuture<String> future = pendingRequests.get(correlationId);
                if (future != null) {
                    future.complete(message.getPayload());
                } else {
                    log.warn("Не найден ожидающий запрос с correlationId: {}", correlationId);
                }
            } else {
                log.warn("Получено сообщение без correlationId");
            }
        };
    }
}
