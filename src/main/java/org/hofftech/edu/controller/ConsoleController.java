package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.CommandHandler;

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
    private final CommandHandler commandHandler;

    public void listen() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Ожидание команды пользователя...");
            System.out.print("Введите команду: ");

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                try {
                    String result = commandHandler.handle(command);
                    if (result != null && !result.isEmpty()) {
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    log.error("Ошибка при выполнении команды: {}", e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Ошибка работы контроллера: {}", e.getMessage());
        }
    }
}
