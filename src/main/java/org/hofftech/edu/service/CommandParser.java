package org.hofftech.edu.service;

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

    /**
     * Разбирает строку команды и преобразует её в объект ParsedCommand.
     *
     * @param command строка команды
     * @return объект ParsedCommand с извлечёнными данными
     */
    public ParsedCommand parse(String command) {
        Map<String, String> parameters = extractParameters(command);
        CommandType commandType = CommandType.fromCommand(command);

        ParsedCommand parsedCommand = createParsedCommand(parameters);
        parsedCommand.setCommandType(commandType);
        setOptionalParameters(parsedCommand, parameters);

        return parsedCommand;
    }

    private Map<String, String> extractParameters(String command) {
        Map<String, String> parameters = new HashMap<>();
        Pattern pattern = Pattern.compile("-([a-zA-Z]+)\\s+\"([^\"]+)\"|-([a-zA-Z]+)\\s+([^\\s]+)");
        Matcher matcher = pattern.matcher(command);

        while (matcher.find()) {
            if (matcher.group(1) != null && matcher.group(2) != null) {
                parameters.put("-" + matcher.group(1), matcher.group(2));
            } else if (matcher.group(3) != null && matcher.group(4) != null) {
                parameters.put("-" + matcher.group(3), matcher.group(4));
            }
        }

        return parameters;
    }

    private ParsedCommand createParsedCommand(Map<String, String> parameters) {
        boolean saveToFile = parameters.containsKey("-save");
        boolean useEasyAlgorithm = parameters.containsKey("-easy");
        boolean useEvenAlgorithm = parameters.containsKey("-even");
        boolean withCount = parameters.containsKey("-withCount");

        String parcelsText = parameters.get("-parcelsText");
        String parcelsFile = parameters.get("-parcelsFile");
        String trucks = parameters.get("-trucks");
        String inFile = parameters.get("-inFile");

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
        parsedCommand.setName(parameters.get("-name"));
        parsedCommand.setOldName(parameters.get("-oldName"));
        parsedCommand.setForm(parameters.get("-form"));
        parsedCommand.setSymbol(parameters.get("-symbol"));
    }


}
