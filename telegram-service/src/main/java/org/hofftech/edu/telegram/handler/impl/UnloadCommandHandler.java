package org.hofftech.edu.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.dto.UnloadCommandDto;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UnloadCommandHandler implements CommandHandler {

    private final CommandParser commandParser;
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "unload";
    }

    @Override
    public String handle(String input) {
        UnloadCommandDto dto = commandParser.parseToUnloadDto(input);

        String url = baseUrl + "/unload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UnloadCommandDto> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }
}

