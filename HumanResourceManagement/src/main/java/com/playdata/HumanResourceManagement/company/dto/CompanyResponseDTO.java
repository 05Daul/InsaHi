package com.playdata.HumanResourceManagement.company.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
//김다울 추가 - starttime 설정
@Data
public class CompanyResponseDTO {
    private String companyCode;
    private LocalTime startTime;
    private String companyName;
    private String headCount;
    private Date createdAt;
    private String businessNumber;
    private String companyAddress;

}
