package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import com.playdata.User.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEntity extends DateEntity {
//직급
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;
    private String positionName;
    private String companyCode;

    @OneToMany
    private List<Employee> employee;


}
