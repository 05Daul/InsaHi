package com.playdata.AttendanceSalary.atdClient.hrmDTO;

import lombok.Data;

import java.time.LocalTime;
@Data
public class CompanyStartTimeDto {
    String companyCode;
    LocalTime startTime;
}
