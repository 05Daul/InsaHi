package com.playdata.HumanResourceManagement.employee.dao;


import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorityDAOImpl implements AuthorityDAO {


    private final AuthorityRepository authorityRepository;

    // ROLE_ADMIN 조회
    public Optional<Authority> getAdminRole() {
        return authorityRepository.findByAuthorityName("ROLE_ADMIN");
    }

    // ROLE_USER 조회
    public Optional<Authority> getUserRole() {
        return authorityRepository.findByAuthorityName("ROLE_USER");
    }
}
