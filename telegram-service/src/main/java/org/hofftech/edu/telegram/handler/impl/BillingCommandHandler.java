package org.hofftech.edu.telegram.handler.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.telegram.dto.BillingCommandDto;
import org.hofftech.edu.telegram.handler.CommandHandler;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BillingCommandHandler implements CommandHandler {

    private final CommandParser commandParser;
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public String getCommand() {
        return "billing";
    }

    @Override
    public String handle(String input) {
        BillingCommandDto dto = commandParser.parseToBillingDto(input);

        String url = baseUrl + "/billing";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BillingCommandDto> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }
}
