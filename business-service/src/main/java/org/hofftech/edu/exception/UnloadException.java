package org.hofftech.edu.exception;

/**
 * Исключение погрузки
 */
public class UnloadException extends RuntimeException {

    public UnloadException(String message) {
        super(message);
    }
}
