package org.hofftech.edu.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Модель заказа.
 */
@Data
@AllArgsConstructor
public class Order {

    private String userId;
    private LocalDate date;
    private OperationType operationType;
    private int truckCount;
    private List<Package> packages;

    /**
     * Вычисляет общую стоимость заказа в зависимости от типа операции.
     * Погрузка (LOAD): 80 рублей за сегмент каждой посылки.
     * Разгрузка (UNLOAD): 50 рублей за сегмент каждой посылки.
     *
     * @return общая стоимость заказа
     */
    public int getTotalCost() {
        if (packages == null || packages.isEmpty()) {
            return 0;
        }

        int costPerSegment = operationType == OperationType.LOAD ? 80 : 50;

        return packages.stream()
                .mapToInt(pkg -> pkg.getHeight() * costPerSegment) // TODO: сделать метод вычисления
                .sum();
    }

    /**
     * Перечисление типов операций.
     */
    public enum OperationType {
        LOAD,
        UNLOAD
    }
}
