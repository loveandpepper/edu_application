package org.hofftech.edu.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.exception.DeleteException;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DeleteCommandHandler implements CommandHandler {

    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "delete";
    }

    @Override
    public String handle(String input) {
        // "delete someName"
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            throw new DeleteException("Не указано имя посылки для delete");
        }
        String name = parts[1].trim();

        String url = baseUrl + "/" + name;
        restTemplate.delete(url);
        return "Посылка '" + name + "' удалена";
    }
}

