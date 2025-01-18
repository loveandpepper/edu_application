package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если не удается создать посылку.
 */
public class ValidateException extends RuntimeException {

    public ValidateException(String message) {
        super(message);
    }
}
