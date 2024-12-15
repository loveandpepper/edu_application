package org.hofftech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderUtil {
    private static final Logger log = LoggerFactory.getLogger(FileReaderUtil.class);

    public List<String> readAllLines(Path filePath) throws IOException {
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
    }
}
