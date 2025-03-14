package com.playdata.HumanResourceManagement.department.business.controller;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.department.business.service.CreateDeptService;
import com.playdata.HumanResourceManagement.department.business.service.MappingDeptService;
import com.playdata.HumanResourceManagement.department.deventAPI.Service.SendService;
import com.playdata.HumanResourceManagement.department.deventAPI.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/api/{companyCode}/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final CreateDeptService createDeptService;
    private final MappingDeptService mappingDeptService;
    private final SendService sendService;
    private final RedisService redisService;

    /**
     * 조직도 리스트 조회 (캐시 또는 DB 조회)
     */
    @GetMapping("/list")
    public ResponseEntity<List<FullOrganizationChartDTO>> getOrganizationChart(@PathVariable String companyCode) {
        List<FullOrganizationChartDTO> organizationChart = redisService.getOrganizationChart(companyCode);

        if (organizationChart == null || organizationChart.isEmpty()) {
            organizationChart = mappingDeptService.getOrganizationChart(companyCode);

            if (!organizationChart.isEmpty()) {
                redisService.saveOrganizationChart(companyCode, organizationChart);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }

        return ResponseEntity.ok(organizationChart);
    }

    /**
     * 부서 생성
     */
    @PostMapping("/create")
    public ResponseEntity<ActionBasedOrganizationChartDTO> createDepartment(
            @PathVariable String companyCode,
            @RequestBody OrganizationStructureDTO request) {

        ActionBasedOrganizationChartDTO createdDepartment = createDeptService.createDepartment(companyCode, request);

        redisService.updateOrganizationChart(companyCode, createdDepartment);
        sendService.publishDepartmentCreatedEvent(createdDepartment);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    /**
     * 부서 수정
     */
    @PutMapping("/edit/{departmentId}")
    public ResponseEntity<ActionBasedOrganizationChartDTO> editDepartment(
            @PathVariable String companyCode,
            @PathVariable String departmentId,
            @RequestBody OrganizationStructureDTO request) {

        ActionBasedOrganizationChartDTO updatedDepartment = createDeptService.updateDepartment(companyCode, departmentId, request);

        redisService.updateOrganizationChart(companyCode, updatedDepartment);
        sendService.publishDepartmentUpdatedEvent(updatedDepartment);

        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * 부서 삭제
     */
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable String companyCode,
            @PathVariable String departmentId) {

        ActionBasedOrganizationChartDTO deletedDepartment = createDeptService.deleteDepartment(companyCode, departmentId);

        redisService.deleteOrganizationChart(companyCode);
        sendService.publishDepartmentDeletedEvent(companyCode, departmentId);

        return ResponseEntity.noContent().build();
    }
}
