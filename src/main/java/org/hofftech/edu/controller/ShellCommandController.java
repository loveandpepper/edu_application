package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.ConsoleCommandHandler;
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

    private final ConsoleCommandHandler consoleCommandHandler;

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
        return consoleCommandHandler.handle("find " + args);
    }

    @ShellMethod("Создание новой посылки")
    public String create(@ShellOption(defaultValue = "") String args) {
        return consoleCommandHandler.handle("create " + args);
    }

    @ShellMethod("Обновление существующей посылки")
    public String update(@ShellOption(defaultValue = "") String args) {
        return consoleCommandHandler.handle("update " + args);
    }

    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption(defaultValue = "") String args) {
        return consoleCommandHandler.handle("delete " + args);
    }

    @ShellMethod("Список всех посылок")
    public String list() {
        return consoleCommandHandler.handle("list");
    }

    @ShellMethod("Погрузка")
    public String load(@ShellOption(defaultValue = "") String args) {

        return consoleCommandHandler.handle("load " + args);
    }

    @ShellMethod("Разгрузка")
    public String unload(@ShellOption(defaultValue = "") String args) {
        return consoleCommandHandler.handle("unload " + args);
    }

    @ShellMethod("Генерация отчета биллинга")
    public String billing(@ShellOption(defaultValue = "") String args) {
        return consoleCommandHandler.handle("billing " + args);
    }

}
