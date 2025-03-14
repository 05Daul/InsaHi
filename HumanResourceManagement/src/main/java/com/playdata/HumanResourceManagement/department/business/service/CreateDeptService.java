package com.playdata.HumanResourceManagement.department.business.service;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.EmployeeDataDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO; // 변경
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.department.business.repository.DepartmentRepository;
import com.playdata.HumanResourceManagement.department.deventAPI.Service.SendService;
import com.playdata.HumanResourceManagement.department.deventAPI.redis.RedisService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateDeptService {

    private final DepartmentRepository departmentRepository;
    private final RedisService redisService;
    private final SendService sendService;

    /**
     * 부서 생성
     */
    @Transactional
    public ActionBasedOrganizationChartDTO createDepartment(String companyCode, OrganizationStructureDTO request) {
        DepartmentEntity parentDepartment = getParentDepartmentIfExists(request.getParentDepartmentId());

        DepartmentEntity department = DepartmentEntity.builder()
                .companyCode(companyCode)
                .departmentName(request.getDepartmentName())
                .parentDepartmentId(parentDepartment)
                .build();

        department = departmentRepository.save(department);
        ActionBasedOrganizationChartDTO createdDepartment = ActionBasedOrganizationChartDTO.fromDepartment(department, "CREATE");

        // Redis 업데이트 및 Kafka 이벤트 발행
        redisService.updateOrganizationChart(companyCode, createdDepartment);
        sendService.publishDepartmentCreatedEvent(createdDepartment);

        return createdDepartment;
    }

    /**
     * 부서 수정
     */
    @Transactional
    public ActionBasedOrganizationChartDTO updateDepartment(String companyCode, String departmentId, OrganizationStructureDTO request) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 부서입니다."));

        DepartmentEntity parentDepartment = getParentDepartmentIfExists(request.getParentDepartmentId());

        department.setDepartmentName(request.getDepartmentName());
        department.setParentDepartmentId(parentDepartment);
        department = departmentRepository.save(department);

        ActionBasedOrganizationChartDTO updatedDepartment = ActionBasedOrganizationChartDTO.fromDepartment(department, "UPDATE");

        // Redis 업데이트 및 Kafka 이벤트 발행
        redisService.updateOrganizationChart(companyCode, updatedDepartment);
        sendService.publishDepartmentUpdatedEvent(updatedDepartment);

        return updatedDepartment;
    }

    /**
     * 부서 삭제
     */
    @Transactional
    public ActionBasedOrganizationChartDTO deleteDepartment(String companyCode, String departmentId) {
        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 부서입니다."));

        departmentRepository.delete(department);
        ActionBasedOrganizationChartDTO deletedDepartment = ActionBasedOrganizationChartDTO.fromDepartment(department, "DELETE");

        // Redis 삭제 및 Kafka 이벤트 발행
        redisService.deleteOrganizationChart(companyCode);
        sendService.publishDepartmentDeletedEvent(companyCode, departmentId);

        return deletedDepartment;
    }

    /**
     * 부모 부서 조회 (없으면 null 반환)
     */
    private DepartmentEntity getParentDepartmentIfExists(String parentDepartmentId) {
        if (parentDepartmentId == null) {
            return null;
        }
        return departmentRepository.findById(parentDepartmentId)
                .orElseThrow(() -> new EntityNotFoundException("부모 부서를 찾을 수 없습니다."));
    }
}
