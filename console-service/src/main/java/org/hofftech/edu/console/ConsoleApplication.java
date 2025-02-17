package org.hofftech.edu.console;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootApplication
public class ConsoleApplication {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        log.info("Приложение запускается...");
        SpringApplication.run(ConsoleApplication.class, args);
        log.info("Приложение завершило работу");
    }
}
