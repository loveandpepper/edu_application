package org.hofftech.edu.console.exception;

/**
 * Исключение команды разгрузки
 */
public class UnloadException extends RuntimeException {

    public UnloadException(String message) {
        super(message);
    }
}
