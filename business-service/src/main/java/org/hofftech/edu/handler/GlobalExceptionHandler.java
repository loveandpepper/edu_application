package org.hofftech.edu.handler;

import org.hofftech.edu.exception.FileParsingException;
import org.hofftech.edu.exception.InputFileException;
import org.hofftech.edu.exception.JsonLoadException;
import org.hofftech.edu.exception.JsonSerializeException;
import org.hofftech.edu.exception.JsonUnloadException;
import org.hofftech.edu.exception.KafkaException;
import org.hofftech.edu.exception.LoadException;
import org.hofftech.edu.exception.OutputFileException;
import org.hofftech.edu.exception.PackageArgumentException;
import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.exception.ProcessorException;
import org.hofftech.edu.exception.TruckException;
import org.hofftech.edu.exception.TxtLoadException;
import org.hofftech.edu.exception.TxtUnloadException;
import org.hofftech.edu.exception.UnloadException;
import org.hofftech.edu.exception.UpdateException;
import org.hofftech.edu.exception.UserNotProvidedException;
import org.hofftech.edu.exception.ValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            FileParsingException.class,
            KafkaException.class,
            JsonSerializeException.class,
            InputFileException.class,
            JsonLoadException.class,
            JsonUnloadException.class,
            LoadException.class,
            OutputFileException.class,
            PackageArgumentException.class,
            PackageNameException.class,
            PackageNotFoundException.class,
            ProcessorException.class,
            TruckException.class,
            TxtLoadException.class,
            TxtUnloadException.class,
            UnloadException.class,
            UpdateException.class,
            UserNotProvidedException.class,
            ValidateException.class
    })
    public ResponseEntity<String> handleCustomExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Ошибка: " + ex.getMessage());
    }
}

