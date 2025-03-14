package com.playdata.ElectronicApproval.repository;

import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLineEntity, String> {

  List<ApprovalLineEntity> findAllByEmployeeId(String employeeId);

  @Query("SELECT l "
      + "FROM ApprovalLineEntity l "
      + "WHERE l.approvalLineDetail.status = :status "
      + "and l.employeeId = :employeeId")
  List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalLineDetailStatus(
      @Param("employeeId") String employeeId,
      @Param("status") ApprovalStatus approvalStatus);

  @Query("SELECT l "
      + "FROM ApprovalLineEntity l "
      + "WHERE l.approvalLineDetail.status = 'PENDING' "
      + "AND l.employeeId = :employeeId "
      + "AND l.approvalOrder = ( " //approvalOrder가 가장 작은 line 찾기 - 메인쿼리의 line과 동일한 file
      + "   SELECT MIN(l2.approvalOrder) "
      + "   FROM ApprovalLineEntity l2  "
      + "   WHERE l2.approvalFile = l.approvalFile "
      + "   AND l2.approvalFile.status = 'PENDING'"
      + "   AND l2.approvalLineDetail.status = 'PENDING')")
  List<ApprovalLineEntity> findFirstPendingLinesByEmployee(@Param("employeeId") String employeeId);

}
