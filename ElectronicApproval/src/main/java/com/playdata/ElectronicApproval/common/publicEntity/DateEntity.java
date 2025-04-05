package com.playdata.ElectronicApproval.common.publicEntity;


import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@MappedSuperclass
public class DateEntity {

  //1
  @CreationTimestamp
  private LocalDateTime createDate; // insert 날짜
  @UpdateTimestamp
  private LocalDateTime updatedDate; //업데이트 날짜


  @CreationTimestamp
  private LocalDateTime createTime; // insert 시간
  @UpdateTimestamp
  private LocalDateTime updateTime; // 업데이트 시간

}
