package org.hofftech.edu.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Модель заказа.
 */
@Data
@AllArgsConstructor
public class Order {

    private String userId;
    private LocalDate date;
    private OrderOperationType operationType;
    private int truckCount;
    private List<Package> packages;
}
