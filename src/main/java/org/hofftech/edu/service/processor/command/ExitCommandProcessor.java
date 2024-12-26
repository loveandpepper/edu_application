package org.hofftech.edu.service.processor.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExitCommandProcessor implements CommandProcessor {

    @Override
    public void execute(String command) {
        log.info("Приложение завершает работу по команде пользователя.");
        System.exit(0);
    }
}
