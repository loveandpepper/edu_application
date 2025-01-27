package org.hofftech.edu.telegram.controller;


import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.telegram.dto.BillingCommandDto;
import org.hofftech.edu.telegram.dto.CreateCommandDto;
import org.hofftech.edu.telegram.dto.LoadCommandDto;
import org.hofftech.edu.telegram.dto.UnloadCommandDto;
import org.hofftech.edu.telegram.dto.UpdateCommandDto;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * TelegramBotController для взаимодействия с API основного сервиса.
 */
@Slf4j
public class TelegramBotController extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;
    private static final String BASE_URL = "http://localhost:8080/api/packages";
    private static final String PARSE_MODE = "MarkdownV2";
    private final CommandParser commandParser;
    private final RestTemplate restTemplate;

    public TelegramBotController(String botToken, String botName, CommandParser commandParser, RestTemplate restTemplate) {
        super("");
        this.botToken = botToken;
        this.botName = botName;
        this.commandParser = commandParser;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken; // Возвращаем токен для библиотеки
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
            throw new RuntimeException("Ошибка регистрации Telegram-бота", e);
        }
    }

    private void handleCommand(String command, Long chatId) {
        if (command.startsWith("create")) {
            CreateCommandDto dto = commandParser.parseToCreateDto(command);
            postRequest("/create", dto, chatId);
        } else if (command.startsWith("update")) {
            UpdateCommandDto dto = commandParser.parseToUpdateDto(command);
            postRequest("/update", dto, chatId);
        } else if (command.startsWith("load")) {
            LoadCommandDto dto = commandParser.parseToLoadDto(command);
            postRequest("/load", dto, chatId);
        } else if (command.startsWith("unload")) {
            UnloadCommandDto dto = commandParser.parseToUnloadDto(command);
            postRequest("/unload", dto, chatId);
        } else if (command.startsWith("billing")) {
            BillingCommandDto dto = commandParser.parseToBillingDto(command);
            postRequest("/billing", dto, chatId);
        } else if (command.startsWith("find")) {
            String name = command.split(" ", 2)[1];
            getRequest("/find/" + name, chatId);
        } else if (command.startsWith("delete")) {
            String name = command.split(" ", 2)[1];
            deleteRequest("/" + name, chatId);
        } else if (command.startsWith("list")) {
            getRequest("/list", chatId);
        } else {
            sendErrorMessage(chatId, "Неизвестная команда.");
        }
    }

    private void postRequest(String endpoint, Object dto, Long chatId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(dto, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + endpoint, entity, String.class);
            sendResponse(chatId, response.getBody());
        } catch (Exception e) {
            log.error("Ошибка POST запроса: {}", e.getMessage());
            sendErrorMessage(chatId, "Ошибка выполнения POST запроса.");
        }
    }

    private void getRequest(String endpoint, Long chatId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + endpoint, String.class);
            sendResponse(chatId, response.getBody());
        } catch (Exception e) {
            log.error("Ошибка GET запроса: {}", e.getMessage());
            sendErrorMessage(chatId, "Ошибка выполнения GET запроса.");
        }
    }

    private void deleteRequest(String endpoint, Long chatId) {
        try {
            restTemplate.delete(BASE_URL + endpoint);
            sendResponse(chatId, "Успешно удалено.");
        } catch (Exception e) {
            log.error("Ошибка DELETE запроса: {}", e.getMessage());
            sendErrorMessage(chatId, "Ошибка выполнения DELETE запроса.");
        }
    }

    private void sendResponse(Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText("```\n" + message + "\n```");
            sendMessage.setParseMode(PARSE_MODE);
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
            sendMessage.setParseMode(PARSE_MODE);
            execute(sendMessage);
        } catch (Exception e) {
            log.error("Ошибка отправки ошибки в Telegram: {}", e.getMessage());
        }
    }
}

