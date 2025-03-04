package com.playdata.User.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="authority")
public class Authority {
    @Id
    @GeneratedValue
    private Long authorityId;
    @Column(length = 50)
    private String authorityName;
    //company 관계매핑
    private String companyCode;
}
