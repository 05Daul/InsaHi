package com.playdata.HumanResourceManagement.department.business.controller;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
import com.playdata.HumanResourceManagement.department.business.service.CreateDeptService;
import com.playdata.HumanResourceManagement.department.business.service.MappingDeptService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

  private final CreateDeptService createDeptService;
  private final MappingDeptService mappingDeptService;

  /**
   * 조직도 리스트 조회 (캐시 또는 DB 조회)
   */
  @GetMapping("{companyCode}/list")
  public ResponseEntity<List<FullOrganizationChartDTO>> getOrganizationChart(
      @PathVariable("companyCode") String companyCode) {
    // Redis 사용 없이 DB에서 직접 가져오는 부분으로 변경
    List<FullOrganizationChartDTO> organizationChart = mappingDeptService.getOrganizationChart(
        companyCode);

    log.info("companyCode::{}", companyCode);
    if (organizationChart.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    for (FullOrganizationChartDTO fullOrganizationChart : organizationChart) {
      log.info("companyCode::{}", companyCode);
      log.info("fullOrganizationChart.{}", fullOrganizationChart.getDepartmentId());
    }
    return ResponseEntity.ok(organizationChart);
  }

  /**
   * 부서 생성
   */
  @PostMapping("{companyCode}/create")
  public ResponseEntity<ActionBasedOrganizationChartDTO> createDepartment(
      @PathVariable("companyCode") String companyCode,
      @RequestBody OrganizationStructureDTO request) {

    log.info("companyCode::{}", companyCode);
    log.info("getParentDepartmentId:{}", request.getParentDepartmentId());
    log.info("getDepartmentName:{}", request.getDepartmentName());
    ActionBasedOrganizationChartDTO createdDepartment = createDeptService.createDepartment(
        companyCode, request);
    log.info("createdDepartment:{}", createdDepartment.getDepartmentName());
    log.info("createdDepartment:{}", createdDepartment.getDepartmentId());
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

    ActionBasedOrganizationChartDTO updatedDepartment = createDeptService.updateDepartment(
        companyCode, departmentId, request);

    return ResponseEntity.ok(updatedDepartment);
  }

  /**
   * 부서 삭제
   */
  @DeleteMapping("/delete/{departmentId}")
  public ResponseEntity<Void> deleteDepartment(
      @PathVariable String companyCode,
      @PathVariable String departmentId) {

    ActionBasedOrganizationChartDTO deletedDepartment = createDeptService.deleteDepartment(
        companyCode, departmentId);

    return ResponseEntity.noContent().build();
  }
}
