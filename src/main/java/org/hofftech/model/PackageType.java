package org.hofftech.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PackageType {
    ONE(new String[]{"1"}),
    TWO(new String[]{"22"}),
    THREE(new String[]{"333"}),
    FOUR(new String[]{"4444"}),
    FIVE(new String[]{"55555"}),
    SIX(new String[]{"666", "666"}),
    SEVEN(new String[]{"777", "7777"}),
    EIGHT(new String[]{"8888", "8888"}),
    NINE(new String[]{"999", "999", "999"});

    private final String[] shape;

    PackageType(String[] shape) {
        this.shape = shape;
    }

    public int getWidth() {
        return shape[0].length();
    }

    public int getHeight() {
        return shape.length;
    }

    public static PackageType fromShape(String[] shape) {
        for (PackageType type : values()) {
            if (Arrays.equals(type.getShape(), shape)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Нет соответствующего PackageType для формы: " + Arrays.toString(shape));
    }
}