package org.hofftech.edu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс, представляющий разобранную команду.
 * Содержит данные, извлеченные из строки команды.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCommand {
    private String name;
    private String oldName;
    private String form;
    private String symbol;

    @JsonProperty("save")
    private boolean saveToFile;

    @JsonProperty("easy")
    private boolean isEasyAlgorithm;

    @JsonProperty("even")
    private boolean isEvenAlgorithm;

    private String user;
    private String from;
    private String to;
    private String parcelsText;
    private String parcelsFile;
    private String trucks;
    private String inFile;
    private boolean iswithCount;

    private CommandType commandType;

    private Integer page;
    private Integer size;
}


