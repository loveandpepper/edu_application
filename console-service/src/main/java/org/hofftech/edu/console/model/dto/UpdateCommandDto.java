package org.hofftech.edu.console.model.dto;

import lombok.Data;

@Data
public class UpdateCommandDto {
    private String oldName;
    private String name;
    private String form;
    private String symbol;
}
