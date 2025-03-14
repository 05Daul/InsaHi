package com.playdata.HumanResourceManagement.employee.dao;

import com.playdata.HumanResourceManagement.employee.entity.Authority;

import java.util.Optional;

public interface AuthorityDAO {

     Optional<Authority> getAdminRole();
     Optional<Authority> getUserRole();
}
