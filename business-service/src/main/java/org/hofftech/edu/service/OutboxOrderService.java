package org.hofftech.edu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.KafkaException;
import org.hofftech.edu.model.dto.OutboxEventDto;
import org.hofftech.edu.repository.OutboxEventRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OutboxOrderService {

    private final OutboxEventRepository outboxEventRepository;
    private final StreamBridge streamBridge;

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEventDto> events = outboxEventRepository.findBySentFalse();
        if (events.isEmpty()) {
            return;
        }

        for (OutboxEventDto ev : events) {
            try {
                streamBridge.send("orderCreated-out-0", ev);
                ev.setSent(true);
                outboxEventRepository.save(ev);
            } catch (Exception e) {
                throw new KafkaException("Ошибка отправки события outbox: " + e.getMessage());
            }
        }
    }
}
