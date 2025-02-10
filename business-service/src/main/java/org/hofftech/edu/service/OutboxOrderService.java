package org.hofftech.edu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.KafkaException;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.dto.OutboxEventDto;
import org.hofftech.edu.repository.OutboxEventRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OutboxOrderService {

    private final OutboxEventRepository outboxEventRepository;
    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEventDto> events = outboxEventRepository.findBySentFalse();
        if (events.isEmpty()) {
            return;
        }

        for (OutboxEventDto ev : events) {
            try {
                Order order = objectMapper.readValue(ev.getPayload(), Order.class);

                Message<Order> message = MessageBuilder
                        .withPayload(order)
                        .setHeader("eventId", ev.getEventId())
                        .setHeader("eventType", ev.getEventType())
                        .build();

                streamBridge.send("orderCreated-out-0", message);
                ev.setSent(true);
                outboxEventRepository.save(ev);
            } catch (Exception e) {
                throw new KafkaException("Ошибка отправки события outbox: " + e.getMessage());
            }
        }
    }
}
