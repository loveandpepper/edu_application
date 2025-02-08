package org.hofftech.edu.billing.repository;

import org.hofftech.edu.billing.model.InboxEventEntity;
import org.hofftech.edu.billing.model.dto.InboxEventDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InboxEventRepository extends JpaRepository<InboxEventEntity, Long> {
    Optional<InboxEventEntity> findByEventId(String eventId);
}
