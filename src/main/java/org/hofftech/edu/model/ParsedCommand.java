package org.hofftech.edu.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ParsedCommand {
    private final String filePath;
    private final int maxTrucks;
    private final boolean saveToFile;
    private final boolean useEasyAlgorithm;
    private final boolean useEvenAlgorithm;
    private CommandType commandType;
}


