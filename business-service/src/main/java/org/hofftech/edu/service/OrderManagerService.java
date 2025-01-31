package org.hofftech.edu.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.exception.BillingException;
import org.hofftech.edu.model.Order;
import org.hofftech.edu.model.OrderOperationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления заказами.
 */
@Slf4j
public class OrderManagerService {
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
    public String generateReport(String userId, String from, String to) {
        LocalDate fromDate;
        LocalDate toDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        log.info("Генерируем отчет для {} с {} по {}", userId, from, to);
        try {
            fromDate = LocalDate.parse(from, formatter);
            toDate = LocalDate.parse(to, formatter);
        } catch (DateTimeParseException e) {
            throw new BillingException("Некорректный формат даты. Используйте формат: dd-MM-yyyy.");
        }

        List<Order> collectedOrders = getOrdersByUserIdAndDateRange(userId, fromDate, toDate);

        if (collectedOrders.isEmpty()) {
            throw new BillingException("Не найдено заказов для пользователя " + userId +
                    " в диапазоне " + fromDate + " - " + toDate);
        }

        return collectedOrders.stream()
                .map(order -> String.format(
                        "%s; %s; %d машин; %d посылок; %d рублей",
                        order.getDate().format(formatter),
                        order.getOperationType() == OrderOperationType.LOAD ? "Погрузка" : "Разгрузка",
                        order.getTruckCount(),
                        order.getPackages().size(),
                        order.getTotalCost()
                ))
                .collect(Collectors.joining("\n"));
    }

    private List<Order> getOrdersByUserIdAndDateRange(String userId, LocalDate dateFrom, LocalDate dateTo) {
        return orders.stream()
                .filter(order -> order.getUserId().equals(userId)
                        && (order.getDate().isEqual(dateFrom) || order.getDate().isAfter(dateFrom))
                        && (order.getDate().isEqual(dateTo) || order.getDate().isBefore(dateTo)))
                .toList();
    }
}
