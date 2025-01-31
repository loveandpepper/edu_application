package org.hofftech.edu.telegram.dto;

import lombok.Data;

@Data
public class CreateCommandDto {
    private String name;
    private String form;
    private String symbol;
}
