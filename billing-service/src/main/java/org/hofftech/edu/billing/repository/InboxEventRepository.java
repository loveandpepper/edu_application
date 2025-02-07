package org.hofftech.edu.billing.repository;

import org.hofftech.edu.billing.model.dto.InboxEventDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InboxEventRepository extends JpaRepository<InboxEventDto, Long> {
    Optional<InboxEventDto> findByEventId(String eventId);
}
