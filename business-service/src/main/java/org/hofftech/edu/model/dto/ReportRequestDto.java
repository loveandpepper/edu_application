package org.hofftech.edu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private String userId;
    private String fromDate;
    private String toDate;
}
