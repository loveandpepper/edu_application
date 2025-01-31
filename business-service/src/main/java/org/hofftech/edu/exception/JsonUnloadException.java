package org.hofftech.edu.exception;

/**
 * Исключение импорта из json
 */
public class JsonUnloadException extends RuntimeException {

    public JsonUnloadException(String message) {
        super(message);
    }
}
