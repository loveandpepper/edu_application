package org.hofftech.edu.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelegramHandlerFactory {

    private final Map<String, CommandHandler> handlers;

    public TelegramHandlerFactory(List<CommandHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(CommandHandler::getCommand, h -> h));

        log.info("Инициализировано {} команд: {}", handlers.size(), handlers.keySet());
    }

    public CommandHandler getHandler(String commandName) {
        return Optional.ofNullable(handlers.get(commandName))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Нет хендлера для команды: " + commandName
                ));
    }
}
