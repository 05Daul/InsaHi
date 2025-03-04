package com.playdata.attendanceSalary.atdSalEntity.cer;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "certificate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private long certificateId;

    @Column(name = "employee_id", nullable = false, length = 50)
    private long employeeId;


    @Column(name = "reason", length = 255)
    private String reason;

//    private enum processingStatus;  => 전저결재 받아옴.
//    private enum certificateType;


    @Column(name = "approval_id")
    private long approvalId;

    @Column(name = "회사코드", length = 100)
    private BigDecimal companyCode;
}