package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Контроллер для обработки команд через Spring Shell.
 */
@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class ShellCommandController {

    private final CommandProcessorFactory processorFactory;
    private final CommandParser commandParser;

    @ShellMethod("Стартовая команда")
    public String start() {
        return "Привет! Список команд в readme.";
    }

    @ShellMethod("Выход из приложения")
    public String exit() {
        System.exit(0);
        return "Приложение завершило работу.";
    }

    @ShellMethod("Поиск посылки")
    public String find(@ShellOption(defaultValue = "") String args) {
        return handleCommand("find " + args);
    }

    @ShellMethod("Создание новой посылки")
    public String create(@ShellOption(defaultValue = "") String args) {
        return handleCommand("create " + args);
    }

    @ShellMethod("Обновление существующей посылки")
    public String update(@ShellOption(defaultValue = "") String args) {
        return handleCommand("update " + args);
    }

    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption(defaultValue = "") String args) {
        return handleCommand("delete " + args);
    }

    @ShellMethod("Список всех посылок")
    public String list() {
        return handleCommand("list");
    }

    @ShellMethod("Погрузка")
    public String load(@ShellOption(defaultValue = "") String args) {
        return handleCommand("load " + args);
    }

    @ShellMethod("Разгрузка")
    public String unload(@ShellOption(defaultValue = "") String args) {
        return handleCommand("unload " + args);
    }

    @ShellMethod("Генерация отчета биллинга")
    public String billing(@ShellOption(defaultValue = "") String args) {
        return handleCommand("billing " + args);
    }

    private String handleCommand(String command) {
        try {
            ParsedCommand parsedCommand = commandParser.parse(command);
            return processorFactory.createProcessor(parsedCommand.getCommandType())
                    .execute(parsedCommand);
        } catch (Exception e) {
            log.error("Ошибка обработки команды: {}", e.getMessage(), e);
            return "Ошибка: " + e.getMessage();
        }
    }
}
