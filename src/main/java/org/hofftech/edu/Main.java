package org.hofftech.edu;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.config.ApplicationContext;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;


@Slf4j
public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        log.info("Приложение запускается...");
        ApplicationContext context = new ApplicationContext();
        context.getConsoleController().listen();

        log.info("Приложение завершило работу.");
    }
}


