package org.hofftech.edu.billing.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.billing.exception.BillingException;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.OrderOperationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderManagerService {

    private final ConcurrentHashMap<String, List<Order>> orders = new ConcurrentHashMap<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Value("${order.load-cost}")
    private BigDecimal loadCost;

    @Value("${order.unload-cost}")
    private BigDecimal unloadCost;

    /**
     * Добавляет заказ в список, учитывая многопоточные операции.
     */
    public void addOrder(Order order) {
        orders.computeIfAbsent(order.getUserId(), k -> new CopyOnWriteArrayList<>()).add(order);
    }

    /**
     * Вычисляет общую стоимость заказа в зависимости от типа операции.
     */
    private BigDecimal getTotalCost(Order order) {
        if (order.getPackages() == null || order.getPackages().isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal costPerSegment = (order.getOperationType() == OrderOperationType.LOAD)
                ? loadCost
                : unloadCost;

        return order.getPackages().stream()
                .map(pkg -> BigDecimal.valueOf(countSegments(pkg.getShape()))
                        .multiply(costPerSegment))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int countSegments(String[] shape) {
        if (shape == null) {
            return 0;
        }
        return (int) java.util.Arrays.stream(shape)
                .flatMapToInt(CharSequence::chars)
                .filter(ch -> ch != ' ')
                .count();
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

    private String buildReport(String userId, String from, String to) {
        LocalDate fromDate;
        LocalDate toDate;
        try {
            fromDate = LocalDate.parse(from, FORMATTER);
            toDate = LocalDate.parse(to, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BillingException("Некорректный формат даты (ожидался dd-MM-yyyy): " + e.getMessage());
        }

        List<Order> filtered = getOrdersByUserAndDateRange(userId, fromDate, toDate);

        if (filtered.isEmpty()) {
            throw new BillingException("Не найдено заказов для пользователя " + userId +
                    " в диапазоне дат " + fromDate + " - " + toDate);
        }

        return filtered.stream()
                .map(order -> String.format(
                        "%s; %s; %d машин; %d посылок; %s рублей",
                        order.getDate().format(FORMATTER),
                        order.getOperationType().getDescription(),
                        order.getTruckCount(),
                        order.getPackages().size(),
                        getTotalCost(order)
                                .setScale(2, RoundingMode.HALF_UP)
                                .toPlainString()
                ))
                .collect(Collectors.joining("\n"));
    }

    private List<Order> getOrdersByUserAndDateRange(String userId, LocalDate fromDate, LocalDate toDate) {
        return orders.getOrDefault(userId, List.of()).stream()
                .filter(order ->
                        !order.getDate().isBefore(fromDate) &&
                                !order.getDate().isAfter(toDate))
                .toList();
    }

    public boolean isRangeWithinLastMonth(String from, String to) {
        try {
            LocalDate fromDate = LocalDate.parse(from, FORMATTER);
            LocalDate toDate = LocalDate.parse(to, FORMATTER);

            LocalDate monthAgo = LocalDate.now().minusMonths(1);
            LocalDate today = LocalDate.now();

            return (!fromDate.isBefore(monthAgo)) && (!toDate.isAfter(today));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
