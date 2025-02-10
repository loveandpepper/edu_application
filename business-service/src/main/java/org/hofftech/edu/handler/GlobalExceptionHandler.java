package org.hofftech.edu.handler;

import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.UserNotProvidedException;
import org.hofftech.edu.exception.ValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotProvidedException.class,
            ValidateException.class,
            PackageNameException.class,
            PackageArgumentException.class
    })
    public ResponseEntity<String> handleValidationExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}


