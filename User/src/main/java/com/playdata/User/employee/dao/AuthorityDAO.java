package com.playdata.User.employee.dao;

import com.playdata.User.employee.entity.Authority;

import java.util.Optional;

public interface AuthorityDAO {

     Optional<Authority> getAdminRole();
     Optional<Authority> getUserRole();
}
