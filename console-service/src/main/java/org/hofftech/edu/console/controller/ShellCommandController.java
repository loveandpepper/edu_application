package org.hofftech.edu.console.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.console.model.dto.BillingCommandDto;
import org.hofftech.edu.console.model.dto.CreateCommandDto;
import org.hofftech.edu.console.model.dto.LoadCommandDto;
import org.hofftech.edu.console.model.dto.UnloadCommandDto;
import org.hofftech.edu.console.model.dto.UpdateCommandDto;
import org.hofftech.edu.console.service.CommandParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;

/**
 * Контроллер для обработки команд через Spring Shell.
 */
@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class ShellCommandController {

    private final RestTemplate restTemplate;
    private final CommandParser commandParser;
    private static final String BASE_URL = "http://localhost:8080/api/packages";

    @ShellMethod("Создать новую посылку")
    public String create(@ShellOption(help = "Строка параметров команды") String command) {
        try {
            CreateCommandDto dto = commandParser.parseToCreateDto(command);
            String url = BASE_URL + "/create";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении команды CREATE: " + e.getMessage());
        }
    }

    @ShellMethod("Обновить существующую посылку")
    public String update(@ShellOption(help = "Строка параметров команды") String command) {
        try {
            UpdateCommandDto dto = commandParser.parseToUpdateDto(command);
            String url = BASE_URL + "/update";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении команды UPDATE: " + e.getMessage());
        }
    }

    @ShellMethod("Погрузить посылки в грузовики")
    public String load(@ShellOption(help = "Строка параметров команды") String command) {
        try {
            LoadCommandDto dto = commandParser.parseToLoadDto(command);
            String url = BASE_URL + "/load";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoadCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении команды LOAD: " + e.getMessage());
        }
    }

    @ShellMethod("Выгрузить посылки из грузовиков")
    public String unload(@ShellOption(help = "Строка параметров команды") String command) {
        try {
            UnloadCommandDto dto = commandParser.parseToUnloadDto(command);
            String url = BASE_URL + "/unload";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UnloadCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении команды UNLOAD: " + e.getMessage());
        }
    }

    @ShellMethod("Рассчитать стоимость")
    public String billing(@ShellOption(help = "Строка параметров команды") String command) {
        try {
            BillingCommandDto dto = commandParser.parseToBillingDto(command);
            String url = BASE_URL + "/billing";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<BillingCommandDto> entity = new HttpEntity<>(dto, headers);

            return restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении команды BILLING: " + e.getMessage());
        }
    }

    @ShellMethod("Поиск посылки")
    public String find(@ShellOption String name) {
        String url = String.format("http://localhost:8080/api/packages/find/%s", name.trim());
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске посылки: " + e.getMessage());
        }
    }

    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption String name) {
        String url = String.format("http://localhost:8080/api/packages/%s", name.trim());
        try {
            restTemplate.delete(url);
            return "Посылка успешно удалена.";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении посылки: " + e.getMessage());
        }
    }

    @ShellMethod("Получение списка посылок")
    public String list(@ShellOption(defaultValue = "0") int page, @ShellOption(defaultValue = "10") int size) {
        String url = String.format("http://localhost:8080/api/packages/list?page=%d&size=%d", page, size);
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка посылок: " + e.getMessage());
        }
    }

    @ShellMethod("Закрытие")
    public String exit() {
        System.exit(0);
        return "Приложение закрыто";
    }
}

