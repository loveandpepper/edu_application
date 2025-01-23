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

    private static final int LOAD_COST = 80;
    private static final int UNLOAD_COST = 50;
    private String userId;
    private LocalDate date;
    private OrderOperationType operationType;
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

        int costPerSegment = operationType == OrderOperationType.LOAD ? LOAD_COST : UNLOAD_COST;

        return packages.stream()
                .mapToInt(pkg -> countSegments(pkg.getShape()) * costPerSegment)
                .sum();
    }

    private int countSegments(List<String> shape) {
        return shape.stream()
                .mapToInt(row -> (int) row.chars().filter(ch -> ch != ' ').count())
                .sum();
    }
}
