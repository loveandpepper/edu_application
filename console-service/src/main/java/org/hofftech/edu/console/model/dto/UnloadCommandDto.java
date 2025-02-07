package org.hofftech.edu.console.model.dto;

import lombok.Data;

@Data
public class UnloadCommandDto {
    private String inFile;
    private String user;
    private boolean withCount;
}
