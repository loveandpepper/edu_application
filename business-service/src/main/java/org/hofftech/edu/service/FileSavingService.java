package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.TxtUnloadException;

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
public final class FileSavingService {
    /**
     * Сохраняет данные о посылках в указанный файл.
     *
     * @param packages       список карт: каждая карта - имя посылки, а значение - количество.
     * @param outputFilePath путь к выходному файлу.
     * @param isWithCount      если true, пишется имя посылки с количеством; иначе только имена посылок.
     */
    public void savePackagesToFile(List<Map<String, Long>> packages, String outputFilePath, boolean isWithCount){
        File outputFile = new File(outputFilePath);
        log.info("Начинаем запись в файл {} количества: {}", outputFilePath, isWithCount ? "с подсчётом" : "без подсчёта");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map<String, Long> map : packages) {
                writePackageMapToFile(map, writer, isWithCount);
            }
        } catch (IOException e) {
            throw new TxtUnloadException("Ошибка записи в файл: " + e.getMessage());
        }

        log.info("Посылки успешно импортированы и сохранены в файл: {}", outputFile.getAbsolutePath());
    }

    /**
     * Пишет содержимое одной карты в файл.
     *
     * @param map       карта: имя посылки - количество.
     * @param writer    BufferedWriter для записи в файл.
     * @param withCount если true, записывается с количеством.
     */
    private void writePackageMapToFile(Map<String, Long> map, BufferedWriter writer, boolean withCount) {
        for (String key : map.keySet()) {
            if (withCount) {
                writeLineWithCount(writer, key, map.get(key));
            } else {
                writeLinesWithoutCount(writer, key, map.get(key));
            }
        }
    }

    /**
     * Записывает строку с количеством в файл.
     *
     * @param writer BufferedWriter для записи в файл.
     * @param key    имя посылки.
     * @param value  количество посылок.
     */
    private void writeLineWithCount(BufferedWriter writer, String key, Long value) {
        String line = String.format("%s - %d шт", key, value);
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new TxtUnloadException(e.getMessage());
        }

    }

    /**
     * Записывает строки без количества в файл.
     *
     * @param writer BufferedWriter для записи в файл.
     * @param key    имя посылки.
     * @param value  количество посылок.
     */
    private void writeLinesWithoutCount(BufferedWriter writer, String key, Long value) {
        for (int i = 0; i < value; i++) {
            try {
                writer.write(key);
                writer.newLine();
            } catch (IOException e) {
                throw new TxtUnloadException(e.getMessage());
            }
        }
    }
}

