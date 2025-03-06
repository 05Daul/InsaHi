package com.playdata.HumanResourceManagement.hrmRepository;

import com.playdata.HumanResourceManagement.hrmEntity.AddressBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<AddressBookEntity, Long> {
//
//    // 1. 회사코드로 연락처 조회
//    List<AddressBookEntity> findByCompanyCode(String companyCode);
//
//    // 2. 회사코드로 연락처 삭제
//    void deleteByCompanyCode(String companyCode);
//
}
