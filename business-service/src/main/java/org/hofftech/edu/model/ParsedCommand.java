package org.hofftech.edu.model;

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

    private boolean save;
    private boolean easy;
    private boolean even;

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


