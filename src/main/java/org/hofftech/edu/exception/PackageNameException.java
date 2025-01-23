package org.hofftech.edu.exception;

/**
 * Исключение при отсутствии имени посылки
 */
public class PackageNameException extends RuntimeException {

    public PackageNameException(String message) {
        super(message);
    }
}
