package org.hofftech.edu.console.model.dto;

import lombok.Data;

@Data
public class LoadCommandDto {
    private String parcelsText;
    private String trucks;
    private String user;
    private boolean even;
    private boolean easy;
    private boolean save;
}
