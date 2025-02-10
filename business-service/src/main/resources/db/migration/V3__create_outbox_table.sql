CREATE SCHEMA IF NOT EXISTS edu;

CREATE TABLE edu.outbox_event (
                                  id BIGSERIAL PRIMARY KEY,
                                  event_id VARCHAR(255) NOT NULL UNIQUE,
                                  event_type VARCHAR(255) NOT NULL,
                                  payload TEXT NOT NULL,
                                  received_at TIMESTAMP NOT NULL,
                                  sent BOOLEAN DEFAULT FALSE NOT NULL
);