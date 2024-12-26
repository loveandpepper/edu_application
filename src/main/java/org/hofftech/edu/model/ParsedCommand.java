package org.hofftech.edu.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParsedCommand {
    private final String filePath;
    private final int maxTrucks;
    private final boolean useEasyAlgorithm;
    private final boolean useEvenAlgorithm;
}

