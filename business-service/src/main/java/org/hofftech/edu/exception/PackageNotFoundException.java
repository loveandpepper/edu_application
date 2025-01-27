package org.hofftech.edu.exception;

/**
 * Исключение, выбрасываемое, если посылка не найдена.
 */
public class PackageNotFoundException extends RuntimeException {

    public PackageNotFoundException(String message) {
        super(message);
    }
}
