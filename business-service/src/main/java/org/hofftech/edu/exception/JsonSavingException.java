package org.hofftech.edu.exception;

/**
 * Исключение сохранения в json
 */
public class JsonSavingException extends RuntimeException {

    public JsonSavingException(String message) {
        super(message);
    }
}
