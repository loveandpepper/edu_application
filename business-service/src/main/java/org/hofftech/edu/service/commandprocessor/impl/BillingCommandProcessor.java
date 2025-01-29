package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.BillingException;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.OrderManagerService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

@RequiredArgsConstructor
public class BillingCommandProcessor implements CommandProcessor {
    private final OrderManagerService orderManagerService;

    private boolean isArgumentsNotEmpty(String user, String dateFrom, String dateTo) {
        return user != null && dateFrom != null && dateTo != null &&
                !user.isEmpty() && !dateFrom.isEmpty() && !dateTo.isEmpty();
    }

    @Override
    public String execute(ParsedCommand command) {
        String user = command.getUser();
        String dateFrom = command.getFrom();
        String dateTo = command.getTo();

        if (!isArgumentsNotEmpty(user, dateFrom, dateTo)) {
            throw new BillingException("Пользователь и диапазон дат должны быть указаны в BILLING");
        }
        return orderManagerService.generateReport(user, dateFrom, dateTo);

    }
}
