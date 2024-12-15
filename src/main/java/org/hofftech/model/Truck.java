package org.hofftech.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Truck {
    @Getter                             //почему-то константы нужно отдельно помечать, иначе не работает
    private static final int WIDTH = 6;
    @Getter
    private static final int HEIGHT = 6;

    private final char[][] grid;
    private final List<Package> packages;

    public Truck() {
        this.grid = new char[HEIGHT][WIDTH];
        this.packages = new ArrayList<>();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                grid[i][j] = ' ';
            }
        }
    }
}
















