package org.hofftech.edu.telegram.handler.impl;


import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.exception.FindException;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FindCommandHandler implements CommandHandler {

    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "find";
    }

    @Override
    public String handle(String input) {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            throw new FindException("Не указано имя посылки для find");
        }
        String name = parts[1].trim();

        String url = baseUrl + "/" + name;
        return restTemplate.getForObject(url, String.class);
    }
}

