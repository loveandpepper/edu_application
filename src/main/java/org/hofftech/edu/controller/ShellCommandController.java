package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.CommandHandler;
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

    private final CommandHandler commandHandler;

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
        return commandHandler.handle("find " + args);
    }

    @ShellMethod("Создание новой посылки")
    public String create(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("create " + args);
    }

    @ShellMethod("Обновление существующей посылки")
    public String update(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("update " + args);
    }

    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("delete " + args);
    }

    @ShellMethod("Список всех посылок")
    public String list() {
        return commandHandler.handle("list");
    }

    @ShellMethod("Погрузка")
    public String load(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("load " + args);
    }

    @ShellMethod("Разгрузка")
    public String unload(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("unload " + args);
    }

    @ShellMethod("Генерация отчета биллинга")
    public String billing(@ShellOption(defaultValue = "") String args) {
        return commandHandler.handle("billing " + args);
    }

}
