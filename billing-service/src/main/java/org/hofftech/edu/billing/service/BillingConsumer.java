package org.hofftech.edu.billing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.billing.exception.BillingException;
import org.hofftech.edu.billing.model.InboxEventEntity;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.dto.ReportRequestDto;
import org.hofftech.edu.billing.repository.InboxEventRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Slf4j
@RequiredArgsConstructor
public class BillingConsumer {

    private static final String CORRELATION_ID = "correlationId";
    private final InboxEventRepository inboxRepository;
    private final OrderManagerService orderManagerService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handleOrderEvent(Message<Order> message) {
        try {
            String eventId = message.getHeaders().get("eventId", String.class);
            String eventType = message.getHeaders().get("eventType", String.class);

            Order order = message.getPayload();
            String rawOrderJson = objectMapper.writeValueAsString(order);

            createInboxEvent(eventId, eventType, rawOrderJson);

            orderManagerService.addOrder(order);
        } catch (Exception e) {
            throw new BillingException("Ошибка обработки события " + e.getMessage());
        }
    }

    private void createInboxEvent(String eventId, String eventType, String rawOrderJson) {
        InboxEventEntity inboxEventEntity = new InboxEventEntity();
        inboxEventEntity.setEventId(eventId);
        inboxEventEntity.setEventType(eventType);
        inboxEventEntity.setPayload(rawOrderJson);
        inboxEventEntity.setReceivedAt(Instant.now());
        inboxEventEntity.setProcessed(true);
        inboxRepository.save(inboxEventEntity);

        log.info("Событие eventId {} успешно обработано", eventId);
    }


    /**
     * Функция для обработки запроса отчёта с поддержкой correlationId.
     * Принимает входящее сообщение (Message<String>) и возвращает ответное сообщение (Message<String>),
     * сохраняя заголовок correlationId для синхронизации с запросом.
     * Обрабатывает сообщения из топика report-request-topic и отправляет ответы в report-response-topic.
     */
    @Transactional
    public Message<String> handleReportRequest(Message<ReportRequestDto> message) {
        String correlationId = message.getHeaders().get(CORRELATION_ID, String.class);
        try {
            ReportRequestDto request = message.getPayload();
            log.info("Обрабатываем запрос на отчёт для пользователя {}", request.getUserId());

            String reportData = orderManagerService.generateReport(
                    request.getUserId(), request.getFromDate(), request.getToDate());
            log.info("Отчёт для пользователя {} сгенерирован", request.getUserId());

            return MessageBuilder.withPayload(reportData)
                    .setHeader(CORRELATION_ID, correlationId)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return MessageBuilder.withPayload("Ошибка формирования отчета " + e.getMessage())
                    .setHeader(CORRELATION_ID, correlationId)
                    .build();
        }
    }
}
