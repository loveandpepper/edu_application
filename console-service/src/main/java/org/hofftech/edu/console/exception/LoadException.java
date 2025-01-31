package org.hofftech.edu.console.exception;

/**
 * Исключение команды погрузки
 */
public class LoadException extends RuntimeException {

    public LoadException(String message) {
        super(message);
    }
}
