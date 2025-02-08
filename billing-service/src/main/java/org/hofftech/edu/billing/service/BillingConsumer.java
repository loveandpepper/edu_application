package org.hofftech.edu.billing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.billing.exception.BillingException;
import org.hofftech.edu.billing.mapper.InboxEventMapper;
import org.hofftech.edu.billing.model.InboxEventEntity;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.dto.InboxEventDto;
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
    private final InboxEventMapper inboxEventMapper;

    @Transactional
    public void handleOrderEvent(InboxEventDto inboxEventDto) {
        try {
            InboxEventEntity inboxEventEntity = inboxEventMapper.toEntity(inboxEventDto);

            inboxRepository.save(inboxEventEntity);
            log.info("Событие eventId {} успешно обработано", inboxEventDto.getEventId());

            Order order = objectMapper.readValue(inboxEventDto.getPayload(), Order.class);
            orderManagerService.addOrder(order);
        } catch (Exception e) {
            throw new BillingException("Ошибка обработки события " + e.getMessage());
        }
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
