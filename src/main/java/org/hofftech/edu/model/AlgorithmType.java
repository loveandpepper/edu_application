package org.hofftech.edu.model;

public enum AlgorithmType {
    EASY("easyalgorithm"),
    EVEN("even"),
    DEFAULT("");

    private final String keyword;

    AlgorithmType(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public static AlgorithmType fromCommand(String command) {
        for (AlgorithmType type : values()) {
            if (command.contains(type.getKeyword())) {
                return type;
            }
        }
        return DEFAULT;
    }
}

