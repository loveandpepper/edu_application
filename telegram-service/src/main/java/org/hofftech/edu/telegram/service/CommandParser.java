package org.hofftech.edu.telegram.service;

import org.hofftech.edu.telegram.dto.BillingCommandDto;
import org.hofftech.edu.telegram.dto.CreateCommandDto;
import org.hofftech.edu.telegram.dto.LoadCommandDto;
import org.hofftech.edu.telegram.dto.UnloadCommandDto;
import org.hofftech.edu.telegram.dto.UpdateCommandDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис для разбора строк команд.
 * Преобразует команды в объекты DTO в зависимости от CommandType.
 */
@Service
public class CommandParser {

    private static final String COMMAND_REGEX = "\\+([a-zA-Z]+),?\\s*(\"[^\"]+\"|[^+]+)";
    private static final int GROUP_ONE = 1;
    private static final int GROUP_TWO = 2;

    /**
     * Извлечение параметров из строки команды.
     *
     * @param command строка команды
     * @return карта параметров
     */
    private Map<String, String> extractParameters(String command) {
        Map<String, String> parameters = new HashMap<>();
        Pattern pattern = Pattern.compile(COMMAND_REGEX);
        Matcher matcher = pattern.matcher(command);

        while (matcher.find()) {
            String key = matcher.group(GROUP_ONE);
            String value = matcher.group(GROUP_TWO);
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            value = value.replaceAll(",$", "").trim();
            parameters.put(key, value);
        }
        return parameters;
    }

    /**
     * Преобразование параметров в CreateCommandDto.
     */
    public CreateCommandDto parseToCreateDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        CreateCommandDto dto = new CreateCommandDto();
        dto.setName(parameters.get("name"));
        dto.setForm(parameters.get("form"));
        dto.setSymbol(parameters.get("symbol"));
        return dto;
    }

    /**
     * Преобразование параметров в UpdateCommandDto.
     */
    public UpdateCommandDto parseToUpdateDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        UpdateCommandDto dto = new UpdateCommandDto();
        dto.setOldName(parameters.get("oldName"));
        dto.setName(parameters.get("name"));
        dto.setForm(parameters.get("form"));
        dto.setSymbol(parameters.get("symbol"));
        return dto;
    }

    /**
     * Преобразование параметров в LoadCommandDto.
     */
    public LoadCommandDto parseToLoadDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        LoadCommandDto dto = new LoadCommandDto();
        dto.setParcelsText(parameters.get("parcelsText"));
        dto.setTrucks(parameters.get("trucks"));
        dto.setUser(parameters.get("user"));
        dto.setEven(Boolean.parseBoolean(parameters.getOrDefault("even", "false")));
        dto.setEasy(Boolean.parseBoolean(parameters.getOrDefault("easy", "false")));
        dto.setSave(Boolean.parseBoolean(parameters.getOrDefault("save", "false")));
        return dto;
    }

    /**
     * Преобразование параметров в UnloadCommandDto.
     */
    public UnloadCommandDto parseToUnloadDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        UnloadCommandDto dto = new UnloadCommandDto();
        dto.setInFile(parameters.get("inFile"));
        dto.setUser(parameters.get("user"));
        dto.setWithCount(Boolean.parseBoolean(parameters.getOrDefault("withCount", "false")));
        return dto;
    }

    /**
     * Преобразование параметров в BillingCommandDto.
     */
    public BillingCommandDto parseToBillingDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        BillingCommandDto dto = new BillingCommandDto();
        dto.setUser(parameters.get("user"));
        dto.setFrom(parameters.get("from"));
        dto.setTo(parameters.get("to"));
        return dto;
    }
}

