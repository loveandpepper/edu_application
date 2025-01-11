package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.DefaultCommandHandler;

import java.util.Scanner;

/**
  Контроллер консоли, обрабатывает ввод пользователя и направляет команды в обработчики.
 */
@Slf4j
@RequiredArgsConstructor
public class ConsoleController {
    /**
     * Запускает цикл обработки пользовательских команд.
     */
    private final DefaultCommandHandler defaultCommandHandler;

    public void listen() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Ожидание команды пользователя...");
            System.out.print("Введите команду: ");

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                defaultCommandHandler.handle(command);
            }
        } catch (Exception e) {
            log.error("Ошибка во время работы консольного контроллера: {}", e.getMessage(), e);
        }
    }

}
