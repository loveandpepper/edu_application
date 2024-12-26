package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.CommandHandler;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class ConsoleController {
    private final CommandHandler commandHandler;

    public void listen() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("Ожидание команды пользователя...");
            System.out.print("Введите import easyalgorithm [путь_к_файлу] или import [путь_к_файлу] или " +
            "save [путь_к_файлу] или importjson [путь_к_файлу] или import even [кол-во грузовиков] [путь_к_файлу]" +
                    " для выхода используйте exit: ");

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                commandHandler.handle(command);
            }
        } catch (Exception e) {
            log.error("Ошибка во время работы консольного контроллера: {}", e.getMessage(), e);
        }
    }

}
