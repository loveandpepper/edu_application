package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если не удается создать посылку.
 */
public class OutputFileException extends RuntimeException {

    public OutputFileException(String message) {
        super(message);
    }
}
