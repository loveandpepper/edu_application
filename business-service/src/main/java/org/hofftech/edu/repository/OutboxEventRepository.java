package org.hofftech.edu.repository;

import org.hofftech.edu.model.dto.OutboxEventDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OutboxEventRepository extends JpaRepository<OutboxEventDto, Long> {
    List<OutboxEventDto> findBySentFalse();
}
