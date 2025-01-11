package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Сервис для валидации данных.
 * Отвечает за проверку файлов, форм посылок и других данных.
 */
@Slf4j
public class ValidatorService {
    /**
     * Проверяет, содержит ли файл допустимые данные.
     *
     * @param lines список строк из файла
     * @return true, если файл валиден, иначе false
     */
    public boolean isValidFile(List<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            log.error("Файл пустой или не содержит данных.");
            return false;
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
        return true;
    }

    /**
     * Проверяет возможность парсинга формы посылки и возвращает список строк, представляющих форму.
     *
     * @param form строка, описывающая форму посылки
     * @return список строк, представляющих форму
     * @throws IllegalArgumentException если форма не указана или некорректна
     */
    public static List<String> isAbleToParseForm(String form) {
        if (form == null || form.isEmpty()) {
            throw new IllegalArgumentException("Форма посылки не указана.");
        }

        if (form.contains(",")) {
            List<String> rows = List.of(form.split(","));
            validateDiagonalTouch(rows);
            return rows;
        }

        return List.of(form);
    }

    /**
     * Проверяет, чтобы форма посылки не имела диагонально соединённых символов.
     *
     * @param rows список строк, представляющих форму посылки
     * @throws IllegalArgumentException если форма содержит некорректное диагональное соединение
     */
    public static void validateDiagonalTouch(List<String> rows) {
        int height = rows.size();

        for (int i = 0; i < height - 1; i++) {
            String currentRow = rows.get(i);
            String nextRow = rows.get(i + 1);

            for (int j = 0; j < currentRow.length(); j++) {
                char current = currentRow.charAt(j);

                if (current != ' ') {
                    boolean hasLeft = j > 0 && currentRow.charAt(j - 1) != ' ';
                    boolean hasRight = j < currentRow.length() - 1 && currentRow.charAt(j + 1) != ' ';
                    boolean hasBottom = j < nextRow.length() && nextRow.charAt(j) != ' ';

                    if ((!hasLeft && !hasBottom) || (!hasRight && !hasBottom)) {
                        throw new IllegalArgumentException("Символ в позиции (" + i + ", " + j + ") висит в воздухе.");
                    }
                }
            }
        }
    }
}
