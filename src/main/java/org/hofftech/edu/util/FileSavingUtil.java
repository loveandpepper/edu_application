package org.hofftech.edu.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Утилитный класс для сохранения данных в файлы.
 * Предоставляет методы для записи данных о посылках.
 */
@Slf4j
public final class FileSavingUtil {
    /**
     * Сохраняет данные о посылках в указанный файл.
     *
     * @param packages       карта, где ключ — имя посылки, а значение — количество
     * @param outputFilePath путь к выходному файлу
     * @param withCount      если true, записываются данные с количеством; иначе только имена посылок
     * @throws IOException если произошла ошибка записи в файл
     */
    public static void savePackagesToFile(List<Map.Entry<String, Long>> packages,
                                          String outputFilePath, boolean withCount) throws IOException {
        File outputFile = new File(outputFilePath);
        log.info("Начинаем запись в файл {} количества", withCount ? "с подсчётом" : "без подсчёта");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Long> entry : packages) {
                String line;
                if (withCount) {
                    line = String.format("\"%s\" - %d шт", entry.getKey(), entry.getValue());
                } else {
                    for (int i = 0; i < entry.getValue(); i++) {
                        line = String.format("\"%s\"", entry.getKey());
                        writer.write(line);
                        writer.newLine();
                    }
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
        log.info("Посылки успешно импортированы и сохранены в файл: {}", outputFile.getAbsolutePath());
    }

}
