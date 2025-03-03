package com.playdata.Common.publicEntity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

public class PersonEntity {
    @CreatedBy
    private char createByWho; // 작성자
    @LastModifiedBy
    private char updateByWho; // 수정자

}
