package com.playdata.HumanResourceManagement.fegin;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "atdsal-service") //feign을 이용하겠다. 어디를?
public interface PositionFeign {

//    @GetMapping("employ_id")
//    EmployeeResponseDTO getEmployId(@RequestParam("employ_id") String employId);

}
