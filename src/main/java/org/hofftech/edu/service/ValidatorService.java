package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Сервис для валидации данных.
 * Отвечает за проверку файлов, форм посылок и других данных.
 */
@Slf4j
public class ValidatorService {

    private static final String FORM_SPLITTER = ",";
    private static final int FIRST_ROW_INDEX = 0;

    /**
     * Проверяет возможность парсинга формы посылки и возвращает список строк, представляющих форму.
     *
     * @param form строка, описывающая форму посылки
     * @return список строк, представляющих форму
     * @throws IllegalArgumentException если форма не указана или некорректна
     */
    public static List<String> validateFord(String form) {
        if (form == null || form.isEmpty()) {
            throw new IllegalArgumentException("Форма посылки не указана.");
        }

        if (form.contains(FORM_SPLITTER)) {
            List<String> rows = List.of(form.split(FORM_SPLITTER));
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

        for (int i = FIRST_ROW_INDEX; i < height - 1; i++) {
            String currentRow = rows.get(i);
            String nextRow = rows.get(i + 1);

            for (int j = FIRST_ROW_INDEX; j < currentRow.length(); j++) {
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

    /**
     * Проверяет, содержит ли файл допустимые данные.
     *
     * @param lines список строк из файла
     * @return true, если файл валиден, иначе false
     */
    public boolean isValidFile(List<String> lines) {
        if (lines.isEmpty()) {
            throw new RuntimeException("Файл пустой или не содержит данных.");
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
        return true;
    }
}
