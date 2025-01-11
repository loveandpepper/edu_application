package org.hofftech.edu.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий грузовик.
 * Содержит информацию о вместимости, текущей загрузке и других свойствах.
 */
@Getter
@ToString
public class Truck {
    private final int width;
    private final int height;

    private final char[][] grid;
    private final List<Package> packages;

    public Truck(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        this.packages = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = ' ';
            }
        }
    }
}
















