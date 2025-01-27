package org.hofftech.edu.console.model.dto;

import lombok.Data;

@Data
public class BillingCommandDto {
    private String user;
    private String from;
    private String to;
}
