CREATE SCHEMA IF NOT EXISTS billing;

CREATE TABLE billing.inbox_event (
                             id BIGSERIAL PRIMARY KEY,
                             event_id VARCHAR(255),
                             event_type VARCHAR(255),
                             payload TEXT,
                             received_at TIMESTAMP NOT NULL,
                             processed BOOLEAN NOT NULL DEFAULT FALSE
);
