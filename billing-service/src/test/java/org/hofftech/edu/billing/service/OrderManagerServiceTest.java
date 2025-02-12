package org.hofftech.edu.billing.service;

import org.hofftech.edu.billing.exception.BillingException;
import org.hofftech.edu.billing.model.Order;
import org.hofftech.edu.billing.model.OrderOperationType;
import org.hofftech.edu.billing.model.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class OrderManagerServiceTest {

    private OrderManagerService orderManagerService;

    @BeforeEach
    void setUp() {
        orderManagerService = new OrderManagerService();
        ReflectionTestUtils.setField(orderManagerService, "loadCost", new BigDecimal("80.00"));
        ReflectionTestUtils.setField(orderManagerService, "unloadCost", new BigDecimal("50.00"));
    }

    @Test
    void shouldAddOrderSuccessfully() {
        Order order = new Order("user1", LocalDate.now(), OrderOperationType.LOAD, 2, List.of());

        orderManagerService.addOrder(order);

        String report = orderManagerService.generateReport(
                "user1",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        );

        assertThat(report).isNotBlank().contains("Погрузка");
    }

    @Test
    void shouldThrowExceptionForInvalidDateFormat() {
        assertThatThrownBy(() -> orderManagerService.generateReport("user1", "01/01/2023", "02/02/2023"))
                .isInstanceOf(BillingException.class)
                .hasMessageStartingWith("Некорректный формат даты");
    }

    @Test
    void shouldThrowExceptionIfNoOrdersFound() {
        assertThatThrownBy(() -> orderManagerService.generateReport("user1", "01-01-2023", "02-01-2023"))
                .isInstanceOf(BillingException.class)
                .hasMessageContaining("Не найдено заказов для пользователя");
    }

    @Test
    void shouldGenerateReportSuccessfully() {
        Order order1 = new Order("user1", LocalDate.of(2023, 1, 1),
                OrderOperationType.LOAD, 3,
                List.of(new Package("Посылка Тип 1", new String[]{"1"}, '1', 0, 0),
                        new Package("Посылка Тип 2", new String[]{"22"}, '2', 0, 0)));

        Order order2 = new Order("user1", LocalDate.of(2023, 1, 1),
                OrderOperationType.UNLOAD, 3, List.of(new Package("Посылка Тип 1", new String[]{"1"},
                        '1', 0, 0),
                new Package("Посылка Тип 3", new String[]{"333"}, '3', 0, 0)));

        orderManagerService.addOrder(order1);
        orderManagerService.addOrder(order2);

        String report = orderManagerService.generateReport("user1", "01-01-2023", "02-01-2023");

        assertThat(report).isNotBlank().contains("Погрузка").contains("Разгрузка");
    }
}

