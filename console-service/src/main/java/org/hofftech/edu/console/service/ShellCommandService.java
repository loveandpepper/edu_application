package org.hofftech.edu.console.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.console.exception.BillingException;
import org.hofftech.edu.console.exception.CreateException;
import org.hofftech.edu.console.exception.DeleteException;
import org.hofftech.edu.console.exception.FindAllException;
import org.hofftech.edu.console.exception.FindException;
import org.hofftech.edu.console.exception.LoadException;
import org.hofftech.edu.console.exception.UnloadException;
import org.hofftech.edu.console.exception.UpdateException;
import org.hofftech.edu.console.model.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Slf4j
@RequiredArgsConstructor
public class ShellCommandService {

    private final RestTemplate restTemplate;
    private final CommandParser commandParser;

    /**
     * Базовый URL
     */
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Создание новой посылки: POST /api/packages
     */
    public String create(String command) {
        try {
            CreateCommandDto dto = commandParser.parseToCreateDto(command);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(baseUrl, entity, String.class);
        } catch (RestClientException e) {
            throw new CreateException("Ошибка при выполнении команды CREATE: " + e.getMessage());
        }
    }

    /**
     * Обновление посылки по имени: PUT /api/packages/{name}
     */
    public String update(String command) {
        try {
            UpdateCommandDto dto = commandParser.parseToUpdateDto(command);
            String url = baseUrl + "/" + dto.getName().trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateCommandDto> entity = new HttpEntity<>(dto, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new UpdateException("Ошибка при выполнении команды UPDATE: " + e.getMessage());
        }
    }

    /**
     * Погрузка: POST /api/packages/load
     */
    public String load(String command) {
        try {
            LoadCommandDto dto = commandParser.parseToLoadDto(command);
            String url = baseUrl + "/load";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoadCommandDto> entity = new HttpEntity<>(dto, headers);
            return restTemplate.postForObject(url, entity, String.class);
        } catch (RestClientException e) {
            throw new LoadException("Ошибка при выполнении команды LOAD: " + e.getMessage());
        }
    }

    /**
     * Разгрузка: POST /api/packages/unload
     */
    public String unload(String command) {
        try {
            UnloadCommandDto dto = commandParser.parseToUnloadDto(command);
            String url = baseUrl + "/unload";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UnloadCommandDto> entity = new HttpEntity<>(dto, headers);
            return restTemplate.postForObject(url, entity, String.class);
        } catch (RestClientException e) {
            throw new UnloadException("Ошибка при выполнении команды UNLOAD: " + e.getMessage());
        }
    }

    /**
     * Расчёт стоимости: POST /api/packages/billing
     */
    public String billing(String command) {
        try {
            BillingCommandDto dto = commandParser.parseToBillingDto(command);
            String url = baseUrl + "/billing";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BillingCommandDto> entity = new HttpEntity<>(dto, headers);
            return restTemplate.postForObject(url, entity, String.class);
        } catch (RestClientException e) {
            throw new BillingException("Ошибка при выполнении команды BILLING: " + e.getMessage());
        }
    }

    /**
     * Найти посылку: GET /api/packages/{name}
     */
    public String find(String name) {
        try {
            String url = baseUrl + "/" + name.trim();
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new FindException("Ошибка при поиске посылки: " + e.getMessage());
        }
    }

    /**
     * Удалить посылку: DELETE /api/packages/{name}
     */
    public String delete(String name) {
        try {
            String url = baseUrl + "/" + name.trim();
            restTemplate.delete(url);
            return "Посылка успешно удалена.";
        } catch (RestClientException e) {
            throw new DeleteException("Ошибка при удалении посылки: " + e.getMessage());
        }
    }

    /**
     * Список посылок: GET /api/packages?page=...&size=...
     */
    public String list(int page, int size) {
        try {
            String url = String.format("%s?page=%d&size=%d", baseUrl, page, size);
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new FindAllException("Ошибка при получении списка посылок: " + e.getMessage());
        }
    }
}
