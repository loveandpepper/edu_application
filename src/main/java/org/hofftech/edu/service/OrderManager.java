package org.hofftech.edu.service;

import org.hofftech.edu.model.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления заказами.
 */
public class OrderManager {
    private final List<Order> orders = new ArrayList<>();

    /**
     * Добавляет новый заказ в список.
     *
     * @param order заказ для добавления
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Получает список заказов пользователя по его ID и диапазону дат.
     *
     * @param userId идентификатор пользователя
     * @param from   начальная дата (включительно)
     * @param to     конечная дата (включительно)
     * @return список заказов
     */
    public List<Order> getOrdersByUserIdAndDateRange(String userId, LocalDate from, LocalDate to) {
        return orders.stream()
                .filter(order -> order.getUserId().equals(userId)
                        && (order.getDate().isEqual(from) || order.getDate().isAfter(from))
                        && (order.getDate().isEqual(to) || order.getDate().isBefore(to)))
                .toList();
    }
}
