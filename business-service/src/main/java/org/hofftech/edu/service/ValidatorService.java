package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.ValidateException;

import java.util.List;

/**
 * Сервис для валидации данных.
 * Отвечает за проверку файлов, форм посылок и других данных.
 */
@Slf4j
public class ValidatorService {

    private static final String FORM_SPLITTER = ",";
    private static final int FIRST_ROW_INDEX = 0;

    private static void checkSymbolsBelowShape(String currentRow, String nextRow) {
        for (int j = FIRST_ROW_INDEX; j < currentRow.length(); j++) {
            char current = currentRow.charAt(j);

            if (current != ' ') {
                boolean hasLeft = j > 0 && currentRow.charAt(j - 1) != ' ';
                boolean hasRight = j < currentRow.length() - 1 && currentRow.charAt(j + 1) != ' ';
                boolean hasBottom = j < nextRow.length() && nextRow.charAt(j) != ' ';

                if ((!hasLeft && !hasBottom) || (!hasRight && !hasBottom)) {
                    throw new ValidateException("Символ висит в воздухе");
                }
            }
        }
    }

    /**
     * Проверяет возможность парсинга формы посылки и возвращает список строк, представляющих форму.
     *
     * @param form строка, описывающая форму посылки
     * @return список строк, представляющих форму
     * @throws IllegalArgumentException если форма не указана или некорректна
     */
    public String[] validateForm(String form) {
        if (form == null || form.isEmpty()) {
            throw new ValidateException("Форма посылки не указана.");
        }

        if (form.contains(FORM_SPLITTER)) {
            String[] rows = form.split(FORM_SPLITTER);
            validateDiagonalTouch(rows);
            return rows;
        }

        return form.split(FORM_SPLITTER);
    }

    /**
     * Проверяет, чтобы форма посылки не имела диагонально соединённых символов.
     *
     * @param rows список строк, представляющих форму посылки
     * @throws IllegalArgumentException если форма содержит некорректное диагональное соединение
     */
    public void validateDiagonalTouch(String[] rows) {
        int height = rows.length;

        for (int i = FIRST_ROW_INDEX; i < height - 1; i++) {
            String currentRow = rows[i];
            String nextRow = rows[i + 1];

            checkSymbolsBelowShape(currentRow, nextRow);
        }
    }

    /**
     * Проверяет, содержит ли файл допустимые данные.
     *
     * @param lines список строк из файла
     */
    public void validateFile(List<String> lines) {
        if (lines.isEmpty()) {
            throw new ValidateException("Файл пустой или не содержит данных.");
        }
        log.info("Файл успешно проверен. Количество строк: {}", lines.size());
    }
}
