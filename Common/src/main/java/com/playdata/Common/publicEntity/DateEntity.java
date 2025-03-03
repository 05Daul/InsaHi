package com.playdata.Common.publicEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@MappedSuperclass
public class DateEntity {

    @CreationTimestamp
    private LocalDate createDate; // insert 날짜
    @UpdateTimestamp
    private LocalDate updatedDate; //업데이트 날짜


    @CreationTimestamp
    private LocalDateTime createTime; // insert 시간
    @UpdateTimestamp
    private LocalDateTime updateTime; // 업데이트 시간

}
