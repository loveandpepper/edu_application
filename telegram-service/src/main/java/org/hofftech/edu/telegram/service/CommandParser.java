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

public class CommandParser {

    private static final Pattern RAW_COMMAND_PATTERN = Pattern.compile("\\+([a-zA-Z]+),?\\s*(\"[^\"]+\"|[^+]+)");
    private static final int GROUP_ONE = 1;
    private static final int GROUP_TWO = 2;
    private static final String NAME_KEY = "name";
    private static final String FORM_KEY = "form";
    private static final String SYMBOL_KEY = "symbol";
    private static final String OLD_NAME_KEY = "oldName";
    private static final String PARCELS_TEXT_KEY = "parcelsText";
    private static final String TRUCKS_KEY = "trucks";
    private static final String USER_KEY = "user";
    private static final String EVEN_KEY = "even";
    private static final String EASY_KEY = "easy";
    private static final String SAVE_KEY = "save";
    private static final String DEFAULT_VALUE = "false";
    private static final String IN_FILE_KEY = "inFile";
    private static final String WITH_COUNT_KEY = "withCount";
    private static final String FROM_KEY = "from";
    private static final String TO_KEY = "to";

    /**
     * Извлечение параметров из строки команды.
     *
     * @param command строка команды
     * @return карта параметров
     */
    private Map<String, String> extractParameters(String command) {
        Map<String, String> parameters = new HashMap<>();
        Matcher matcher = RAW_COMMAND_PATTERN.matcher(command);

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
        dto.setName(parameters.get(NAME_KEY));
        dto.setForm(parameters.get(FORM_KEY));
        dto.setSymbol(parameters.get(SYMBOL_KEY));
        return dto;
    }

    /**
     * Преобразование параметров в UpdateCommandDto.
     */
    public UpdateCommandDto parseToUpdateDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        UpdateCommandDto dto = new UpdateCommandDto();
        dto.setOldName(parameters.get(OLD_NAME_KEY));
        dto.setName(parameters.get(NAME_KEY));
        dto.setForm(parameters.get(FORM_KEY));
        dto.setSymbol(parameters.get(SYMBOL_KEY));
        return dto;
    }

    /**
     * Преобразование параметров в LoadCommandDto.
     */
    public LoadCommandDto parseToLoadDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        LoadCommandDto dto = new LoadCommandDto();
        dto.setParcelsText(parameters.get(PARCELS_TEXT_KEY));
        dto.setTrucks(parameters.get(TRUCKS_KEY));
        dto.setUser(parameters.get(USER_KEY));
        dto.setEven(Boolean.parseBoolean(parameters.getOrDefault(EVEN_KEY, DEFAULT_VALUE)));
        dto.setEasy(Boolean.parseBoolean(parameters.getOrDefault(EASY_KEY, DEFAULT_VALUE)));
        dto.setSave(Boolean.parseBoolean(parameters.getOrDefault(SAVE_KEY, DEFAULT_VALUE)));
        return dto;
    }

    /**
     * Преобразование параметров в UnloadCommandDto.
     */
    public UnloadCommandDto parseToUnloadDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        UnloadCommandDto dto = new UnloadCommandDto();
        dto.setInFile(parameters.get(IN_FILE_KEY));
        dto.setUser(parameters.get(USER_KEY));
        dto.setWithCount(Boolean.parseBoolean(parameters.getOrDefault(WITH_COUNT_KEY, DEFAULT_VALUE)));
        return dto;
    }

    /**
     * Преобразование параметров в BillingCommandDto.
     */
    public BillingCommandDto parseToBillingDto(String command) {
        Map<String, String> parameters = extractParameters(command);
        BillingCommandDto dto = new BillingCommandDto();
        dto.setUser(parameters.get(USER_KEY));
        dto.setFrom(parameters.get(FROM_KEY));
        dto.setTo(parameters.get(TO_KEY));
        return dto;
    }
}

