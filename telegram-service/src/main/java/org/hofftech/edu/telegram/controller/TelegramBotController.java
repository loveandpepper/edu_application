package org.hofftech.edu.telegram.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.telegram.dto.BillingCommandDto;
import org.hofftech.edu.telegram.dto.CreateCommandDto;
import org.hofftech.edu.telegram.dto.LoadCommandDto;
import org.hofftech.edu.telegram.dto.UnloadCommandDto;
import org.hofftech.edu.telegram.dto.UpdateCommandDto;
import org.hofftech.edu.telegram.exception.RegisterBotException;
import org.hofftech.edu.telegram.service.CommandParser;
import org.hofftech.edu.telegram.service.TelegramHandlerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * TelegramBotController для взаимодействия с API основного сервиса.
 */
@Slf4j
@RequiredArgsConstructor
public class TelegramBotController extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;
    private final TelegramHandlerFactory telegramHandlerFactory;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            try {
                handleCommand(message, chatId);
            } catch (Exception e) {
                log.error("Ошибка обработки команды: {}", e.getMessage());
                sendErrorMessage(chatId, "Ошибка выполнения команды.");
            }
        }
    }

    /**
     * Регистрация телеграм бота.
     */
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (Exception e) {
            throw new RegisterBotException(e.getMessage());
        }
    }

    private void handleCommand(String command, Long chatId) {
        String[] parts = command.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();

        telegramHandlerFactory.getHandler(commandName).ifPresentOrElse(
                handler -> {
                    try {
                        String result = handler.handle(command);
                        sendResponse(chatId, result);
                    } catch (Exception e) {
                        log.error("Ошибка в хендлере '{}': {}", commandName, e.getMessage(), e);
                        sendErrorMessage(chatId, "Ошибка при выполнении " + commandName + ": " + e.getMessage());
                    }
                },
                () -> sendErrorMessage(chatId, "Неизвестная команда: " + commandName)
        );
    }

    private void sendResponse(Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText("```\n" + message + "\n```");
            sendMessage.setParseMode(ParseMode.MARKDOWNV2);
            execute(sendMessage);
        } catch (Exception e) {
            log.error("Ошибка отправки ответа в Telegram: {}", e.getMessage());
        }
    }

    private void sendErrorMessage(Long chatId, String errorMessage) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText(errorMessage);
            sendMessage.setParseMode(ParseMode.MARKDOWNV2);
            execute(sendMessage);
        } catch (Exception e) {
            log.error("Ошибка отправки ошибки в Telegram: {}", e.getMessage());
        }
    }
}

