package org.hofftech.edu.service;

import org.hofftech.edu.factory.CommandTypeFactory;
import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.model.ParsedCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис для разбора строк команд.
 * Преобразует команды в объект ParsedCommand с извлечёнными параметрами.
 */

public class CommandParser {
    private final static String COMMAND_REGEX = "-([a-zA-Z]+)\\s+\"([^\"]+)\"|-([a-zA-Z]+)\\s+([^\\s]+)";
    private static final String SAVE = "-save";
    private static final String EASY = "-easy";
    private static final String EVEN = "-even";
    private static final String WITH_COUNT = "-withCount";
    private static final String PARCELS_TEXT = "-parcelsText";
    private static final String PARCELS_FILE = "-parcelsFile";
    private static final String TRUCKS = "-trucks";
    private static final String IN_FILE = "-inFile";
    private static final String NAME = "-name";
    private static final String OLD_NAME = "-oldName";
    private static final String FORM = "-form";
    private static final String SYMBOL = "-symbol";
    private static final int GROUP_ONE = 1;
    private static final int GROUP_TWO = 2;
    private static final int GROUP_THREE = 3;
    private static final int GROUP_FOUR = 4;

    /**
     * Разбирает строку команды и преобразует её в объект ParsedCommand.
     *
     * @param command строка команды
     * @return объект ParsedCommand с извлечёнными данными
     */
    public ParsedCommand parse(String command) {
        Map<String, String> parameters = extractParameters(command);
        String firstArgumentFromCommand = command.split(" ")[0].
                replaceFirst("^/", "").toUpperCase();

        CommandType commandType = CommandTypeFactory.determineCommandType(firstArgumentFromCommand);

        ParsedCommand parsedCommand = createParsedCommand(parameters);
        parsedCommand.setCommandType(commandType);
        setOptionalParameters(parsedCommand, parameters);

        return parsedCommand;
    }

    private Map<String, String> extractParameters(String command) {
        Map<String, String> parameters = new HashMap<>();
        Pattern pattern = Pattern.compile(COMMAND_REGEX);
        Matcher matcher = pattern.matcher(command);

        while (matcher.find()) {
            if (matcher.group(GROUP_ONE) != null && matcher.group(GROUP_TWO) != null) {
                parameters.put("-" + matcher.group(GROUP_ONE), matcher.group(GROUP_TWO));
            } else if (matcher.group(GROUP_THREE) != null && matcher.group(GROUP_FOUR) != null) {
                parameters.put("-" + matcher.group(GROUP_THREE), matcher.group(GROUP_FOUR));
            }
        }

        return parameters;
    }

    private ParsedCommand createParsedCommand(Map<String, String> parameters) {
        boolean saveToFile = parameters.containsKey(SAVE);
        boolean useEasyAlgorithm = parameters.containsKey(EASY);
        boolean useEvenAlgorithm = parameters.containsKey(EVEN);
        boolean withCount = parameters.containsKey(WITH_COUNT);

        String parcelsText = parameters.get(PARCELS_TEXT);
        String parcelsFile = parameters.get(PARCELS_FILE);
        String trucks = parameters.get(TRUCKS);
        String inFile = parameters.get(IN_FILE);

        return new ParsedCommand(
                saveToFile,
                useEasyAlgorithm,
                useEvenAlgorithm,
                parcelsText,
                parcelsFile,
                trucks,
                inFile,
                withCount
        );
    }

    private void setOptionalParameters(ParsedCommand parsedCommand, Map<String, String> parameters) {
        parsedCommand.setName(parameters.get(NAME));
        parsedCommand.setOldName(parameters.get(OLD_NAME));
        parsedCommand.setForm(parameters.get(FORM));
        parsedCommand.setSymbol(parameters.get(SYMBOL));
    }


}
