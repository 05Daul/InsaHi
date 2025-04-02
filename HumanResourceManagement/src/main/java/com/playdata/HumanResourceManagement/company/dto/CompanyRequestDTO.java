package com.playdata.HumanResourceManagement.company.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDTO {

  private String companyCode;
  private String companyName;
  private String companyAddress;
  private String headCount;
  private Date createdAt;
  private String businessNumber;
}
