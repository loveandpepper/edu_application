package org.hofftech.edu.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;
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

    private final String botToken;
    private final String botName;
    private static final String PARSE_MODE = "MarkdownV2";
    private final CommandProcessorFactory processorFactory;
    private final CommandParser commandParser;

    /**
     * Конструктор TelegramBotController.
     *
     * @param processorFactory фабрика процессоров команд
     * @param commandParser    парсер команд
     */
    public TelegramBotController(String botToken, String botName,
                                 CommandProcessorFactory processorFactory,
                                 CommandParser commandParser) {
        super("");
        this.botToken = botToken;
        this.botName = botName;
        this.processorFactory = processorFactory;
        this.commandParser = commandParser;
    }

    @Override
    public String getBotUsername() {
        return botName;
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
                ParsedCommand parsedCommand = commandParser.parse(message);
                String response = (String) processorFactory.createProcessor(parsedCommand.getCommandType())
                        .execute(parsedCommand);
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
     * Регистрация телеграм бота.
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
