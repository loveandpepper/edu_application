package org.hofftech.edu.exception;

/**
 * Исключение сохранения в json
 */
public class JsonLoadException extends RuntimeException {

    public JsonLoadException(String message) {
        super(message);
    }
}
