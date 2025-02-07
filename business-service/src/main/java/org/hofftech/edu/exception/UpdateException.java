package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое при обновлении посылки
 */
public class UpdateException extends RuntimeException {

    public UpdateException(String message) {
        super(message);
    }
}
