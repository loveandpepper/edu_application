package org.hofftech.edu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.JsonSerializeException;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.dto.OutboxEventDto;
import org.hofftech.edu.repository.OutboxEventRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    /**
     * Сохраняет событие заказа (outbox event) в новой транзакции.
     *
     * @param order     объект заказа
     * @param eventType тип события
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrderEvent(Order order, String eventType) {
        OutboxEventDto event = new OutboxEventDto();
        event.setEventType(eventType);
        event.setPayload(convertOrderToJson(order));
        event.setReceivedAt(Instant.now());
        event.setEventId(UUID.randomUUID().toString());
        event.setSent(false);

        outboxEventRepository.save(event);
        log.info("Событие {} успешно сохранено для заказа пользователя {}", eventType, order.getUserId());
    }

    private String convertOrderToJson(Order order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (Exception e) {
            throw new JsonSerializeException("Ошибка сериализации заказа в JSON " + e.getMessage());
        }
    }
}
