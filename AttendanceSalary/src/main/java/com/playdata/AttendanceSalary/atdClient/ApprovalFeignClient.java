package com.playdata.AttendanceSalary.atdClient;

import org.springframework.cloud.openfeign.FeignClient;

//1
@FeignClient(name = "approval-service", url = "${approval.service.url}")
public interface ApprovalFeignClient {

}