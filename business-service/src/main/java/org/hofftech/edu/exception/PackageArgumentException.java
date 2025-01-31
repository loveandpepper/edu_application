package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если не удается создать посылку.
 */
public class PackageArgumentException extends RuntimeException {

    public PackageArgumentException(String message) {
        super(message);
    }
}
