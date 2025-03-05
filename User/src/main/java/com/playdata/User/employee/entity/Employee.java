package com.playdata.User.employee.entity;

import com.playdata.User.company.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "employee_id", unique = true, length = 36 )
    private String employeeId;
    private String password; // 1
    private String name; //2
    private String email; //3
    private String phoneNumber; //4
    private String address; //5
    private String departmentId;
    private String teamId;
    private String state;

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "companyCode")
    private Company company;

    @ManyToMany
    @JoinTable(
            name="employee_authority",
            joinColumns = {@JoinColumn(name="employee_id",
                    referencedColumnName = "employee_id" )},
            inverseJoinColumns =
                    {@JoinColumn(name="authority_id",
                            referencedColumnName ="authority_id" )}
    )
    private Set<Authority> authoritylist;

    @PrePersist
    public void generateEmployeeId() {
        if (this.employeeId == null) {
            this.employeeId = UUID.randomUUID().toString().substring(0, 8);
        }
    }


    //   public Employee(String employeeId, String password, String name, String email, String gender){
//        this.employeeId = employeeId;
//        this.password = password;
//        this.name = name;
//        this.email = email;
//        this.gender = gender;
//
//    }


}
