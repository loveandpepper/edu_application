package org.hofftech.controller;

import org.hofftech.handler.ImportCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class ConsoleController {
    private static final Logger log = LoggerFactory.getLogger(ConsoleController.class);

    private final ImportCommandHandler importCommandHandler;

    public ConsoleController(ImportCommandHandler importCommandHandler) {
        this.importCommandHandler = importCommandHandler;
    }

    public void listen() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Ошибка установки кодировки для консоли", e);
        }
        Scanner scanner = new Scanner(System.in);
        log.info("Ожидание команды пользователя...");

        while (true) {
            System.out.print("Введите import easyalgorithm [путь_к_файлу] или import [путь_к_файлу], " +
                    "для выхода используйте exit: ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                log.info("Приложение завершает работу по команде пользователя.");
                break;
            }

            if (command.startsWith("import ")) {
                boolean useEasyAlgorithm = command.contains("easyalgorithm");
                String filePath = command.replace("easyalgorithm", "").replace("import", "").trim();
                importCommandHandler.handle(filePath, useEasyAlgorithm);
            } else {
                log.warn("Неизвестная команда: {}", command);
                System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }

        scanner.close();
    }
}
