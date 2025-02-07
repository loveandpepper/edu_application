package org.hofftech.edu.exception;

/**
 * Исключение валидации
 */
public class ValidateException extends RuntimeException {

    public ValidateException(String message) {
        super(message);
    }
}
