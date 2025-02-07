package org.hofftech.edu.exception;

/**
 * Исключение при обработке входящего файлс
 */
public class InputFileException extends RuntimeException {

    public InputFileException(String message) {
        super(message);
    }
}
