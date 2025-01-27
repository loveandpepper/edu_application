package org.hofftech.edu.exception;

/**
 * Исключение при работе с выходным файлом
 */
public class OutputFileException extends RuntimeException {

    public OutputFileException(String message) {
        super(message);
    }
}
