package org.hofftech.edu.exception;

/**
 * Исключение парсинга txt файла
 */
public class FileParsingException extends RuntimeException {

    public FileParsingException(String message) {
        super(message);
    }
}
