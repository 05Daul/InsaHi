package com.playdata.User.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDTO {
    private String companyCode;
    private String companyName;
//    private String companyImage;
    private String companyAddress;
    private String headCount;
//    private String category;
    private Date createdAt;
    private String businessNumber;
//    private Timestamp startTime;
}
