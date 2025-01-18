package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если не удается создать посылку.
 */
public class InputFileException extends RuntimeException {

    public InputFileException(String message) {
        super(message);
    }
}
