package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если не удается создать посылку.
 */
public class PackageNameException extends RuntimeException {

    public PackageNameException(String message) {
        super(message);
    }
}
