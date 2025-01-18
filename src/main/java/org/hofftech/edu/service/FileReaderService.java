package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.InputFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Утилитный класс для чтения строк из файлов.
 * Предоставляет методы для безопасного чтения данных.
 */
@Slf4j
public final class FileReaderService {
    /**
     * Читает все строки из указанного файла.
     *
     * @param filePath путь к файлу
     * @return список строк, содержащихся в файле
     * @throws RuntimeException если файл не существует или недоступен для чтения
     */
    public static List<String> readAllLines(Path filePath) {
        try {
            if (!Files.exists(filePath)) {
                throw new InputFileException("Файл не существует: " + filePath);
            }
            if (!Files.isReadable(filePath)) {
                throw new InputFileException("Файл недоступен для чтения: " + filePath);
            }
            log.info("Чтение строк из файла: {}", filePath);
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new InputFileException("Ошибка чтения файла: " + filePath);
        }
    }
}
