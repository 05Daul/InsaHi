package com.playdata.common.publicEntity;


import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class DateEntity {

    @CreationTimestamp
    private LocalDateTime createDate; // insert 날짜
    @UpdateTimestamp
    private LocalDateTime updatedDate; //업데이트 날짜



}
