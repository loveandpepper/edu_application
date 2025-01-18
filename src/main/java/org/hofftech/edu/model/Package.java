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

    public List<String> getReversedShape() {
        List<String> reversedShape = new ArrayList<>(this.shape);
        Collections.reverse(reversedShape);
        return reversedShape;
    }


    @Override
    public int compareTo(Package other) {
        int heightDiff = Integer.compare(other.getHeight(), this.getHeight());
        if (heightDiff == 0) {
            return Integer.compare(other.getWidth(), this.getWidth());
        }
        return heightDiff;
    }
}
