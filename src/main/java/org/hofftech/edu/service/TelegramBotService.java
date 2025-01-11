package org.hofftech.edu.service;

import org.hofftech.edu.handler.CommandHandler;
import org.hofftech.edu.util.telegram.TelegramAppender;
import org.hofftech.edu.util.telegram.TelegramPrintStream;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Сервис для взаимодействия с Telegram-ботом.
 * Отвечает за получение сообщений от пользователей и обработку команд.
 */
public class TelegramBotService extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final TelegramAppender telegramAppender;
    private final TelegramPrintStream printStream;

    public static final String TOKEN = "7650285239:AAErFmH9TqHJ_0UbpPd0MzQDBL0DjK6CxEY";
    public static final String BOT_NAME = "java_edu_project_bot.";

    /**
     * Конструктор TelegramBotService.
     *
     * @param botToken         токен для авторизации бота
     * @param commandHandler   обработчик команд
     * @param telegramAppender аппендер для логов в Telegram
     * @param printStream      поток для вывода сообщений в Telegram
     */
    public TelegramBotService(String botToken, CommandHandler commandHandler,
                              TelegramAppender telegramAppender, TelegramPrintStream printStream) {
        super(botToken);
        this.commandHandler = commandHandler;
        this.telegramAppender = telegramAppender;
        this.printStream = printStream;
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
            telegramAppender.setChatId(chatId.toString());
            printStream.setChatId(chatId.toString());
            commandHandler.handle(message);
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
}
