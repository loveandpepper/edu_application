package org.hofftech.edu.service;

import org.hofftech.edu.model.CommandType;
import org.hofftech.edu.model.ParsedCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {

    public ParsedCommand parse(String command) {
        Map<String, String> parameters = new HashMap<>();

        String[] parts = command.split("\\s+");
        for (String part : parts) {
            if (part.contains("=")) {
                String[] keyValue = part.split("=", 2);
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        CommandType commandType = CommandType.fromCommand(command);

        String filePath = parameters.getOrDefault("file", null);
        int maxTrucks = Integer.parseInt(parameters.getOrDefault("max", Integer.toString(Integer.MAX_VALUE)));
        boolean saveToFile = Boolean.parseBoolean(parameters.getOrDefault("save", "false"));
        boolean useEasyAlgorithm = Boolean.parseBoolean(parameters.getOrDefault("easy", "false"));
        boolean useEvenAlgorithm = Boolean.parseBoolean(parameters.getOrDefault("even", "false"));

        ParsedCommand parsedCommand = new ParsedCommand(filePath, maxTrucks, saveToFile, useEasyAlgorithm, useEvenAlgorithm);
        parsedCommand.setCommandType(commandType);

        return parsedCommand;
    }
}
