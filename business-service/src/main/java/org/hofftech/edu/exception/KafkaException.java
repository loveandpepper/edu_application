package org.hofftech.edu.exception;

/**
 * Исключение работы через Kafka
 */
public class KafkaException extends RuntimeException {

    public KafkaException(String message) {
        super(message);
    }
}
