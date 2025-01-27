package org.hofftech.edu.exception;

/**
 * Исключение, если процессор для команды не найден
 */
public class ProcessorException extends RuntimeException {

    public ProcessorException(String message) {
        super(message);
    }
}
