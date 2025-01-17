package org.hofftech.edu.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий разобранную команду.
 * Содержит данные, извлеченные из строки команды.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ParsedCommand {
    private String name;
    private String oldName;
    private String form;
    private String symbol;

    private final boolean saveToFile;
    private final boolean useEasyAlgorithm;
    private final boolean useEvenAlgorithm;

    private final String parcelsText;
    private final String parcelsFile;
    private final String trucks;
    private final String inFile;
    private final boolean iswithCount;

    private CommandType commandType;
}


