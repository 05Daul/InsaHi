package com.playdata.HumanResourceManagement.addressBook.repository;

import com.playdata.HumanResourceManagement.addressBook.entity.AddressBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<AddressBookEntity, Long> {

    // 1. 회사코드로 연락처 조회
    List<AddressBookEntity> findByCompanyCompanyCode(String companyCode);
    // 2. 회사코드로 연락처 삭제
    void deleteByCompanyCompanyCode(String companyCode);

}
