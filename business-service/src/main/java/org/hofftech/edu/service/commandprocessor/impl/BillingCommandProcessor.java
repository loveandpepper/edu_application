package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.ValidateException;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.ReportService;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

@RequiredArgsConstructor
public class BillingCommandProcessor implements CommandProcessor {

    private final ReportService reportService;

    @Override
    public String execute(ParsedCommand command) {
        String user = command.getUser();
        String dateFrom = command.getFrom();
        String dateTo = command.getTo();

        if (!isArgumentsNotEmpty(user, dateFrom, dateTo)) {
            throw new ValidateException("Пользователь и диапазон дат должны быть указаны в BILLING");
        }
        return reportService.generateReport(user, dateFrom, dateTo);

    }

    private boolean isArgumentsNotEmpty(String user, String dateFrom, String dateTo) {
        return user != null &&
                dateFrom != null &&
                dateTo != null &&
                !user.isEmpty() &&
                !dateFrom.isEmpty() &&
                !dateTo.isEmpty();
    }
}
