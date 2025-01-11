package org.hofftech.edu.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Утилитный класс для чтения строк из файлов.
 * Предоставляет методы для безопасного чтения данных.
 */
@Slf4j
public final class FileReaderUtil {
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
                log.error("Файл не существует: {}", filePath);
                throw new IOException("Файл не существует: " + filePath);
            }
            if (!Files.isReadable(filePath)) {
                log.error("Файл недоступен для чтения: {}", filePath);
                throw new IOException("Файл недоступен для чтения: " + filePath);
            }
            log.info("Чтение строк из файла: {}", filePath);
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            log.error("Произошла ошибка чтения файла {}", filePath);
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
    }
}
