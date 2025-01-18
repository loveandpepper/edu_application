package org.hofftech.edu.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Сервис для взаимодействия с Telegram-ботом.
 * Отвечает за получение сообщений от пользователей и обработку команд.
 */
@Slf4j
@Getter
public class TelegramBotController extends TelegramLongPollingBot {
    public static final String TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    public static final String BOT_NAME = System.getenv("TELEGRAM_BOT_NAME");
    private static final String PARSE_MODE = "MarkdownV2";
    private final CommandHandler commandHandler;

    /**
     * Конструктор TelegramBotService.
     *
     * @param botToken         токен для авторизации бота
     * @param commandHandler   обработчик команд
     */
    public TelegramBotController(String botToken, CommandHandler commandHandler) {
        super(botToken);
        this.commandHandler = commandHandler;
    }

    /**
     * Метод для обработки входящих сообщений от пользователей.
     *
     * @param update объект Update, содержащий информацию о входящем сообщении
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            try {
                String response = commandHandler.handle(message);
                String markdownResponse = "```\n" + response + "\n```";
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId.toString());
                sendMessage.setText(markdownResponse);
                sendMessage.setParseMode(PARSE_MODE);
                execute(sendMessage);
            } catch (Exception e) {
                log.error("Ошибка обработки команды из Telegram: {}", e.getMessage());
            }
        }
    }

    /**
     * Возвращает имя бота.
     *
     * @return строка с именем бота
     */
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Регистрация телеграм бота
     */
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка регистрации Telegram-бота", e);
        }
    }
}
