package org.hofftech.edu.exception;

/**
 * Исключение погрузки из txt
 */
public class TxtLoadException extends RuntimeException {

    public TxtLoadException(String message) {
        super(message);
    }
}
