package org.hofftech.edu.billing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление типов операций для заказа.
 */
@Getter
@AllArgsConstructor
public enum OrderOperationType {

    LOAD("Погрузка"),
    UNLOAD("Разгрузка");

    private final String description;
}
