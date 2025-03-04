package com.playdata.attendanceSalary.atdSalEntity.sal;
import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "severance_pay")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeverancePayEntity  extends DateEntity {

    @Id
    @Column(name = "직원ID", length = 50)
    private long employeeId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate retirementDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal severanceAmount;

    @Column(name = "회사코드", precision = 10)
    private BigDecimal companyCode;
}