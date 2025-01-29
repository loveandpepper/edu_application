package org.hofftech.edu.console.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.console.service.ShellCommandService;
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

    private final ShellCommandService shellCommandService;

    @ShellMethod("Создать новую посылку")
    public String create(@ShellOption(help = "Строка параметров команды") String command) {
        return shellCommandService.create(command);
    }

    @ShellMethod("Обновить существующую посылку")
    public String update(@ShellOption(help = "Строка параметров команды") String command) {
        return shellCommandService.update(command);
    }

    @ShellMethod("Погрузить посылки в грузовики")
    public String load(@ShellOption(help = "Строка параметров команды") String command) {
        return shellCommandService.load(command);
    }

    @ShellMethod("Выгрузить посылки из грузовиков")
    public String unload(@ShellOption(help = "Строка параметров команды") String command) {
        return shellCommandService.unload(command);
    }

    @ShellMethod("Рассчитать стоимость")
    public String billing(@ShellOption(help = "Строка параметров команды") String command) {
        return shellCommandService.billing(command);
    }

    @ShellMethod("Поиск посылки")
    public String find(@ShellOption String name) {
        return shellCommandService.find(name);
    }

    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption String name) {
        return shellCommandService.delete(name);
    }

    @ShellMethod("Получение списка посылок")
    public String list(@ShellOption(defaultValue = "0") int page,
                       @ShellOption(defaultValue = "10") int size) {
        return shellCommandService.list(page, size);
    }

    @ShellMethod("Закрытие")
    public String exit() {
        System.exit(0);
        return "Приложение закрыто";
    }
}


