package org.hofftech.edu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий упаковку.
 * Содержит информацию о свойствах упаковки.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Package implements Comparable<Package> {
    private String name;
    private List<String> shape;
    private char symbol;
    private PackageStartPosition packageStartPosition;

    public int getWidth() {
        return shape.getFirst().length();
    }

    public int getHeight() {
        return shape.size();
    }

    /**
     * Обновляет символ упаковки на новый.
     * Также заменяет старый символ на новый во всех строках формы упаковки.
     *
     * @param newSymbol новый символ для упаковки.
     */
    public void updateSymbol(char newSymbol) {
        if (shape == null || shape.isEmpty()) {
            return;
        }

        List<String> updatedShape = new ArrayList<>();
        for (String row : shape) {
            String updatedRow = row.replace(symbol, newSymbol);
            updatedShape.add(updatedRow);
        }
        this.shape = updatedShape;
        this.symbol = newSymbol;
    }

    /**
     * Возвращает перевёрнутую форму упаковки.
     * Формирует новый список строк, в котором порядок строк обратный оригиналу.
     *
     * @return перевёрнутая форма упаковки.
     */
    public List<String> getReversedShape() {
        List<String> reversedShape = new ArrayList<>(this.shape);
        Collections.reverse(reversedShape);
        return reversedShape;
    }

    /**
     * Сравнивает текущую упаковку с другой по высоте, а затем по ширине.
     * Упаковка с большей высотой считается больше.
     * Если высоты равны, сравнение проводится по ширине.
     *
     * @param other другая упаковка для сравнения.
     * @return отрицательное число, ноль или положительное число,
     * в зависимости от порядка текущей упаковки относительно другой.
     */
    @Override
    public int compareTo(Package other) {
        int heightDiff = Integer.compare(other.getHeight(), this.getHeight());
        if (heightDiff == 0) {
            return Integer.compare(other.getWidth(), this.getWidth());
        }
        return heightDiff;
    }
}
