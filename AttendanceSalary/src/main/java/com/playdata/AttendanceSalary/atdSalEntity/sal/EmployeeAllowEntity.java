package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.EmployeeAllowDTO;
import jakarta.persistence.*;
        import lombok.*;

        import java.math.BigDecimal;

@Entity
@Table(name = "employee_allowance")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAllowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeAllowId;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowance_type", nullable = false)
    private AllowanceType allowanceType;

//        @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pay_stub_id")
//    private PayStubEntity payStub;

    @Column(nullable = false)
    private BigDecimal amount;
//
//    EmployeeAllowDTO toDTO() {
//        EmployeeAllowDTO dto = new EmployeeAllowDTO();
//        dto.setEmployeeId(employeeId);
//        dto.setAllowanceType(allowanceType);
//        dto.setAmount(amount);
//        return dto;
//    }
}