package org.hofftech.edu.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Entity
@Table(schema = "edu", name = "outbox_event")
@Getter
@Setter
public class OutboxEventDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(nullable = false)
    private boolean sent = false;
}
