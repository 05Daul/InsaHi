package com.playdata.HumanResourceManagement.employee.repository;

import com.playdata.HumanResourceManagement.employee.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(String authorityName);
}