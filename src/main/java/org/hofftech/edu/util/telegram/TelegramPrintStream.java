package org.hofftech.edu.util.telegram;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Поток вывода, отправляющий сообщения в Telegram.
 * Может использоваться для переопределения системного вывода.
 */
@Slf4j
@Setter
public final class TelegramPrintStream extends PrintStream {
    private AbsSender bot;

    @Setter
    private String chatId;
    private final ExecutorService executorService;

    /**
     * Конструктор TelegramPrintStream.
     *
     * @param out базовый поток вывода
     * @param bot объект Telegram-бота
     */
    public TelegramPrintStream(OutputStream out, AbsSender bot) {
        super(out);
        this.bot = bot;
        this.executorService = Executors.newSingleThreadExecutor();
        this.chatId = null;
    }

    /**
     * Печатает сообщение и отправляет его в Telegram.
     *
     * @param message текст сообщения
     */
    @Override
    public void println(String message) {
        super.println(message);
        if (this.chatId != null) {
            sendToTelegramAsync(message);
        }
    }

    private void sendToTelegramAsync(String message) {
        if (bot == null) {
            log.error("Бот не установлен. Сообщение не отправлено: {}", message);
            return;
        }

        executorService.submit(() -> {
            try {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("```\n" + message + "\n```");
                sendMessage.setParseMode("MarkdownV2");
                bot.execute(sendMessage);
            } catch (Exception e) {
                log.error("Ошибка при отправке сообщения в Telegram: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Завершает работу потоков, используемых для отправки сообщений.
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
