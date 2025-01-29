package org.hofftech.edu.exception;

/**
 * Исключение погрузки
 */
public class LoadException extends RuntimeException {

    public LoadException(String message) {
        super(message);
    }
}
