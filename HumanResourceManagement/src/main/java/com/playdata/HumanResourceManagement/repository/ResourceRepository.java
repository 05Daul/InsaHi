package com.playdata.HumanResourceManagement.repository;

import com.playdata.HumanResourceManagement.entity.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<AddressBook, Long> {

    // 1. 회사코드로 연락처 조회
    List<AddressBook> findByCompanyCode(String companyCode);

    // 2. 회사코드로 연락처 삭제
    void deleteByCompanyCode(String companyCode);
}
