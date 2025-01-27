package org.hofftech.edu.telegram.dto;

import lombok.Data;

@Data
public class BillingCommandDto {
    private String user;
    private String from;
    private String to;
}
