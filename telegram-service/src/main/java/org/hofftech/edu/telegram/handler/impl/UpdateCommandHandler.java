package org.hofftech.edu.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.dto.UpdateCommandDto;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UpdateCommandHandler implements CommandHandler {

    private final CommandParser commandParser;
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "update";
    }

    @Override
    public String handle(String input) {
        UpdateCommandDto dto = commandParser.parseToUpdateDto(input);

        String url = baseUrl + "/" + dto.getName();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateCommandDto> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        return response.getBody();
    }
}
