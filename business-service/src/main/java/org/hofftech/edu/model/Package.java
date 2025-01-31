package org.hofftech.edu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий упаковку.
 * Содержит информацию о свойствах упаковки.
 */

@Entity
@Table(schema = "edu", name = "\"package\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Package implements Comparable<Package> {

    @Id
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * Хранение массива строк (text[]) в PostgreSQL.
     *
     * Hibernate 6 позволяет указать @JdbcTypeCode(SqlTypes.ARRAY),
     * чтобы корректно маппить колонку типа text[] к String[].
     */
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "shape", columnDefinition = "text[]", nullable = false)
    private String[] shape;

    @Column(nullable = false, length = 1)
    private char symbol;

    @Column(name = "start_position_x", nullable = false)
    private int startPositionX;

    @Column(name = "start_position_y", nullable = false)
    private int startPositionY;

    public int getWidth() {
        return shape[0].length();
    }

    public int getHeight() {
        return shape.length;
    }

    /**
     * Обновляет символ упаковки на новый.
     * Также заменяет старый символ на новый во всех строках формы упаковки.
     *
     * @param newSymbol новый символ для упаковки.
     */
    public void updateSymbol(char newSymbol) {
        if (shape == null || shape.length == 0) {
            return;
        }

        String[] updatedShape = new String[shape.length];
        for (int i = 0; i < shape.length; i++) {
            updatedShape[i] = shape[i].replace(symbol, newSymbol);
        }
        this.shape = updatedShape;
    }

    /**
     * Возвращает перевёрнутую форму упаковки.
     * Формирует новый список строк, в котором порядок строк обратный оригиналу.
     *
     * @return перевёрнутая форма упаковки.
     */
    public String[] getReversedShape() {
        if (this.shape == null || this.shape.length == 0) {
            return new String[0];
        }
        String[] reversed = Arrays.copyOf(this.shape, this.shape.length);
        for (int i = 0; i < reversed.length / 2; i++) {
            String temp = reversed[i];
            reversed[i] = reversed[reversed.length - 1 - i];
            reversed[reversed.length - 1 - i] = temp;
        }
        return reversed;
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
