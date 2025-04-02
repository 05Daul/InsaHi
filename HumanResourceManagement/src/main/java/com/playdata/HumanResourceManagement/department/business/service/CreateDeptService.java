package com.playdata.HumanResourceManagement.department.business.service;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.department.business.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateDeptService {

  private final DepartmentRepository departmentRepository;

  /**
   * 부서 생성
   */
  @Transactional
  public ActionBasedOrganizationChartDTO createDepartment(String companyCode,
      OrganizationStructureDTO request) {
    DepartmentEntity parent = getParentDepartmentIfExists(request.getParentDepartmentId());

    DepartmentEntity department = DepartmentEntity.builder()
        .companyCode(companyCode)
        .departmentName(request.getDepartmentName())
        .parentDepartmentId(parent)
        .build();

    DepartmentEntity savedDepartment = departmentRepository.save(department);
    ActionBasedOrganizationChartDTO dto = ActionBasedOrganizationChartDTO.fromDepartment(
        savedDepartment, "CREATE");

//        publishAndUpdateCache(companyCode, dto);

    return dto;
  }

  /**
   * 부서 수정
   */
  @Transactional
  public ActionBasedOrganizationChartDTO updateDepartment(String companyCode, String departmentId,
      OrganizationStructureDTO request) {
    DepartmentEntity department = departmentRepository.findById(departmentId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 부서입니다. ID = " + departmentId));

    DepartmentEntity parent = getParentDepartmentIfExists(request.getParentDepartmentId());

    department.setDepartmentName(request.getDepartmentName());
    department.setParentDepartmentId(parent);

    DepartmentEntity updatedDepartment = departmentRepository.save(department);
    ActionBasedOrganizationChartDTO dto = ActionBasedOrganizationChartDTO.fromDepartment(
        updatedDepartment, "UPDATE");

//        publishAndUpdateCache(companyCode, dto);

    return dto;
  }

  /**
   * 부서 삭제
   */
  @Transactional
  public ActionBasedOrganizationChartDTO deleteDepartment(String companyCode, String departmentId) {
    DepartmentEntity department = departmentRepository.findById(departmentId)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 부서입니다. ID = " + departmentId));

    departmentRepository.delete(department);
    ActionBasedOrganizationChartDTO dto = ActionBasedOrganizationChartDTO.fromDepartment(department,
        "DELETE");

    return dto;
  }

  private DepartmentEntity getParentDepartmentIfExists(String parentDepartmentId) {
    if (parentDepartmentId == null) {
      return null;
    }
    System.out.println("parentDepartMentIDDDDDDD++++>>>>>>>" + parentDepartmentId);
    return departmentRepository.findById(parentDepartmentId)
        .orElseThrow(
            () -> new EntityNotFoundException("부모 부서를 찾을 수 없습니다. ID = " + parentDepartmentId));
  }
//
//    private void publishAndUpdateCache(String companyCode, ActionBasedOrganizationChartDTO dto) {
//        redisService.updateOrganizationChart(companyCode, dto);
//
//        switch (dto.getAction()) {
//            case "CREATE" -> sendService.publishDepartmentCreatedEvent(dto);
//            case "UPDATE" -> sendService.publishDepartmentUpdatedEvent(dto);
//        }
//    }
}
