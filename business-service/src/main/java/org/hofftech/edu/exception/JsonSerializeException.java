package org.hofftech.edu.exception;

/**
 * Исключение импорта из json
 */
public class JsonSerializeException extends RuntimeException {

    public JsonSerializeException(String message) {
        super(message);
    }
}
