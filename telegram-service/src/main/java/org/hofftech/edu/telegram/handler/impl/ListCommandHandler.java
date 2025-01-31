package org.hofftech.edu.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ListCommandHandler implements CommandHandler {

    private static final int PAGINATION_PAGE_DEFAULT = 0;
    private static final int PAGINATION_SIZE_DEFAULT = 100;
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "list";
    }

    @Override
    public String handle(String input) {
        String url = String.format("%s?page=%d&size=%d", baseUrl, PAGINATION_PAGE_DEFAULT, PAGINATION_SIZE_DEFAULT);
        return restTemplate.getForObject(url, String.class);
    }
}

