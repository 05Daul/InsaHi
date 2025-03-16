//package com.playdata.HumanResourceManagement.department.deventAPI.assembler;
//
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.EmployeeDataDTO;
//import com.playdata.HumanResourceManagement.department.deventAPI.dao.DepartmentKafkaDAO;
//import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.employee.entity.Employee;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//@AllArgsConstructor
//public class EventAssembler {
//
//    // 이벤트 생성자 맵 (DepartmentEntity에서 필요한 정보 추출 후 DAO 생성)
//    private static final Map<DepartmentAction, EventCreator> eventCreators = Map.of(
//            DepartmentAction.CREATED, (entity) -> DepartmentKafkaDAO.created(
//                    entity.getDepartmentId(), entity.getDepartmentName(), entity.getCompanyCode()
//            ),
//            DepartmentAction.UPDATED, (entity) -> DepartmentKafkaDAO.updated(
//                    entity.getDepartmentId(), entity.getDepartmentName(), entity.getCompanyCode()
//            ),
//            DepartmentAction.DELETED, (entity) -> DepartmentKafkaDAO.deleted(
//                    entity.getDepartmentId(), entity.getDepartmentName(), entity.getCompanyCode()
//            )
//    );
//
//    // 이벤트 생성 메서드
//    public DepartmentKafkaDAO createEvent(DepartmentEntity entity, DepartmentAction action) {
//        EventCreator creator = eventCreators.get(action);
//        if (creator == null) {
//            throw new IllegalArgumentException("지원되지 않는 이벤트 액션: " + action);
//        }
//        return creator.createEvent(entity);
//    }
//
//    // 함수형 인터페이스 (DepartmentEntity → DepartmentKafkaDAO 변환)
//    @FunctionalInterface
//    interface EventCreator {
//        DepartmentKafkaDAO createEvent(DepartmentEntity entity);
//    }
//
//    // 이벤트 액션 Enum
//    public enum DepartmentAction {
//        CREATED, UPDATED, DELETED
//    }
//
//    /**
//     * ActionBasedOrganizationChartDTO 변환
//     */
//    public ActionBasedOrganizationChartDTO createActionBasedDTO(DepartmentEntity department, String action) {
//        if (department == null) {
//            throw new IllegalArgumentException("부서 정보가 없습니다.");
//        }
//        return ActionBasedOrganizationChartDTO.fromDepartment(department, action);
//    }
//
//    /**
//     * FullOrganizationChartDTO 변환
//     */
//    public FullOrganizationChartDTO createFullOrganizationChartDTO(DepartmentEntity department, String action, List<Employee> employees) {
//        if (department == null) {
//            throw new IllegalArgumentException("부서 정보가 없습니다.");
//        }
//
//        // employees가 null이면 빈 리스트로 대체
//        List<EmployeeDataDTO> employeeDataDTOList = (employees == null) ? List.of() :
//                employees.stream()
//                        .map(EmployeeDataDTO::fromEntity)
//                        .collect(Collectors.toList());
//
//        return FullOrganizationChartDTO.fromEntity(department, employeeDataDTOList, action);
//    }
//}