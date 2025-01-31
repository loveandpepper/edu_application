package org.hofftech.edu.exception;

/**
 * Исключение разгрузки в txt
 */
public class TxtUnloadException extends RuntimeException {

    public TxtUnloadException(String message) {
        super(message);
    }
}
