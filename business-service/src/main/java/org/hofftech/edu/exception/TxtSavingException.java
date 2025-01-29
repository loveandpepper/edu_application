package org.hofftech.edu.exception;

/**
 * Исключение разгрузки в txt
 */
public class TxtSavingException extends RuntimeException {

    public TxtSavingException(String message) {
        super(message);
    }
}
