package org.hofftech.edu.billing.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.billing.exception.BillingException;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.OrderOperationType;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderManagerService {

    private final List<Order> orders = new ArrayList<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Добавляет заказ в список
     */
    public void addOrder(Order order) {
        orders.add(order);
    }

    /**
     * Генерирует отчёт, кэшируя его ТОЛЬКО если диапазон дат (from..to) попадает
     * в "последний месяц" (определяется методом isRangeWithinLastMonth).
     */
    @Cacheable(
            value = "reportCache",
            key = "{ #userId, #from, #to }",
            condition = "#root.target.isRangeWithinLastMonth(#from, #to)"
    )
    public String generateReport(String userId, String from, String to) {
        log.info("Вызвана генерация отчёта без кэша...");
        return buildReport(userId, from, to);
    }

    /**
     * "Чистая" логика генерации отчёта:
     * - парсинг дат
     * - фильтрация заказов
     * - форматирование строки
     */
    private String buildReport(String userId, String from, String to) {
        LocalDate fromDate;
        LocalDate toDate;
        try {
            fromDate = LocalDate.parse(from, FORMATTER);
            toDate   = LocalDate.parse(to, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BillingException("Некорректный формат даты (ожидался dd-MM-yyyy)." + e.getMessage());
        }

        List<Order> filtered = getOrdersByUserAndDateRange(userId, fromDate, toDate);

        if (filtered.isEmpty()) {
            throw new BillingException("Не найдено заказов для пользователя " + userId +
                    " в диапазоне дат " + fromDate + " - " + toDate);
        }

        return filtered.stream()
                .map(order -> String.format(
                        "%s; %s; %d машин; %d посылок; %d рублей",
                        order.getDate().format(FORMATTER),
                        (order.getOperationType() == OrderOperationType.LOAD) ? "Погрузка" : "Разгрузка",
                        order.getTruckCount(),
                        order.getPackages().size(),
                        order.getTotalCost()
                ))
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("");
    }

    private List<Order> getOrdersByUserAndDateRange(String userId, LocalDate fromDate, LocalDate toDate) {
        return orders.stream()
                .filter(order ->
                        order.getUserId().equals(userId)
                                && ( !order.getDate().isBefore(fromDate) )
                                && ( !order.getDate().isAfter(toDate)  )
                )
                .toList();
    }

    /**
     * Проверяет, что запрошенный период (from..to) полностью лежит
     * в интервале в месяц от текущего момента.
     */
    public boolean isRangeWithinLastMonth(String from, String to) {
        try {
            LocalDate fromDate = LocalDate.parse(from, FORMATTER);
            LocalDate toDate   = LocalDate.parse(to, FORMATTER);

            LocalDate monthAgo = LocalDate.now().minusMonths(1);
            LocalDate today    = LocalDate.now();

            return (!fromDate.isBefore(monthAgo)) && (!toDate.isAfter(today));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
