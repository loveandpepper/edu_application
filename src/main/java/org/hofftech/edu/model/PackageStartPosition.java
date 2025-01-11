package org.hofftech.edu.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, представляющий начальную позицию упаковки.
 * Используется для задания позиции в процессе упаковки.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PackageStartPosition {
    private final int x;
    private final int y;
}
